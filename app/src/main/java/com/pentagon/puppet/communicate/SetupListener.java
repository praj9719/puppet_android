package com.pentagon.puppet.communicate;

import java.net.Socket;

public interface SetupListener {
    void  onSuccess(Socket socket);
    void onFailed(String message);
}
