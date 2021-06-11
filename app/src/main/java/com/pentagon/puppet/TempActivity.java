package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pentagon.puppet.communicate.SendAsync;
import com.pentagon.puppet.communicate.SetupAsync;
import com.pentagon.puppet.communicate.SetupListener;
import com.pentagon.puppet.extra.GlobalConstant;
import com.pentagon.puppet.extra.Popup;
import com.pentagon.puppet.object.Device;

import java.net.Socket;

public class TempActivity extends AppCompatActivity {

    private static final String TAG = "TempActivity";
    private RelativeLayout mousePad, paintPad;
    private ImageView keyboard, mTerminateConnection;
    private GlobalConstant gc;
    private TextView mServerName, mConnectionStatus, mLeftClick, mRightClick;

    private Socket socket;
    private GestureDetector gestureDetector;
    private VelocityTracker mVelocityTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        gc = new GlobalConstant();
        mousePad = findViewById(R.id.mousepad);
        paintPad = findViewById(R.id.paintpad);
        keyboard = findViewById(R.id.keyboard);
        mServerName = findViewById(R.id.server_name);
        mConnectionStatus = findViewById(R.id.connection_status);
        mTerminateConnection = findViewById(R.id.terminate_connection);
        mLeftClick = findViewById(R.id.left_click);
        mRightClick = findViewById(R.id.right_click);
        keyboard.setOnClickListener(view -> gc.toggleKeyboard(this));
        mTerminateConnection.setOnClickListener(view -> onBackPressed());
        mLeftClick.setOnClickListener(view -> sendCommand(gc.newCmd(0, 0, gc.singleClick, gc.default_key)));
        mRightClick.setOnClickListener(view -> sendCommand(gc.newCmd(0, 0, gc.rightClick, gc.default_key)));
        Device server = getServer();
        if (server != null) connect(server);
        else Toast.makeText(this, "Server not found!", Toast.LENGTH_SHORT).show();
        init();
    }


    private Device getServer(){
        String deviceinfo = getIntent().getStringExtra("deviceinfo");
        if (deviceinfo != null && !deviceinfo.isEmpty()) {
            String[] arr = deviceinfo.split("<-->");
            if (arr.length == 3) return new Device(arr[0], arr[1], arr[2]);
        }
        return null;
    }

    private void connect(Device device){
        mServerName.setText(device.getName());
        new SetupAsync(this, device, new SetupListener() {
            @Override
            public void onSuccess(Socket sct) {
                socket = sct;
                mConnectionStatus.setText("Connected");
            }

            @Override
            public void onFailed(String message) {
                new Popup(TempActivity.this, "Error", message).show();
            }
        }).execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        // Mouse Pad
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                sendCommand(gc.newCmd(0, 0, gc.doubleClick, gc.default_key));
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                sendCommand(gc.newCmd(0, 0, gc.singleClick, gc.default_key));
                return super.onSingleTapConfirmed(e);
            }
        });
        mousePad.setOnTouchListener((view, event) -> {
            gestureDetector.onTouchEvent(event);
            int index = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(index);
            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
                    else mVelocityTracker.clear();
                    mVelocityTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    int vX = (int)mVelocityTracker.getXVelocity(pointerId);
                    int vY = (int)mVelocityTracker.getYVelocity(pointerId);
                    int x = (int)(vX*0.01);
                    int y = (int)(vY*0.01);
                    sendCommand(gc.newCmd(x, y, 0, gc.default_key));
                    break;
            }
            return true;
        });
        // PaintPad
        paintPad.setOnTouchListener((view, event) -> {
            int index = event.getActionIndex();
            int action = event.getActionMasked();
            int pointerId = event.getPointerId(index);
            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
                    else mVelocityTracker.clear();
                    mVelocityTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mVelocityTracker.addMovement(event);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    int vX = (int)mVelocityTracker.getXVelocity(pointerId);
                    int vY = (int)mVelocityTracker.getYVelocity(pointerId);
                    int x = (int)(vX*0.01);
                    int y = (int)(vY*0.01);
                    sendCommand(gc.newCmd(x, y, gc.singleClick, gc.default_key));
                    break;
            }
            return true;
        });
    }


    private void sendCommand(String cmd){
        validateConnection();
        new SendAsync(socket, cmd).execute();

    }

    private void validateConnection(){
        if (socket == null || !socket.isConnected())
            new Popup(this, "Server is not connected!").onClick("Terminate", this::lobby);
    }

    private void lobby(){
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key;
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                key = gc.enter;
                break;
            case KeyEvent.KEYCODE_DEL:
                key = gc.delete;
                break;
            case KeyEvent.KEYCODE_SPACE:
                key = gc.space;
                break;
            default:
                char ch = (char) event.getUnicodeChar();
                key = String.valueOf(ch);
                break;
        }
        if (key.trim().isEmpty()) key = gc.default_key;
        sendCommand(gc.newCmd(0, 0, 0, key));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        new Popup(this, "Terminate Connection")
                .onClick("Terminate", () -> {
                    sendCommand(gc.terminate_connection);
                    lobby();
                });
    }
}

// TODO need to work on onResume, onPause, onStart, onFinish calls