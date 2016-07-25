package com.bluedatax.s1_wdp_code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.adapter.AccidentStateAdapter;
import com.bluedatax.s1_wdp_code.utils.AccidentStateData;
import com.bluedatax.s1_wdp_code.utils.pullRefresh.XListView;
import com.bluedatax.s1_wdp_code.view.CircleImageView;

import java.util.ArrayList;
import java.util.Iterator;


public class AccidentState extends ActionBarActivity implements View.OnClickListener,XListView.IXListViewListener{

    private TextView stateStartTime;
    private TextView stateHappenTime;
    private RadioButton mButtonPlayVideo;
    private VideoView mVideoView;
    private CircleImageView mCircleImageAccidentState;
    private LayoutInflater mInflater;
    private ArrayList<String> accidentName;
    private ArrayList<String> accidentTime;
    private ArrayList<AccidentStateData> mDatas;
    private XListView mListView;
    private AccidentStateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_state);
//        String startTime = getIntent().getStringExtra("accident_start_time");
//        String happenTime = getIntent().getStringExtra("accident_happen_time");
//        accidentName = getIntent().getStringArrayListExtra("name");
//
//        Iterator its = accidentName.iterator();
//        while (its.hasNext()) {
//            Object obj = its.next();
//            Log.d("******name", obj + "");
//        }
//        accidentTime = getIntent().getStringArrayListExtra("time");
//        Iterator it = accidentTime.iterator();
//        while (it.hasNext()) {
//            Object obj = it.next();
//            Log.d("******time", obj + "");
//        }
//        stateStartTime = (TextView) findViewById(R.id.state_start_time);
//        stateHappenTime = (TextView) findViewById(R.id.state_happen_time);
//        stateStartTime.setText(startTime);
//        stateHappenTime.setText(happenTime);
        mButtonPlayVideo = (RadioButton) findViewById(R.id.radiobutton_playvideo);
        mButtonPlayVideo.setOnClickListener(this);
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mCircleImageAccidentState = (CircleImageView) findViewById(R.id.circleimage_accident_state);
        mCircleImageAccidentState.setOnClickListener(this);
        mListView = (XListView) findViewById(R.id.listView_accident);
        mInflater =getLayoutInflater();
        initDatas();
        mAdapter =new AccidentStateAdapter(mDatas, mInflater,AccidentState.this);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mDatas =new ArrayList<AccidentStateData>();
//        for (int i = 0; i < accidentTime.size() ; i++) {
//            mDatas.add(new AccidentStateData(R.drawable.accident_circle, accidentTime.get(i) + "", accidentName.get(i) + ""));
//        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circleimage_accident_state:
//                Intent intent=new Intent(AccidentState.this, BdMapApplication.class);
//                startActivity(intent);
                break;
            case R.id.radiobutton_playvideo:
                String path= Environment.getExternalStorageDirectory()+"/DCIM/Camera/video_20150803_164216.mp4";
                mVideoView.setVisibility(View.VISIBLE);
                //找到视频地址，还可以通过setVideoUri来找到
                mVideoView.setVideoPath(path);
//                mVideoView.setMediaController(new MediaController(AccidentState.this));
                mVideoView.start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        mListView.stopLoadMore();
    }
 }

