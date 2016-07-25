package com.bluedatax.s1_wdp_code.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.fragment.FragmentDevice;
import com.bluedatax.s1_wdp_code.fragment.FragmentMusic;
import com.bluedatax.s1_wdp_code.fragment.FragmentRank;
import com.bluedatax.s1_wdp_code.fragment.FragmentSet;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView lvRadar;
    private Button rbDevice;
    private Button rbRanking;
    private Button rbMusic;
    private Button rbSetting;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentDevice mFragmentDevice;
    private FragmentRank mFragmentRank;
    private final String ACTION_NAME = "发送广播";

    private FragmentMusic mFragmentMusic;
    private FragmentSet mFragmentSet;
    private Drawable homePage;
    private Drawable rank;
    private Drawable music;
    private Drawable set;
    private Drawable homePre;
    private Drawable rankPre;
    private Drawable musicPre;
    private Drawable setPre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        NotificationReceive notificationReceive = new NotificationReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME);
        registerReceiver(notificationReceive, intentFilter);
//        NotificationManager manager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notify = new Notification(R.mipmap.ic_launcher, "您有新的消息，请注意查收！", System.currentTimeMillis());
//        Intent i = new Intent(MainActivity.this,DeviceEvent.class);
//        PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, i,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//        notify.setLatestEventInfo(MainActivity.this,"This is title","This is content",pi);
//        manager.notify(1,notify);
    }

    private class NotificationReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String notification = intent.getStringExtra("pushNotification");
            System.out.println("推送的消息" + notification);
            if (notification != null) {
                try {
                    JSONObject jsonNotification = new JSONObject(notification);
                    String eid = jsonNotification.getString("eid");
                    NotificationManager manager = (NotificationManager)
                            getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify = new Notification(R.mipmap.ic_launcher, "您有新的消息，请注意查收！", System.currentTimeMillis());
                    Intent i = new Intent(MainActivity.this,DeviceEvent.class);
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, i,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    notify.setLatestEventInfo(MainActivity.this,"This is title","This is content",pi);
                    manager.notify(1,notify);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        rbDevice = (Button) findViewById(R.id.rb_device);
        rbRanking = (Button) findViewById(R.id.rb_ranking);
        rbMusic = (Button) findViewById(R.id.rb_music);
        rbSetting = (Button) findViewById(R.id.rb_setting);

        rbDevice.setOnClickListener(this);
        rbRanking.setOnClickListener(this);
        rbMusic.setOnClickListener(this);
        rbSetting.setOnClickListener(this);

        homePage = getResources().getDrawable(R.mipmap.devices);
        rank = getResources().getDrawable(R.mipmap.rank);
        music = getResources().getDrawable(R.mipmap.music);
        set = getResources().getDrawable(R.mipmap.setting);
        homePre = getResources().getDrawable(R.mipmap.devices_press);
        rankPre = getResources().getDrawable(R.mipmap.rank_press);
        musicPre = getResources().getDrawable(R.mipmap.music_press);
        setPre = getResources().getDrawable(R.mipmap.setting_press);
        homePre.setBounds(0,0,50,50);
        rankPre.setBounds(0,0,50,50);
        musicPre.setBounds(0, 0, 50, 50);
        setPre.setBounds(0, 0, 50, 50);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_device:
                setTabSelection(0);
                break;
            case R.id.rb_ranking:
                setTabSelection(1);
                break;
            case R.id.rb_music:
                setTabSelection(2);
                break;
            case R.id.rb_setting:
                setTabSelection(3);
                break;
        }
    }

    private void setTabSelection(int index) {
        // 重置按钮
        resetBtn();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//        hideFragments(transaction);
        switch (index) {
            case 0:
                rbDevice.setCompoundDrawables(null, homePre, null, null);
                mFragmentDevice = new FragmentDevice();
                transaction.replace(R.id.content_layout, mFragmentDevice);
                radioGroup.check(R.id.rb_device);
                break;
            case 1:
                rbRanking.setCompoundDrawables(null, rankPre, null, null);
                mFragmentRank = new FragmentRank();
                transaction.replace(R.id.content_layout, mFragmentRank);
                break;
            case 2:
                rbMusic.setCompoundDrawables(null, musicPre, null, null);
                mFragmentMusic = new FragmentMusic();
                transaction.replace(R.id.content_layout,mFragmentMusic);
                break;
            case 3:
                rbSetting.setCompoundDrawables(null, setPre, null, null);
                mFragmentSet = new FragmentSet();
                transaction.replace(R.id.content_layout, mFragmentSet);
                break;
        }
            transaction.commit();
    }

    private void resetBtn() {
        homePage.setBounds(0,0,50,50);
        rank.setBounds(0,0,50,50);
        music.setBounds(0, 0, 50, 50);
        set.setBounds(0, 0, 50, 50);
        rbDevice.setCompoundDrawables(null, homePage, null, null);
        rbRanking.setCompoundDrawables(null, rank, null, null);
        rbMusic.setCompoundDrawables(null, music, null, null);
        rbSetting.setCompoundDrawables(null, set, null, null);
    }

//    private void hideFragments (FragmentTransaction mTransaction ) {
//        if (mFragmentDevice != null) {
//            mTransaction.hide(mFragmentDevice);
//        }
//    }

}
