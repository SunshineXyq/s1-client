package com.bluedatax.s1_wdp_code.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xuyuanqiang on 5/11/16.
 */
public class HttpUtils {
    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setRequestProperty("ServerProvider", "BDX");
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.connect();

            InputStream is = urlConn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return bitmap;
    }
}
