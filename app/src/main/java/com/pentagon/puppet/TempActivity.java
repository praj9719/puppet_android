package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pentagon.puppet.communicate.SendAsync;
import com.pentagon.puppet.communicate.SetupAsync;
import com.pentagon.puppet.communicate.SetupListener;
import com.pentagon.puppet.extra.C;
import com.pentagon.puppet.extra.Popup;
import com.pentagon.puppet.object.Device;

import java.net.Socket;

public class TempActivity extends AppCompatActivity {

    private static final String TAG = "TempActivity";
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Device server = getServer();
        if (server != null) connect(server);
        else Toast.makeText(this, "Server not found!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TempActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }


    private void init(){
        validateConnection();
        sendCommand(C.tmpCmd(0, 0, 0));
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


}

// TODO need to work on onResume, onPause, onStart, onFinish calls