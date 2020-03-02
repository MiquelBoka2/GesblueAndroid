package com.boka2.gesblue.network;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
