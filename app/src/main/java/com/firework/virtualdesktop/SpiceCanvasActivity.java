package com.firework.virtualdesktop;

import com.firework.virtualdesktop.datagram.DGType;
import com.firework.virtualdesktop.datagram.KeyDG;
import com.firework.virtualdesktop.datagram.MouseDG;
import com.firework.virtualdesktop.socket.Connector;
import com.firework.virtualdesktop.socket.InputSender;
import com.firework.virtualdesktop.socket.FrameReciver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewDebug;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lujie on 11/14/17.
 */

public class SpiceCanvasActivity extends Activity {
    private SpiceCanvas canvas;
    private float scaling = 1;
    Handler handler;
    InputSender inputSender;
    FrameReciver frameReciver;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.spicecanvas);
        canvas = (SpiceCanvas) findViewById(R.id.spice_canvas);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SpiceCanvas.UPDATE_CANVAS:
                        canvas.invalidate();
                        break;
                    case Connector.CONNECT_UNKOWN_ERROR:
                        inputSender.stop();
                        frameReciver.stop();

                        AlertDialog.Builder builder = new AlertDialog.Builder(SpiceCanvasActivity.this);
                        builder.setTitle(R.string.error);
                        builder.setMessage(R.string.disconnected);
                        AlertDialog ad = builder.create();
                        ad.show();

                        finish();
                        break;
                    case Connector.CONNECT_SUCCESS:
                        inputSender.stop();
                        frameReciver.stop();

                        finish();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        inputSender = new InputSender();
        frameReciver = new FrameReciver(canvas);
        Connector.getInstance().setHandler(handler);

        canvas.setOnTouchListener(new SpiceCanvasOnTouchListener());

        Display display = getWindowManager().getDefaultDisplay();
        canvas.setDisplayWH(display.getWidth(), display.getHeight());

        frameReciver.startRecieveFrame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spicecanvasactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("firework", "keyDown = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                canvas.panTo(0, 0, scaling);
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                Log.v("firework", "CODE = " + keyCode);
                inputSender.sendKey(new KeyDG(DGType.ANDROID_KEY_PRESS, keyCode));
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.v("firework", "keyUp = " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                Log.v("firework", "CODE = " + keyCode);
                inputSender.sendKey(new KeyDG(DGType.ANDROID_KEY_RELEASE, keyCode));
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        inputSender.sendOverMsg();
        inputSender.stop();
        frameReciver.stop();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.input:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.showSoftInput(canvas, InputMethodManager.SHOW_IMPLICIT);
                canvas.requestFocus();
                return true;
            case R.id.zoomin:
                scaling += 0.25;
                if (scaling >= 2) {
                    scaling = 2;
                }
                canvas.zoom(scaling);
                return true;
            case R.id.zoomout:
                scaling -= 0.25;
                if (scaling <= 0.25) {
                    scaling = 0.25f;
                } else {

                }
                canvas.zoom(scaling);
                return true;
            case R.id.exit:
                inputSender.sendOverMsg();
                inputSender.stop();
                frameReciver.stop();

                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                manager.restartPackage(getPackageName());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class SpiceCanvasOnTouchListener implements OnTouchListener {
        float x1, y1, xs, ys = y1 = x1 = xs = 0;
        int xo, yo = xo = 0;
        long last = 0;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                xs = x1 = x;
                ys = y1 = y;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                xo = (int) (x1 - x);
                yo = (int) (y1 - y);
                canvas.pan(xo, yo, scaling);

                x1 = x;
                y1 = y;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                xo = Math.abs((int) (xs - x));
                yo = Math.abs((int) (ys - y));

                //single click
                if (xo <= 5 && yo <= 5) {
                    int nx = (int) ((x + canvas.getXOffset()) / scaling);
                    int ny = (int) ((y + canvas.getYOffset()) / scaling);
                    if ((event.getEventTime() - last) < 500) {
                        inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_PRESS, nx, ny));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {

                        }

                        inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_RELEASE, nx, ny));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {

                        }

                        inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_PRESS, nx, ny));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {

                        }

                        inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_RELEASE, nx, ny));
                        last = event.getEventTime();
                        return true;
                    }
                    inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_PRESS, nx, ny));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                    inputSender.sendMouse(new MouseDG(DGType.ANDROID_BUTTON_RELEASE, nx ,ny));
                }
                last = event.getEventTime();
            }
            return true;
        }
    }
}
