
package com.bluedatax.s1_wdp_code.fragment;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.activity.DeviceEvent;
import com.bluedatax.s1_wdp_code.activity.DeviceMessage;
import com.bluedatax.s1_wdp_code.activity.LoginActivity;
import com.bluedatax.s1_wdp_code.service.MyService;
import com.bluedatax.s1_wdp_code.utils.HttpUtils;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.utils.TransparentBitmap;
import com.bluedatax.s1_wdp_code.utils.getTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuyuanqiang on 5/7/16.
 */
public class FragmentDevice extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ListView lvRadar;
    private SharedPreferences mSharedPreference;
    private final String ACTION_NAME = "发送广播";
    private String latitude;
    private String lnglatitude;
    private String currentTime;
    private ArrayList<String> listImg;
    private ArrayList<Object> listConn;
    private DeviceReceive deviceReceive;
    private Bitmap mDownloadImageOne;
    private boolean _isExe = false;
    private downloadImageTask task;
    private SwipeRefreshLayout mSwipeLayout;
    private String device;
    private String fub;
    private String auid;
    private ImageView deviceOne;
    private ImageView deviceTwo;
    private ImageView deviceThree;
    private ImageView deviceFour;
    private ImageView deviceFive;
    private ImageView deviceSix;
    private ImageView deviceSeven;
    private ImageView deviceEight;
    private ImageView deviceNine;
    private ImageView deviceTen;
    private ImageView deviceEleven;
    private ImageView deviceTwelve;
    private ImageView deviceThirteen;
    private ImageView deviceFourteen;
    private JSONArray devices;
    private MyAdapter mAdapter;
    private ArrayList<Object> listGdid;
    private ArrayList<Object> listOnlineTime;
    private int imageId ;
    private int icon = 0;
    private String pathOne;
    private String pathTwo;
    private String pathThree;
    private static final int REFRESH_COMPLETE = 0X110;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        lvRadar = (ListView) view.findViewById(R.id.lv_radar);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        currentTime = getTime.getCurrentTime();
        mSharedPreference = getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        fub = SharedPrefsUtil.getValue(getActivity().getApplicationContext(), "fub", "");
        auid = SharedPrefsUtil.getValue(getActivity().getApplicationContext(), "auid", "");
        pathOne = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage1.jpg";
        pathTwo = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage2.jpg";
        pathThree = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage3.jpg";
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceMsgQuery();
        deviceReceive = new DeviceReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        getActivity().registerReceiver(deviceReceive, intentFilter);
        mAdapter = new MyAdapter(getActivity().getApplicationContext());
        lvRadar.setAdapter(mAdapter);
    }

    private void showDevice() {
        System.out.println(devices.length());
        if (devices.length() == 1) {
            String pathOne = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage1.jpg";

            Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathOne), 50);

            deviceOne.setImageBitmap(devTwoBitmap);
            deviceOne.setVisibility(View.VISIBLE);
        } else if (devices.length() == 2) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
        } else if (devices.length() == 3) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            if (listConn.get(0) == 0) {
                deviceOne.setAlpha(0);
            } else if (listConn.get(1) == 0) {
                deviceTwo.setAlpha(0);
            } else if (listConn.get(2) == 0) {
                deviceThree.setAlpha(0);
            }
        } else if (devices.length() == 4) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
        } else if (devices.length() == 5) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
        } else if (devices.length() == 6) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
        } else if (devices.length() == 7) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
        } else if (devices.length() == 8) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
        } else if (devices.length() == 9) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
        } else if (devices.length() == 10) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
            deviceTen.setVisibility(View.VISIBLE);
        } else if (devices.length() == 11) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
            deviceTen.setVisibility(View.VISIBLE);
            deviceEleven.setVisibility(View.VISIBLE);
        } else if (devices.length() == 12) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
            deviceTen.setVisibility(View.VISIBLE);
            deviceEleven.setVisibility(View.VISIBLE);
            deviceTwelve.setVisibility(View.VISIBLE);
        } else if (devices.length() == 13) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
            deviceTen.setVisibility(View.VISIBLE);
            deviceEleven.setVisibility(View.VISIBLE);
            deviceTwelve.setVisibility(View.VISIBLE);
            deviceThirteen.setVisibility(View.VISIBLE);
        } else if (devices.length() == 14) {
            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            deviceFour.setVisibility(View.VISIBLE);
            deviceFive.setVisibility(View.VISIBLE);
            deviceSix.setVisibility(View.VISIBLE);
            deviceSeven.setVisibility(View.VISIBLE);
            deviceEight.setVisibility(View.VISIBLE);
            deviceNine.setVisibility(View.VISIBLE);
            deviceTen.setVisibility(View.VISIBLE);
            deviceEleven.setVisibility(View.VISIBLE);
            deviceTwelve.setVisibility(View.VISIBLE);
            deviceThirteen.setVisibility(View.VISIBLE);
            deviceFourteen.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(deviceReceive);
    }



    /**
     * 下拉刷新实现的方法
     */

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 5000);
    }


    private class DeviceReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String deviceMsg = intent.getStringExtra("deviceMsg");
            parseDeviceMsg(deviceMsg);
            mAdapter = new MyAdapter(getActivity().getApplicationContext());
            lvRadar.setAdapter(mAdapter);
