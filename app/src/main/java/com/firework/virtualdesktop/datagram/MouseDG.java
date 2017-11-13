package com.firework.virtualdesktop.datagram;

/**
 * Created by lujie on 11/13/17.
 */

public class MouseDG {
    private int dgType = DGType.ANDROID_UNKOWN;
    private int x;
    private int y;

    /**
     * init
     * @param dgType
     * @param x
     * @param y
     */
    public MouseDG(int dgType, int x, int y) {
        this.dgType = dgType;
        this.x = x;
        this.y = y;
    }

    /**
     * get the dgType
     * @return
     */
    public int getDgType() {
        return dgType;
    }

    /**
     * set the dgType
     * @param dgType
     */
    public void setDgType(int dgType) {
        this.dgType = dgType;
    }

    /**
     * get the x
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * set the x
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * get the y
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * set the y
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
}
