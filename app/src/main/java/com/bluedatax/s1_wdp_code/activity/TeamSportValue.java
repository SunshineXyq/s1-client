package com.bluedatax.s1_wdp_code.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bluedatax.s1_wdp_code.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class TeamSportValue extends ActionBarActivity {

    private ListView lvDevice;
    private ImageButton ibReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_sport_value);
        lvDevice = (ListView) findViewById(R.id.lv_device);
        ibReturn = (ImageButton) findViewById(R.id.ib_return);
        DeviceAdapter deviceAdapter = new DeviceAdapter(this);
        lvDevice.setAdapter(deviceAdapter);
        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeamSportValue.this,GroupRank.class);
                startActivity(intent);
            }
        });
        ibReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<String> getDate(){

        ArrayList<String> listItem = new ArrayList<String>();

        for(int i=0;i<10;i++)
        {
            listItem.add("设备名为空");
        }
        return listItem;

    }
    private class DeviceAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        public DeviceAdapter (Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return getDate().size();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_rank_item,null);
                holder = new ViewHolder();
                holder.tvDevice = (TextView) convertView.findViewById(R.id.tv_device_name);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvDevice.setText(getDate().get(position));
            return convertView;
        }
        private class ViewHolder {
            private TextView tvDevice;
        }
    }
}