package com.bluedatax.s1_wdp_code.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.service.MyService;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class DeviceEvent extends ActionBarActivity implements View.OnClickListener{

    private String currentTime;
    private Button mButtonNo;
    private Button mButtonYes;
    private CircleImageView mCircleImage;
    private TextView accidentStartTime;
    private TextView accidentHappenTime;
    private TextView accidentDesc;
    private String desc;
    private String tm_s;
    private ArrayList listName;
    private ArrayList listTime;
    private String time;
    private final String ACTION_NAME = "发送广播";
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_event);
        NotificationManager manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manger.cancel(1);
        initWidget();
        currentTime = getCurrentTime();
        DeviceEventReceive eventReceive =new DeviceEventReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        registerReceiver(eventReceive, intentFilter);
        queryDeviceEventState();
        String accidentJson = getIntent().getStringExtra("accidentJson");
//        Log.d("AccidentReport接收到的json", accidentJson);

//        try {
//            JSONObject acceentJsonObject = new JSONObject(accidentJson);
//            JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
//            if(accentJsonBody.getJSONArray("event") != null) {
//                Log.d("判断event是否为空","*********");
//                parseAccentJsonObject(accidentJson);
                getTimeSubtraction();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void queryDeviceEventState() {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", "1-1001");
            jsonBody.put("eid", 1);
            jsonBody.put("req", "event");
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonObject);
            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s", "");
            jsonRang.put("dt_e", "");
            jsonBody.put("rang", jsonRang);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 112);
            jsonReq.put("body", jsonBody);
            jsonReq.put("token", LoginActivity.loginToken);

            MyService.client.send(jsonReq.toString());
        } catch (Exception e) {

        }
    }


    private class DeviceEventReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String eventState = intent.getStringExtra("deviceEventState");
            parseAccidentJsonObject(eventState);
        }
    }

    private void getTimeSubtraction() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {
            java.util.Date now = df.parse(currentTime);
            java.util.Date date=df.parse(tm_s);
            long l=now.getTime()-date.getTime();
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            time = ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
            Log.d("时间", time);
            System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
            accidentHappenTime.setText("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
            accidentStartTime.setText(tm_s);
            accidentDesc.setText(desc);
        }
        catch (Exception e)
        {
        }
    }

    private String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        String CurrentTime = sdformat.format(date);

        Log.d("AccidentReport获取的当前的时间", CurrentTime);

        return CurrentTime;
    }

    private void parseAccidentJsonObject(String accidentJson) {
        listName = new ArrayList();
        listTime = new ArrayList();

        try {
            JSONObject acceentJsonObject = new JSONObject(accidentJson);
            long acceentJsonObjectToken = acceentJsonObject.getLong("token");
            Log.d("返回的token数据", acceentJsonObjectToken + "");
            JSONObject accentJsonBody = acceentJsonObject.getJSONObject("body");
            Log.d("解析后的body数据", accentJsonBody + "");
            JSONObject auth_name = accentJsonBody.getJSONObject("auth_name");
            String event1 = auth_name.getString("event");
            Log.d("auth_name event", event1 + "");
            String auth_id = accentJsonBody.getString("auth_id");
            Log.d("auth_id", auth_id);
            JSONArray event = accentJsonBody.getJSONArray("event");
            Log.d("event", event + "");
            for (int i = 0; i < event.length(); i++) {
                JSONObject eventJson = event.getJSONObject(i);
                int eid = eventJson.getInt("eid");
                Log.d("设备事件通知eid", eid + "");
                String gdid = eventJson.getString("gdid");
                Log.d("gdid", gdid);
                String type = eventJson.getString("type");
                Log.d("type", type);
                String ename = eventJson.getString("ename");
                Log.d("ename", ename);
                String title = eventJson.getString("title");
                Log.d("title", title);
                desc = eventJson.getString("desc");
                Log.d("desc", desc);
                String tm_c = eventJson.getString("tm_c");
                Log.d("tm_c", tm_c);
                tm_s = eventJson.getString("tm_s");
                Log.d("tm_s", tm_s);
                String tm_e = eventJson.getString("tm_e");
                Log.d("tm_e", tm_e);
                String video = eventJson.getString("video");
                Log.d("video", video);
                String addr = eventJson.getString("addr");
                Log.d("addr", addr);
                JSONObject list = eventJson.getJSONObject("list");
                String accendTitle = list.getString("title");
                Log.d("title", accendTitle);
                JSONArray item = list.getJSONArray("item");
                for (int j = 0; j < item.length(); j++) {
                    JSONObject itemJson = item.getJSONObject(j);
                    int id = itemJson.getInt("id");
                    Log.d("id", id + "");
                    int idx = itemJson.getInt("idx");
                    Log.d("idx", idx + "");
                    String name = itemJson.getString("name");
                    listName.add(name);
                    Log.d("name", name);
                    String tm = itemJson.getString("tm");
                    listTime.add(tm);
                    Log.d("tm", tm);
                    String op_sign_name = itemJson.getString("op_sign_name");
                    Log.d("op_sign_name", op_sign_name);
                    int order_id = itemJson.getInt("order_id");
                    Log.d("order_id", order_id+"");
                    String order_status = itemJson.getString("order_status");
                    Log.d("order_status", order_status);

                    Iterator its = listTime.iterator();
                    while (its.hasNext()) {
                        Object obj = its.next();
                        Log.d("time******", obj + "");
                    }

                    Iterator it = listName.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next();
                        Log.d("name******",obj+"");

                    }
                }
            }
        }catch(Exception e){

        }
    }

    private void initWidget() {
        mButtonNo = (Button) findViewById(R.id.button_accident_no);
        mButtonNo.setOnClickListener(this);
        mButtonYes = (Button) findViewById(R.id.button_accident_yes);
        mButtonYes.setOnClickListener(this);
        mCircleImage = (CircleImageView) findViewById(R.id.circleimage_accident);
        mCircleImage.setOnClickListener(this);
        accidentStartTime = (TextView) findViewById(R.id.accident_start_time);
        accidentHappenTime = (TextView) findViewById(R.id.accident_happen_time);
        accidentDesc = (TextView) findViewById(R.id.accident_desc);
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.button_accident_no:
                // 回到主界面
                intent=new Intent(DeviceEvent.this, MainActivity.class);
                break;
            case R.id.button_accident_yes:
                //切换到事故状态界面
                intent=new Intent(DeviceEvent.this,AccidentState.class);
                intent.putExtra("accident_start_time", tm_s);
                intent.putExtra("accident_happen_time", time);
                intent.putStringArrayListExtra("name", listName);
                intent.putStringArrayListExtra("time", listTime);
                break;
            case R.id.circleimage_accident:
                //调用地图

                break;
        }
        startActivity(intent);
    }
  }