//            dowmloadImage();
        }
    }


    /**
     * 解析设备信息查询
     *
     * @param deviceMessage
     */

    private void parseDeviceMsg(String deviceMessage) {
        listImg = new ArrayList<>();
        listConn = new ArrayList<>();
        listGdid = new ArrayList<>();
        listOnlineTime = new ArrayList<>();
        try {
            JSONObject deviceMsgJsonObject = new JSONObject(deviceMessage);
            long acceentJsonObjectToken = deviceMsgJsonObject.getLong("token");
            Log.d("返回的token数据", acceentJsonObjectToken + "");
            JSONObject deviceMsgBody = deviceMsgJsonObject.getJSONObject("body");
            JSONObject auth_name = deviceMsgBody.getJSONObject("auth_name");
            device = auth_name.getString("device");
            Log.d("device", device);
            String auth_id = deviceMsgBody.getString("auth_id");
            Log.d("auth_id", auth_id);
            devices = deviceMsgBody.getJSONArray("device");
            Log.d("device数组", devices + "");

            for (int i = 0; i < devices.length(); i++) {
                System.out.println("设备的数量" + devices.length());
                JSONObject devicesJson = devices.getJSONObject(i);

                String cid = devicesJson.getString("cid");
                Log.d("cid", cid);

                int conn_status = devicesJson.getInt("conn_status");
                Log.d("conn_status", conn_status + "");
                listConn.add(conn_status);

                String gdid = devicesJson.getString("gdid");
                Log.d("gdid", gdid);
                listGdid.add(gdid);

//                String name = devicesJson.getString("name");
//                Log.d("name", name);

                String oage = devicesJson.getString("oage");
                Log.d("oage", oage);

                String online_ldt = devicesJson.getString("online_ldt");
                Log.d("online_ldt", online_ldt);
                listOnlineTime.add(online_ldt);

                String profile_img = devicesJson.getString("profile_img");
                Log.d("profile_img", profile_img);
                listImg.add(profile_img);

                String srv_status = devicesJson.getString("srv_status");
                Log.d("srv_status", srv_status);

//                String upn = devicesJson.getString("upn");
//                Log.d("upn", upn);

                String id = devicesJson.getString("id");
                Log.d("id", id);

                System.out.println("----------------------");
            }
        } catch (Exception e) {

        }
    }

    /**
     * 设备信息查询
     */

    private void deviceMsgQuery() {

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s", "");
            jsonRang.put("dt_t", "");
            JSONObject jsonObj = new JSONObject(jsonRang.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", 31);
            jsonBody.put("rang", jsonObj);
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 120);
            jsonReq.put("token", LoginActivity.loginToken);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }

    /**
     * 下载图片
     */

    private void dowmloadImage() {
        if (!_isExe) {
            for (int i = 0; i < 3; i++) {
                task = new downloadImageTask();
                task.execute(String.format("%s?prod=w65&id=1&fttype=dl_files&auth_user=w65_wdas&" +
                        "auth_name=%s&fname=%s&auth_id=%s&type=png&bdx=prod", fub, device, listImg.get(i), auid));
                System.out.println("下载头像的地址"+String.format("%s?prod=w65&id=1&fttype=dl_files&auth_user=w65_wdas&" +
                        "auth_name=%s&fname=%s&auth_id=%s&type=png&bdx=prod", fub, device, listImg.get(i), auid));
            }
            _isExe = true;
        }
    }

    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            mDownloadImageOne = HttpUtils.getNetWorkBitmap(params[0]);
            try {
                saveLocalImage(mDownloadImageOne);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            imageId++;
            System.out.println(imageId+"-----------------");
            if (imageId == 1) {
                if (listConn.get(0) == 1) {
                    System.out.println("get(0)*************" + listConn.get(0));
                    deviceOne.setImageBitmap(mDownloadImageOne);
                } else {
//                    System.out.println("判断是否在线");
//                    BitmapDrawable bd = new BitmapDrawable(getResources(), mDownloadImageOne);
//                    bd.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
//                    System.out.println("设置drawable透明度");
//                    deviceOne.setAlpha(100);
                    String pathOne = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage1.jpg";

                    Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathOne), 50);

                    deviceOne.setImageBitmap(devTwoBitmap);
                }
            } else if (imageId == 2) {
//                if (listConn.get(1) == 1) {
                    deviceTwo.setImageBitmap(mDownloadImageOne);
//                } else {
//                    deviceTwo.setAlpha(100);
//                    deviceTwo.setImageBitmap(mDownloadImageOne);
////                    String pathTwo = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage2.jpg";
////
////                    Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathTwo), 50);
////
////                    deviceTwo.setImageBitmap(devTwoBitmap);
//                }
            } else if (imageId == 3) {
//                if (listConn.get(2) == 1) {
                    deviceThree.setImageBitmap(mDownloadImageOne);
//                } else {
//                    deviceThree.setAlpha(10);
//                    deviceThree.setImageBitmap(mDownloadImageOne);
////                    String pathThree = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage3.jpg";
////
////                    Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathThree), 50);
////
////                    deviceThree.setImageBitmap(devTwoBitmap);
//                }
                System.out.println("result = " + result);
                super.onPostExecute(result);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

    /**
     * 取出图片
     * @param path
     * @return
     */

    public static Bitmap getLocalImage(String path) {
        Bitmap deviceBitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                deviceBitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
        }
        return deviceBitmap;
    }

    /**
     * 保存下载的头像
     * @param bm
     * @return
     * @throws IOException
     */

    public File saveLocalImage(Bitmap bm) throws IOException {
        String path = Environment.getExternalStorageDirectory().toString() + "/s1/icon_download/";

        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myDownloadFile = new File(path + String.format("downloadImage%d.jpg", ++icon));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myDownloadFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 20, bos);
        bos.flush();
        bos.close();
        return myDownloadFile;
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.listview_radar_item, null);
            deviceOne = (ImageView) convertView.findViewById(R.id.device_one);
            deviceTwo = (ImageView) convertView.findViewById(R.id.device_two);
            deviceThree = (ImageView) convertView.findViewById(R.id.device_three);
            deviceFour = (ImageView) convertView.findViewById(R.id.device_four);
            deviceFive = (ImageView) convertView.findViewById(R.id.device_five);
            deviceSix = (ImageView) convertView.findViewById(R.id.device_six);
            deviceSeven = (ImageView) convertView.findViewById(R.id.device_seven);
            deviceEight = (ImageView) convertView.findViewById(R.id.device_eight);
            deviceNine = (ImageView) convertView.findViewById(R.id.device_nine);
            deviceTen = (ImageView) convertView.findViewById(R.id.device_ten);
            deviceEleven = (ImageView) convertView.findViewById(R.id.device_eleven);
            deviceTwelve = (ImageView) convertView.findViewById(R.id.device_twelve);
            deviceThirteen = (ImageView) convertView.findViewById(R.id.device_thriteen);
            deviceFourteen = (ImageView) convertView.findViewById(R.id.device_fourteen);
