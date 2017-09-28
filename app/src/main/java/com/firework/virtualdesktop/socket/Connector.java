package com.firework.virtualdesktop.socket;

/**
 * Created by lujie on 9/28/17.
 */
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.TabHost;

public class Connector {
    //load libspicec.so
    static {
        System.loadLibrary("spicec");
    }
    public native int AndroidSpicec(String cmd);

    //define the message code
    public static final int CONNECT_SUCCESS = 0;
    public static final int CONNECT_IP_PORT_ERROR = 1;
    public static final int CONNECT_PASSWORD_ERROR = 2;
    public static final int CONNECT_UNKOWN_ERROR = 3;

    private Handler handler = null;
    private int rs = CONNECT_SUCCESS;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    class StartRun extends Thread {
        private String cmd;

        public StartRun(String cmd) {
            this.cmd = cmd;
        }

        public void run() {
            long t1 = System.currentTimeMillis();
            rs = AndroidSpicec(cmd);
            Log.v("lujie", "Connect rs = " + rs + ",cost = " + (System.currentTimeMillis() - t1));
            //use message to notice the interface
            if (handler != null) {
                Message message = new Message();
                message.what = rs;
                handler.sendMessage(message);
            }
        }
    }

    //single case
    private static Connector connector = new Connector();

    public static Connector getInstance() {
        return connector;
    }

    public int connect(String ip, String port, String password) {
        StringBuffer buf = new StringBuffer();

        buf.append("spicy -h").append(ip);
        buf.append(" -p ").append(port);
        buf.append(" -w ").append(password);

        new StartRun(buf.toString()).start();
        //if success, ConnectT process will block all the time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        return rs;
    }

}
