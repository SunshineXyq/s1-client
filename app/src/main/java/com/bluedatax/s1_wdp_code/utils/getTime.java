package com.bluedatax.s1_wdp_code.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuyuanqiang on 5/10/16.
 */
public class getTime {
    private static String CurrentTime;

    public static String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        CurrentTime = sdformat.format(date);

        Log.d("静态方法当前时间为", CurrentTime);

        return CurrentTime;

    }
}
