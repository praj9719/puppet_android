package com.pentagon.puppet.extra;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

// 192.168.43.250:9999
// X-477<-->192.168.43.250<-->9999

/*
QR => name<-->IP<-->port
cmd => mouse_X<-->mouse_Y<-->mouse_click<-->key_presses
backspace => <-x
space => [-]
 */

public class GlobalConstant {

    public final String breaker = "<-->";
    public final String default_key = "xox";
    public final String enter = "@en";
    public final String delete = "@de";
    public final String space = "@sp";
    public final String terminate_connection = "@ex";
    public final int singleClick = 1;
    public final int doubleClick = 2;
    public final int rightClick = 3;
    public final String default_command = newCmd(0, 0, 0, default_key);

    public String newCmd(int x, int y, int c, String key){
        if (key.length() >= 4 && key.contains(breaker)) return default_command;
        return x + breaker + y + breaker + c + breaker + key;
    }

    public String tmpCmd(int x, int y, int c){
        return x + ":" + y + ":" + c;
    }

    public void toggleKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}
