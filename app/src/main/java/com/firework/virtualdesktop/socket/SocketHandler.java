package com.firework.virtualdesktop.socket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.util.Log;

/**
 * Created by lujie on 11/13/17.
 */

public class SocketHandler {
    /**
     * init
     */
    private LocalSocket socket = null;
    private DataInputStream inStream = null;
    private DataOutputStream outStream = null;
    private String filePath = null;

    public SocketHandler(String filePath) {
        this.filePath = filePath;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    /**
     * create a connection
     */
    public boolean connect() {
        try {
            Log.v("firework","Try to connec unix socket " + filePath);
            socket = new LocalSocket();
            socket.connect(new LocalSocketAddress(filePath, Namespace.FILESYSTEM));
            Log.v("firework","Unix socket " + filePath + " connected ...");
            return true;
        } catch (IOException e) {
            close();
        }
        return false;
    }

    public DataInputStream getInput() throws IOException {
        inStream = new DataInputStream(socket.getInputStream());
        return inStream;
    }

    public DataOutputStream getOutput() throws IOException {
        outStream = new DataOutputStream(socket.getOutputStream());
        return outStream;
    }

    /**
     * close the connection
     */
    public void close() {
        if (inStream != null) {
            try {
                inStream.close();
            } catch (IOException e) {

            }
        }

        if (outStream != null) {
            try {
                outStream.close();
            } catch (IOException e) {

            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
