package com.pentagon.puppet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pentagon.puppet.communicate.SetupAsync;
import com.pentagon.puppet.communicate.SetupListener;
import com.pentagon.puppet.extra.Popup;
import com.pentagon.puppet.object.Device;

import java.io.IOException;
import java.net.Socket;

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
        popupConnect("X-477", "X-477<-->192.168.43.250<-->9999");
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
                if (txt != null && !txt.isEmpty()) {
                    String[] arr = txt.split("<-->");
                    if (arr.length == 3) popupConnect(arr[0], txt);
                    else popup(txt);
                }
            }
        }else super.onActivityResult(requestCode, resultCode, data);
    }

    private void popupConnect(String name, String deviceinfo){
        Log.d(TAG, "popupConnect: deviceinfo: " + deviceinfo);
        Popup popup = new Popup(this, "Server Found!", "Connect to " + name);
        popup.onClick("Connect", () -> {
            startActivity(new Intent(MainActivity.this, TempActivity.class).putExtra("deviceinfo", deviceinfo));
        });
    }

    private void popup(String msg){
        Popup popup = new Popup(this, msg);
        popup.onClick("Copy", () -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", msg);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

    }

}