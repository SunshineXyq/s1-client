package com.bluedatax.s1_wdp_code.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.activity.SetHand;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.utils.TackPhoto;

/**
 * Created by xuyuanqiang on 5/7/16.
 */
public class FragmentSet extends Fragment implements View.OnClickListener{

    private ImageView userHand;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //更新UI
            Bundle data = msg.getData();
            String path = data.getString("path");
            Bitmap copressImage = TackPhoto.copressImage(path);
            userHand.setImageBitmap(copressImage);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set,null);
        userHand = (ImageView) view.findViewById(R.id.user_head);
        userHand.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head:
                Intent intent = new Intent(getActivity(),SetHand.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowPhoto();
    }

    private void isShowPhoto(){
        String photoPath = SharedPrefsUtil.getValue(getActivity(),"photoPath", "");
        if(!photoPath.equals("")){
            Message message = new Message();
            Bundle bundle= new Bundle();
            bundle.putString("path", photoPath);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
