package com.bluedatax.s1_wdp_code.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.utils.TackPhoto;

public class SetHand extends ActionBarActivity {

    private RelativeLayout mHead;
    private ImageView ivHeadPic;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //更新UI
            Bundle data = msg.getData();
            String path = data.getString("path");
            Bitmap copressImage = TackPhoto.copressImage(path);
            ivHeadPic.setImageBitmap(copressImage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hand);
        mHead = (RelativeLayout) findViewById(R.id.rlMineinfoHead);
        ivHeadPic = (ImageView) findViewById(R.id.ivHeadPic);
        mHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetHand.this,ChooseHead.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowPhoto();
    }

    private void isShowPhoto(){
        String photoPath = SharedPrefsUtil.getValue(getApplicationContext(), "photoPath", "");
        if(!photoPath.equals("")){
            Message message = new Message();
            Bundle bundle= new Bundle();
            bundle.putString("path", photoPath);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
