package com.bluedatax.s1_wdp_code.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by xuyuanqiang on 5/21/16.
 */
public class ActivityControl {
    private static ArrayList<Activity> arrayList = new ArrayList<Activity>();

    public static void addActivity (Activity activity) {

        arrayList.add(activity);
    }

    public static void removeActivity (Activity activity) {

        arrayList.remove(activity);
    }

    public static void finishAll() {

        for (Activity activity : arrayList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
