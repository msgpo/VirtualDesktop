package com.firework.virtualdesktop.socket;

import java.io.DataInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.os.Message;

import com.firework.virtualdesktop.SpiceCanvas;
import com.firework.virtualdesktop.datagram.BitmapDG;

/**
 * Created by lujie on 11/13/17.
 */

public class FrameReciver {
    private SpiceCanvas canvas;
    private SocketHandler socketHandler = new SocketHandler("/home/lujie/AndroidStudioProjects/VirtualDesktop/socket_data/spice-output.socket");
    private boolean keepRecieve = true;
    private FrameRecieveT frameReciveT = null;
    private Options opt = null;

    /**
     * init
     * @param canvas
     */
    public FrameReciver(SpiceCanvas canvas) {
        this.canvas = canvas;
        opt = new Options();
        opt.inPreferredConfig = Config.ARGB_8888;
    }

    /**
     * start the frameReciever
     */
    public void startRecieveFrame() {
        if (frameReciveT == null) {
            frameReciveT = new FrameRecieveT();
            frameReciveT.start();
        }
    }

    /**
     * stop the frameReciever
     */
    public void stop() {
        keepRecieve = false;
        socketHandler.close();
    }

    /**
     * define a new class FrameReciverT
     */
    class FrameRecieveT extends Thread {
        public void run() {
            while (keepRecieve) {
                recive();
            }
        }
    }

    /**
     * recive the data
     */
    private void recive() {
        if (socketHandler.isConnected()) {
            try {
                DataInputStream inputStream = socketHandler.getInput();
                BitmapDG bmpDg = canvas.getBitmapDG();
                bmpDg.setDgType(inputStream.readInt());
                bmpDg.setWidth(inputStream.readInt());
                bmpDg.setHeight(inputStream.readInt());
                bmpDg.setX(inputStream.readInt());
                bmpDg.setY(inputStream.readInt());
                int size = inputStream.readInt();

                byte[] bs = new byte[size];
                inputStream.readFully(bs);
                Bitmap bmpp = BitmapFactory.decodeByteArray(bs, 0, size, opt);
                bmpDg.setBitmap(combine(bmpp, bmpDg.getY()));

                Message message = new Message();
                message.what = SpiceCanvas.UPDATE_CANVAS;
                Connector.getInstance().getHandler().sendMessage(message);
            } catch (IOException e) {
                socketHandler.close();
            }
        } else {
            if (!socketHandler.connect()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    private Canvas cvs = null;
    private Bitmap bmpOverlay = null;
    private Bitmap combine(Bitmap bmp, int y) {
        if (bmpOverlay == null) {
            bmpOverlay = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            cvs = new Canvas(bmpOverlay);
            cvs.drawBitmap(bmp, 0, 0, null);
            return bmp;
        }
        cvs.drawBitmap(bmpOverlay, 0, 0, null);
        cvs.drawBitmap(bmp, 0, y, null);
        return bmpOverlay;
    }
}
