package com.pentagon.puppet.communicate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.pentagon.puppet.object.Device;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SetupAsync extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SetupAsync";

    private final Device device;
    private final SetupListener setupListener;
    private final ProgressDialog progressDialog;

    public SetupAsync(Context context, Device device, SetupListener setupListener){
        this.device = device;
        this.setupListener = setupListener;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: init");
        progressDialog.setTitle("Connecting...");
        progressDialog.setMessage("Make sure server " + device.getName() + " is listening!");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket socket = new Socket(String.valueOf(device.getIP()), Integer.parseInt(device.getPort()));
            setupListener.onSuccess(socket);
        } catch (Exception e) {
            setupListener.onFailed(e.getMessage());
        }
        progressDialog.dismiss();
        return null;
    }
}
