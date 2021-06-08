package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pentagon.puppet.communicate.SetupAsync;
import com.pentagon.puppet.communicate.SetupListener;
import com.pentagon.puppet.extra.Popup;
import com.pentagon.puppet.object.Device;

import java.net.Socket;
import java.text.DecimalFormat;

public class TempActivity extends AppCompatActivity {

    private static final String TAG = "TempActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Device server = getServer();
        if (server != null) connect(server);
        else {
            Log.d(TAG, "onCreate: server is null!");
        }
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
            public void onSuccess(Socket socket) {
                Log.d(TAG, "onSuccess: success");
            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "onFailed: message");
            }
        }).execute();
    }

}