package com.bluedatax.s1_wdp_code.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.activity.LoginActivity;
import com.bluedatax.s1_wdp_code.activity.TeamSportValue;
import com.bluedatax.s1_wdp_code.service.MyService;
import com.bluedatax.s1_wdp_code.utils.getTime;

import org.json.JSONObject;

/**
 * Created by xuyuanqiang on 5/7/16.
 */
public class FragmentRank extends Fragment implements View.OnClickListener{

    private TextView tvTeam;
    private String currentTime;
    private SharedPreferences mSharedPreference;
    private String latitude;
    private String lnglatitude;
    private final String ACTION_NAME = "发送广播";

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank,null);
        tvTeam = (TextView) view.findViewById(R.id.tv_team_one);
        tvTeam.setOnClickListener(this);
        currentTime = getTime.getCurrentTime();
        mSharedPreference = getActivity().getSharedPreferences("count", Context.MODE_PRIVATE);
        latitude = mSharedPreference.getString("lat", "");
        lnglatitude = mSharedPreference.getString("lng", "");
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_team_one:
                Intent intent = new Intent(getActivity(), TeamSportValue.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DeviceData deviceData = new DeviceData();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        getActivity().registerReceiver(deviceData,intentFilter);
        queryDeviceData();
    }

    public class DeviceData extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String deviceData = intent.getStringExtra("deviceData");
        }
    }

    private void queryDeviceData() {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", latitude);
            jsonGeo.put("lng", lnglatitude);
            Log.d("json经纬度", jsonGeo.toString());
            JSONObject jsonObject = new JSONObject(jsonGeo.toString());

            JSONObject jsonRang = new JSONObject();
            jsonRang.put("dt_s","");
            jsonRang.put("dt_t","");
            JSONObject jsonObj = new JSONObject(jsonRang.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", 31);
            jsonBody.put("rang", jsonObj);
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonObject);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 121);
            jsonReq.put("token", LoginActivity.loginToken);
            jsonReq.put("body", jsonBody);

            MyService.client.send(jsonReq.toString());

        } catch (Exception e) {

        }
    }
}
