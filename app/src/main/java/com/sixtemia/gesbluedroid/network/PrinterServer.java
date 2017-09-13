package com.sixtemia.gesbluedroid.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PrinterServer implements Runnable {
    public static final int DEFAULT_PORT = 8006;
   
    private ServerSocket mServerSocket;
    private PrinterServerListener mListener;
    private boolean mRunning;
    
    public PrinterServer(PrinterServerListener listener) throws IOException {
        this(DEFAULT_PORT, listener);
    }
    
    public PrinterServer(int port, PrinterServerListener listener) throws IOException {
        if (listener == null) {
            throw new NullPointerException("The listener is null");
        }
                
        mListener = listener;
        mServerSocket = new ServerSocket(); // <-- create an unbound socket first
        mServerSocket.setReuseAddress(true);
        mServerSocket.bind(new InetSocketAddress(port),1); // <-- now bind it
        mRunning = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        while (mRunning) {
            Socket s = null;
            try {
                s = mServerSocket.accept();
                s.setKeepAlive(true);
                s.setTcpNoDelay(true);                    
            } catch (IOException e) {
                break;
            }
            
            try {
                mListener.onConnect(s);
            } catch (Exception e) {
                
            }                
        }       
    }

    public void close() throws IOException {
        mRunning = false;        
        mServerSocket.close();                
    }   
}
