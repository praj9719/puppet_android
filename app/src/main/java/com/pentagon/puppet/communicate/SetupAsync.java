package com.pentagon.puppet.communicate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pentagon.puppet.object.Device;

import java.net.Socket;

public class SetupAsync extends AsyncTask<Void, Void, Socket> {

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
    protected Socket doInBackground(Void... voids) {
        Socket socket = null;
        try {
            socket = new Socket(String.valueOf(device.getIP()), Integer.parseInt(device.getPort()));
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            setupListener.onFailed(e.getMessage());
        }
        return socket;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        super.onPostExecute(socket);
        progressDialog.dismiss();
        if (socket == null) setupListener.onFailed("Null response!");
        else setupListener.onSuccess(socket);
    }
}
