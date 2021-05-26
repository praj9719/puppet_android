package com.pentagon.puppet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pentagon.puppet.fragment.HomeFragment;
import com.pentagon.puppet.fragment.MouseFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new HomeFragment());
        init();
    }

    @SuppressLint("NonConstantResourceId")
    private void init(){
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.bottom_nav_mouse:
                    fragment = new MouseFragment();
                    break;
                case R.id.bottom_nav_keyboard:
                    // TODO add keyboard fragment if needed
                    break;
                default:
                    fragment = new HomeFragment();
                    break;
            }
            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            return true;
        }
        return false;
    }

}