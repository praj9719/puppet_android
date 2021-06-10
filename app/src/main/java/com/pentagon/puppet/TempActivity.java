package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.VelocityTrackerCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pentagon.puppet.communicate.SendAsync;
import com.pentagon.puppet.communicate.SetupAsync;
import com.pentagon.puppet.communicate.SetupListener;
import com.pentagon.puppet.extra.C;
import com.pentagon.puppet.extra.Popup;
import com.pentagon.puppet.object.Device;

import java.net.Socket;
import java.util.Stack;

public class TempActivity extends AppCompatActivity {

    private static final String TAG = "TempActivity";
    private RelativeLayout mousePad;
    private ImageView keyboard;

    private Socket socket;
    private GestureDetector gestureDetector;
    private VelocityTracker mVelocityTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        mousePad = findViewById(R.id.mousepad);
        keyboard = findViewById(R.id.keyboard);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.setOnClickListener(view -> imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0));


//        Device server = getServer();
//        if (server != null) connect(server);
//        else Toast.makeText(this, "Server not found!", Toast.LENGTH_SHORT).show();
//        init();
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
        new SetupAsync(this, device, new SetupListener() {
            @Override
            public void onSuccess(Socket sct) {
                socket = sct;
                init();
            }

            @Override
            public void onFailed(String message) {
                new Popup(TempActivity.this, "Error", message).show();
            }
        }).execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
//        validateConnection();
        // Mouse
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                sendCommand(C.tmpCmd(0, 0, 2));
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                sendCommand(C.tmpCmd(0, 0, 1));
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
                    sendCommand(C.tmpCmd(x, y, 0));
                    break;
            }
            return true;
        });
        // Keyboard
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
        Log.d(TAG, "lobby: closing activity");
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

// TODO need to work on onResume, onPause, onStart, onFinish calls