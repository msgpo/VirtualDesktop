package com.firework.virtualdesktop.datagram;

import android.graphics.Bitmap;

/**
 * Created by lujie on 11/13/17.
 */

public class BitmapDG {
    private int dgType = DGType.ANDROID_SHOW;
    private int w = 0;
    private int h = 0;
    private int x = 0;
    private int y = 0;
    private Bitmap bitmap;

    /**
     * return the dgTYpe
     */
    public int getDgType() {
        return dgType;
    }

    /**
     * set the dgType
     */
    public void setDgType(int dgType) {
        this.dgType = dgType;
    }

    /**
     * return the width
     */
    public int getWidth() {
        return w;
    }

    /**
     * set the width
     */
    public void setWidth(int w) {
        if (this.w < w) {
            this.w = w;
        }
    }

    /**
     * return the height
     */
    public int getHeight() {
        return h;
    }

    /**
     * set the height
     */
    public void setHeight(int h) {
        if (this.h < h) {
            this.h = h;
        }
    }

    /**
     * get the x
     */
    public int getX() {
        return x;
    }

    /**
     * set the x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get the y
     */
    public int getY() {
        return y;
    }

    /**
     * set the y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * get the bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * set the bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
