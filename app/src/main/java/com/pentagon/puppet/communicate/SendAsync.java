package com.pentagon.puppet.communicate;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SendAsync extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SendAsync";

    private Socket socket;
    private String cmd;

    public SendAsync(Socket socket, String cmd) {
        this.socket = socket;
        this.cmd = cmd;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] messageBytes = cmd.getBytes();
            int messageLen = messageBytes.length;
            byte[] messageLenBytes = new byte[4];
            messageLenBytes[0] = (byte) (messageLen & 0xff);
            messageLenBytes[1] = (byte) ((messageLen >> 8) & 0xff);
            messageLenBytes[2] = (byte) ((messageLen >> 16) & 0xff);
            messageLenBytes[3] = (byte) ((messageLen >> 24) & 0xff);
            outputStream.write(messageLenBytes);
            outputStream.write(messageBytes);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: Exception: " + e.getMessage());
        }
        return null;
    }
}
