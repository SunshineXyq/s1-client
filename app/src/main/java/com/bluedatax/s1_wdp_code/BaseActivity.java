package com.bluedatax.s1_wdp_code;

import android.app.Activity;
import android.os.Bundle;

import com.bluedatax.s1_wdp_code.utils.ActivityControl;

/**
 * Created by xuyuanqiang on 5/21/16.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityControl.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControl.finishAll();
    }
}
