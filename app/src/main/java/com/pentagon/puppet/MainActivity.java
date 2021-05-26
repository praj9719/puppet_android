package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button mBtnQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mBtnQR = findViewById(R.id.btn_scan_qr_code);
        mBtnQR.setOnClickListener(view -> scanQR());
    }

    private void scanQR(){
        Toast.makeText(this, "Scanning....", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        new Handler().postDelayed(() -> {
//            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//        }, 1000);
    }
}