//            showDevice();

            deviceOne.setVisibility(View.VISIBLE);
            deviceTwo.setVisibility(View.VISIBLE);
            deviceThree.setVisibility(View.VISIBLE);
            String pathOne = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage1.jpg";
            String pathTwo = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage2.jpg";
            String pathThree = Environment.getExternalStorageDirectory() + "/s1/icon_download/" + "downloadImage3.jpg";
            Bitmap devOneBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathOne), 50);
            Bitmap devTwoBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathTwo), 50);
            Bitmap devThreeBitmap = TransparentBitmap.getTransparentBitmap(getLocalImage(pathThree), 50);
            deviceOne.setImageBitmap(devOneBitmap);
            deviceTwo.setImageBitmap(devTwoBitmap);
            deviceThree.setImageBitmap(devThreeBitmap);

            deviceOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DeviceMessage.class);
                    intent.putExtra("id","deviceOne");
                    intent.putExtra("oneStatus",listConn.get(0)+"");
                    intent.putExtra("oneGdid",listGdid.get(0)+"");
                    intent.putExtra("oneTime",listOnlineTime.get(0)+"");
                    startActivity(intent);
                }
            });
            deviceTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DeviceMessage.class);
                    intent.putExtra("id","deviceTwo");
                    intent.putExtra("twoStatus",listConn.get(1)+"");
                    intent.putExtra("twoGdid",listGdid.get(1)+"");
                    intent.putExtra("twoTime",listOnlineTime.get(1)+"");
                    startActivity(intent);
                }
            });

            deviceThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DeviceMessage.class);
                    intent.putExtra("id","deviceThree");
                    intent.putExtra("threeStatus",listConn.get(2)+"");
                    intent.putExtra("threeGdid",listGdid.get(2)+"");
                    intent.putExtra("threeTime",listOnlineTime.get(2)+"");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}