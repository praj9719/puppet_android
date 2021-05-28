package com.pentagon.puppet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pentagon.puppet.object.Device;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mBtnQR;
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        qrScan = new IntentIntegrator(this);
        mBtnQR = findViewById(R.id.btn_scan_qr_code);
        mBtnQR.setOnClickListener(view -> qrScan.initiateScan());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null) Toast.makeText(this, "Result not found!", Toast.LENGTH_SHORT).show();
            else{
                String txt = result.getContents();
                if (txt != null && !txt.isEmpty()) popupDevice(new Device("S-707", "194.214.165.117", "9999"));
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    private void popupDevice(Device device){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(device.getName())
                .setMessage(device.getIP()+":"+device.getPort())
                .setPositiveButton("Connect", (dialogInterface, i) -> connectDevice(device))
                .setNegativeButton("Cancel", (dialogInterface, i) -> {})
                .create();
        dialog.show();
    }

    private void connectDevice(Device device){
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }


}