package com.firework.virtualdesktop.datagram;

/**
 * Created by lujie on 11/13/17.
 */

public class KeyDG {
    private int dgType = DGType.ANDROID_UNKOWN;
    private int keycode;

    /**
     * init
     */
    public KeyDG(int dgType, int keycode) {
        this.dgType = dgType;
        this.keycode = keycode;
    }

    /**
     * get the dgType
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
     * get the keycode
     */
    public int getKeycode() {
        return keycode;
    }

    /**
     * set the keycode
     */
    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }
}
