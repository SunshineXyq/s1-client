package com.bluedatax.s1_wdp_code.utils;

/**
 * Created by xuyuanqiang on 5/30/16.
 */
public class AccidentStateData {
    private int dotStyle;
    private String date;
    private String info;

    public AccidentStateData(int dotStyle, String date, String info) {
        this.dotStyle = dotStyle;
        this.date = date;
        this.info = info;
    }

    public int getDotStyle() {
        return dotStyle;
    }

    public void setDotStyle(int dotStyle) {
        this.dotStyle = dotStyle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
