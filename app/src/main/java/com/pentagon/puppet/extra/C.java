package com.pentagon.puppet.extra;

// 192.168.43.250:9999
// X-477<-->192.168.43.250<-->9999

/*
QR => name<-->IP<-->port
cmd => mouse_X<-->mouse_Y<-->mouse_click<-->key_presses

backspace => <-x
space => [-]

 */


public class C {


    // make sure breaker string is not in string key
    public static String breaker = "<-->";
    public static String default_command = newCmd(0, 0, 0, "xox");

    public static String newCmd(int x, int y, int c, String key){
        if (key.length() >= 4 && key.contains(breaker)) return default_command;
        return x + breaker + y + breaker + c + breaker + key;
    }

    public static String tmpCmd(int x, int y, int c){
        return x + ":" + y + ":" + c;
    }

}
