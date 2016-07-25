package com.bluedatax.s1_wdp_code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bluedatax.s1_wdp_code.utils.websocket.WebSocketClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xuyuanqiang on 5/10/16.
 */
public class MyService extends Service {
    public OnConnectListener onConnectListener;
    public static WebSocketClient client;
    private final String ACTION_NAME = "发送广播";
    private final String MESSAGE_NOTIFICATION = "消息通知";
    private Intent intent = new Intent(ACTION_NAME);
    private Intent in = new Intent(MESSAGE_NOTIFICATION);
    private String duid;

    /**
     * 注册回调接口的方法，供外部调用
     * @param
     */
    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        duid = intent.getStringExtra("duid");
        startConnect();
        return super.onStartCommand(intent, flags, startId);
    }
    public void startConnect(){
        List<BasicNameValuePair> extraHeaders = Arrays.asList(new BasicNameValuePair("Cookie", "session=abcd"));

        client = new WebSocketClient(URI.create(String.format("ws://192.168.0.9:19000?connect&prod=w65" +
                "&auid=89b5129f7d5f447562b632d724e4c7a0&duid=%s&apc=2", duid)), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
                Log.d("onConnect", "Connected!");
                String notice = "Connected";
                Log.d("判断连接状态", notice);
                if(onConnectListener != null ) {
                    Log.d("发送的通知", notice);
                    onConnectListener.onConnected(notice);
                }
            }
            @Override
            public void onMessage(String message) {
                Log.d("onMessage", String.format("Got string message! %s", message));
                try {
                    Log.d("*******", "1234456");
                    JSONObject parseJson = new JSONObject(message);
                    System.out.println( "---------------" + parseJson.getInt("msg"));
                    if (onConnectListener != null && parseJson.getInt("msg") == 10) {
                        Log.d("发送给UI的json数据:", parseJson + "");
                        onConnectListener.onJSonObject(parseJson);
                    } else if (parseJson.getInt("msg") == 11) {

                    } else if (onConnectListener != null && parseJson.getInt("msg") == 110) {
                        onConnectListener.onJSonObject(parseJson);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 120) {
                        intent.putExtra("deviceMsg",message);
                        Log.d("发送设备信息查询", message);
                        sendBroadcast(intent);
                    } else if (parseJson.getInt("msg") == 121 ) {
                        intent.putExtra("deviceData",message);
                        Log.d("发送设备数据查询", message);
                        sendBroadcast(intent);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 200) {
                        intent.putExtra("pushNotification",message);
                        Log.d("发送消息推送通知", message);
                        sendBroadcast(intent);
                    } else if (onConnectListener != null && parseJson.getInt("msg") == 112) {
                        intent.putExtra("deviceEventState",message);
                        sendBroadcast(intent);
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onMessage(byte[] data) {
                Log.d("onMessage", String.format("Got binary message! %s", new String(data)));

            }
            @Override
            public void onDisconnect(int code, String reason) {
                Log.d("onDisconnect", String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }
            @Override
            public void onError(Exception error) {
                Log.e("onError", "Error!", error);
                Handler handler=new Handler(Looper.getMainLooper());
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getApplicationContext(), "网络连接错误，请重新连接!", Toast.LENGTH_LONG).show();
                    }
                });
                if (onConnectListener != null) {
                    onConnectListener.onError(error);
                }
            }
        }, extraHeaders);

        client.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return new MyBinder();
    }
    public class MyBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public MyService getService() {

            return MyService.this;
        }
    }
}
