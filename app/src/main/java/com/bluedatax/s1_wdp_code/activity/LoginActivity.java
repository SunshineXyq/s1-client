package com.bluedatax.s1_wdp_code.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.service.MyService;
import com.bluedatax.s1_wdp_code.service.OnConnectListener;
import com.bluedatax.s1_wdp_code.utils.GetAppVersion;
import com.bluedatax.s1_wdp_code.utils.MD5Utils;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.utils.getTime;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener, LocationListener {
    private Button btLogin;
    private EditText etUsername;
    private EditText etPassword;
    private MyService myService;
    private Exception error;
    private Intent in;
    private static final String TAG = "LoginActivity";

    //Activity与service建立关联
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("123456", "*************");
            //返回一个MsgService对象
            myService = ((MyService.MyBinder) service).getService();
            System.out.println("绑定服务成功");
            startService(in);
            //注册回调接口来接收变化
            myService.setOnConnectListener(new OnConnectListener() {

                @Override
                public void onJSonObject(JSONObject json) {
                    try {
                        if (json.getInt("msg") == 10) {
                            parseInitJSONObject(json);
//                            openTimedTask();
                        } else if (json.getInt("msg") == 110) {
                            parseLoginJSONObject(json);
                        }
                    } catch (Exception e) {
                    }
                }
                public void onError(Exception msg) {
                    error = msg;
                    System.out.println(msg);
                }

                public void onConnected(String notice) {
                    Log.d("传递过来的notice", notice);
                    System.out.println(notice);
                    if (notice.equals("Connected")) {
                        initServer();
                    } else if (notice.equals("")) {
                        Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onRingJSONObject(JSONObject ringJSON) {
                }
                public void onFamilyNumber(JSONObject familyJSON) {
                }
            });
        }
    };
    private Activity mContext = this;
    private String CurrentTime;
    private TelephonyManager tm;
    private String name;
    private String sver;
    private String model;
    private StringBuffer sb;
    private String duid;
    private String aver;
    private String DEVICE_ID;
    private long initToken;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    public static long loginToken;
    private LocationManager mLocationManager;
    private String LocationData;
    private SharedPreferences.Editor mEditor;
    private String geo;
    private String lat;
    private String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        getPostion();
        CurrentTime = getTime.getCurrentTime();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        name = android.os.Build.MANUFACTURER;          //name
        Log.d("loginActivity", name);
        sver = android.os.Build.VERSION.RELEASE;    //sver
        Log.d("loginActivity", sver);
        model = Build.MODEL;                //device model 型号
        Log.d("loginActivity", model);
        GetAppVersion mGetAppVersion = new GetAppVersion(mContext);
        aver = mGetAppVersion.getVersion();      //aver
        Log.d("loginActivity", aver);
        DEVICE_ID = tm.getDeviceId();
        Log.d("loginActivity", DEVICE_ID);
        String MD5DeviceID = MD5Utils.encode(DEVICE_ID);
        Log.d("MD5设备号码", MD5DeviceID);
        sb = new StringBuffer(MD5DeviceID);
        sb.insert(6, "-");
        sb.insert(11, "-");
        sb.insert(16, "-");
        sb.insert(21, "-");
        Log.d("MD5设备号码", sb.toString());
        duid = sb.toString();

        in = new Intent(this, MyService.class);
        in.putExtra("duid", duid);
//        bindService(in, conn, Context.BIND_AUTO_CREATE);
        etUsername.setText(SharedPrefsUtil.getValue(mContext, "userPhone", ""));
    }

    private void initView() {
        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                checkLogin();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void checkLogin() {
        if (etUsername.getText().toString().trim().equals("")
                || etPassword.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "手机号或登录密码为空", Toast.LENGTH_LONG).show();
            etUsername.setFocusable(true);
        } else {
            if (etUsername.getText().toString().length() == 11) {
                String num = "[1][3458]\\d{9}";
                Pattern p = Pattern.compile(num);       //编译
                Matcher m = p.matcher(etUsername.getText().toString().trim());     //匹配程序
                if (m.matches()) {
                    saveUserPhone();
                    login();
                } else {
                    Toast.makeText(LoginActivity.this, "请输入正确格式的手机号", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "请输入11位手机号", Toast.LENGTH_LONG).show();
            }
        }
    }


    public String getUserPhone() {
        String phone = etUsername.getText().toString();
        if (phone != null) {
            return phone;
        }
        return "";
    }

    public void saveUserPhone() {
        String phone = getUserPhone();
        SharedPrefsUtil.putValue(mContext, "userPhone", phone);
    }

    private void login() {
        String user = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String md5Password = MD5Utils.encode(password);

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("upn", user);
            jsonBody.put("psw", md5Password);
            jsonBody.put("ts", CurrentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 110);
            jsonReq.put("token", initToken);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
        }
    }

    /**
     * 发送初始化数据
     */

    private void initServer() {
        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("duid", duid);
            jsonBody.put("name", name);
            jsonBody.put("model", model);
            jsonBody.put("sver", sver);
            jsonBody.put("aver", aver);
            jsonBody.put("type", "2");
            jsonBody.put("ts", CurrentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 10);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {
        }
    }

    /**
     * 解析初始化数据
     *
     * @param json
     */

    private void parseInitJSONObject(JSONObject json) {
        try {
            initToken = json.getLong("token");
            Log.d("解析后的token数据", initToken + "");
            JSONObject body = json.getJSONObject("body");
            Log.d("解析后的body数据", body + "");
            String htp = body.getString("htp");
            Log.d("解析后的htp数据", htp + "");
            String spn = body.getString("spn");
            Log.d("解析后的spn数据", spn + "");
            String laver = body.getString("laver");
            Log.d("解析后的laver数据", laver + "");
            String upgurl = body.getString("upgurl");
            Log.d("解析后的upgurl数据", upgurl + "");
            String appurl = body.getString("appurl");
            Log.d("解析后的appurl数据", appurl + "");
            String sturl = body.getString("sturl");
            Log.d("解析后的sturl数据", sturl + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析登录数据
     */

    private void parseLoginJSONObject(JSONObject json) {
        try {
            loginToken = json.getLong("token");
            Log.d("解析登录返回的token数据", loginToken + "");
            JSONObject body = json.getJSONObject("body");
            Log.d("解析后的body数据", body + "");
            int id = body.getInt("id");
            Log.d("id", id + "");
            String name = body.getString("name");
            Log.d("解析后的name数据", name);
            String auid = body.getString("auid");
            Log.d("auid", auid);
            int ast = body.getInt("ast");
            Log.d("解析后的ast数据", ast + "");
            String fub = body.getString("fub");
            Log.d("解析后的fub数据", fub);
            String ts = body.getString("ts");
            Log.d("解析后的tm数据", ts);
            SharedPrefsUtil.putValue(mContext, "fub", fub);
            SharedPrefsUtil.putValue(mContext, "auid", auid);
            SharedPrefsUtil.putLong(mContext, "loginToken", loginToken);
        } catch (Exception e) {
        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void getPostion() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);      //获取系统定位管理器
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationData = "Enabled:" + LocationManager.GPS_PROVIDER;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.i(TAG, "location:" + location.toString());
        geo = location.getLatitude() + "#" + location.getLongitude();
        lat = location.getLatitude() + "";
        lng = location.getLongitude() + "";

        SharedPreferences mShare1 = getSharedPreferences("count", MODE_PRIVATE);
        mEditor = mShare1.edit();
        mEditor.putString("lat", lat);
        mEditor.putString("lng", lng);
        Log.d("存入本地的经纬度", lat + "#" + lng);
        mEditor.commit();

        Log.d("获取手机经纬度", geo);
        Log.d("lat", lat);
        Log.d("lng", lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        String strText = String.format("provider is %s, status = %d", provider, status);
        Log.i(TAG, strText);

        LocationData = strText;
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(TAG, provider);

        LocationData = "Enabled:" + provider;
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(TAG, provider);

        LocationData = "Disabled:" + provider;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(in);
//        unbindService(conn);
//        MyService.client.disconnect();
    }
}

