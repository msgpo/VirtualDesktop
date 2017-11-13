package com.firework.virtualdesktop;

import android.widget.ImageView;
import com.firework.virtualdesktop.datagram.BitmapDG;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by lujie on 11/13/17.
 */

public class SpiceCanvas extends ImageView {
    public static final int UPDATE_CANVAS = 111;

    private BitmapDG bitmapDG = new BitmapDG();
    private Matrix matrix;
    private int x;
    private int y;
    private int displayWidth, displayHeight;
    private Paint paint = new Paint();

    /**
     * set the display's width and heigth
     * @param w
     * @param h
     */
    public void setDisplayWH(int w, int h) {
        this.displayWidth = w;
        this.displayHeight = h;
    }

    /**
     * get the bitmap
     * @return
     */
    public BitmapDG getBitmapDG() {
        return bitmapDG;
    }

    /**
     * get the X's offset
     * @return
     */
    public int getXOffset() {
        return x;
    }

    /**
     * get the Y's offset
     * @return
     */
    public int getYOffset() {
        return y;
    }

    /**
     * set some attrs and context
     * @param context
     * @param attrs
     */
    public SpiceCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /**
     * draw
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        if (bitmapDG.getBitmap() != null) {
            Bitmap bm = Bitmap.createBitmap(bitmapDG.getBitmap(), 0, 0, bitmapDG.getBitmap().getWidth(), bitmapDG.getBitmap().getHeight(), matrix, true);
            canvas.drawBitmap(bm, 0, 0, paint);
        }
    }

    /**
     * zoom in or zoom out
     * @param scaling
     */
    public void zoom(float scaling) {
        resetMatrix();
        matrix.postScale(scaling, scaling);
        setImageMatrix(matrix);
        pan(0, 0, scaling);
        invalidate();
    }

    /**
     * translation
     * @param px
     * @param py
     * @param scaling
     */
    public void pan(int px, int py, float scaling) {
        x += px;
        y += py;
        calcBorder(scaling);
        scrollTo(x, y);
    }

    /**
     * translate to another point
     * @param px
     * @param py
     * @param scaling
     */
    public void panTo(int px, int py, float scaling) {
        x = px;
        y = py;
        calcBorder(scaling);
        scrollTo(x, y);
    }

    private int blackSpaceSize = 40;

    /**
     * calc the border
     * @param scaling
     */
    private void calcBorder(float scaling) {
        if (x < - blackSpaceSize) {
            x = - blackSpaceSize;
        } else {
            if (bitmapDG != null && (x + displayWidth) > bitmapDG.getWidth() * scaling + blackSpaceSize) {
                x = (int) (bitmapDG.getWidth() * scaling) - displayWidth + blackSpaceSize;
            }

            if (y < - blackSpaceSize) {
                y = -blackSpaceSize;
            } else {
                if (bitmapDG != null && (y + displayHeight) > bitmapDG.getHeight() * scaling + blackSpaceSize) {
                    y = (int) (bitmapDG.getHeight() * scaling) - displayHeight + blackSpaceSize;
                }
            }
        }
    }

    /**
     * reset the Matrix
     */
    private void resetMatrix() {
        matrix.reset();
        matrix.preTranslate(0, 0);
    }
}
