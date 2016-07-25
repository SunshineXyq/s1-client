package com.bluedatax.s1_wdp_code.service;

import org.json.JSONObject;

/**
 * Created by xuyuanqiang on 5/10/16.
 */
public interface OnConnectListener {
    void onJSonObject(JSONObject json);
    void onError(Exception msg);
    void onConnected(String notice);
    void onRingJSONObject(JSONObject ringJSON);
    void onFamilyNumber(JSONObject familyJSON);
}
