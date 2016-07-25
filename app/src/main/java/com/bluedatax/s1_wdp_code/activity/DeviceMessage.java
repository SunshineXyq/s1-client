package com.bluedatax.s1_wdp_code.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bluedatax.s1_wdp_code.R;

public class DeviceMessage extends ActionBarActivity {

    private TextView onlineStatus;
    private TextView gdid;
    private TextView loginTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_message);
        onlineStatus = (TextView) findViewById(R.id.online_status);
        gdid = (TextView) findViewById(R.id.device_num);
        loginTime = (TextView) findViewById(R.id.device_time);
    }

    @Override
    protected void onResume() {
        if (getIntent().getStringExtra("id").equals("deviceOne")) {
            if (getIntent().getStringExtra("oneStatus").equals("0")) {
                onlineStatus.setText("离线");
            }
            gdid.setText(getIntent().getStringExtra("oneGdid"));
            loginTime.setText(getIntent().getStringExtra("oneTime"));
        } else if (getIntent().getStringExtra("id").equals("deviceTwo")) {
            if (getIntent().getStringExtra("twoStatus").equals("0")) {
                onlineStatus.setText("离线");
            }
            gdid.setText(getIntent().getStringExtra("twoGdid"));
            loginTime.setText(getIntent().getStringExtra("twoTime"));
        } else if (getIntent().getStringExtra("id").equals("deviceThree")) {
            if (getIntent().getStringExtra("threeStatus").equals("0")) {
                onlineStatus.setText("离线");
            }
            gdid.setText(getIntent().getStringExtra("threeGdid"));
            loginTime.setText(getIntent().getStringExtra("threeTime"));
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
