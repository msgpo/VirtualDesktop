package com.firework.virtualdesktop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firework.virtualdesktop.socket.Connector;
import org.w3c.dom.Text;

/**
 * Created by lujie on 11/14/17.
 */

public class AndroidSpiceActivity extends Activity {
    private TextView ipText;
    private TextView portText;
    private TextView passwordText;
    private TextView resultView;
    private Button settingBtn;
    private Button connectBtn;

    static {
        System.loadLibrary("spicec");
    }
    public native String hello();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the data from the client textview
        ipText = (TextView)findViewById(R.id.ipText);
        portText = (TextView)findViewById(R.id.VMPortText);
        passwordText = (TextView)findViewById(R.id.passwordText);
        resultView = (TextView)findViewById(R.id.resultView);
        resultView.setTextColor(Color.RED);
        settingBtn = (Button)findViewById(R.id.SettingBtn);
        connectBtn = (Button)findViewById(R.id.ConnectBtn);


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AndroidSpiceActivity.this,hello(),Toast.LENGTH_SHORT).show();
            }
        });

        //btn click listener
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = ipText.getText().toString();
                String port = portText.getText().toString();
                String password = passwordText.getText().toString();
                if(StartSpice(ip, port, password)) {
                    toCanvas();
                }
            }
        });
    }

    private boolean StartSpice(String ip, String port, String password) {
        int rs = Connector.getInstance().connect(ip, port, password);
        switch(rs) {
            case Connector.CONNECT_IP_PORT_ERROR:
                resultView.setText(R.string.error_invalid_ip_port);
                return false;
            case Connector.CONNECT_PASSWORD_ERROR:
                resultView.setText(R.string.error_invalid_password);
                return false;
            case Connector.CONNECT_UNKOWN_ERROR:
                resultView.setText(R.string.error_connect_failed);
                return false;
            default:
                break;
        }
        return true;
    }

    private void toCanvas() {
        Intent intent = new Intent(this, SpiceCanvasActivity.class);
        startActivity(intent);
    }
}
