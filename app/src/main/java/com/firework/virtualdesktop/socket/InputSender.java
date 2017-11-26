package com.firework.virtualdesktop.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import android.util.Log;
import com.firework.virtualdesktop.datagram.DGType;
import com.firework.virtualdesktop.datagram.KeyDG;
import com.firework.virtualdesktop.datagram.MouseDG;

/**
 * Created by lujie on 11/13/17.
 */

public class InputSender {
    private SocketHandler socketHandler = new SocketHandler("/home/lujie/AndroidStudioProjects/VirtualDesktop/socket_data/spice-input.socket");

    /**
     *
     * @param keyDg
     */
    public void sendKey(KeyDG keyDg) {
        if (!socketHandler.isConnected()) {
            if (!socketHandler.connect()) {
                return;
            }
        }
        Log.v("firework", "SendKey:" + keyDg.getKeycode());
        try {
            DataOutputStream outputStream = socketHandler.getOutput();
            outputStream.writeInt(keyDg.getDgType());
            if (keyDg.getKeycode() == 56) {
                outputStream.writeInt(96);
            } else {
                outputStream.writeInt(keyDg.getKeycode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            socketHandler.close();
        }
    }

    /**
     *
     * @param mouseDg
     */
    public void sendMouse(MouseDG mouseDg) {
        if (!socketHandler.isConnected()) {
            if (!socketHandler.connect()) {
                return;
            }
        }
        Log.v("firework", "SendMouse:x=" + mouseDg.getX() + ",y=" + mouseDg.getY());
        try {
            DataOutputStream outputStream = socketHandler.getOutput();
            outputStream.writeInt(mouseDg.getDgType());
            outputStream.writeInt(mouseDg.getX());
            outputStream.writeInt(mouseDg.getY());
        } catch (IOException e) {
            e.printStackTrace();
            socketHandler.close();
        }
    }

    /**
     *
     */
    public void sendOverMsg() {
        if (!socketHandler.isConnected()) {
            if (!socketHandler.connect()) {
                return;
            }
        }
        try {
            DataOutputStream outputStream = socketHandler.getOutput();
            outputStream.writeInt(DGType.ANDROID_OVER);
        } catch (IOException e) {
            e.printStackTrace();
            socketHandler.close();
        }
    }

    /**
     *
     */
    public void stop() {
        socketHandler.close();
    }
}
