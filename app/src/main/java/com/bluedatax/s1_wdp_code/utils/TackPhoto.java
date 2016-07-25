package com.bluedatax.s1_wdp_code.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;

/**
 * Created by xuyuanqiang on 5/17/16.
 */
public class TackPhoto {

    //缩放图片
    public static int reckonThumbnail(int oldWidth,int oldHeight,int newWidth,int newHeight){
        if((oldHeight>newHeight && oldWidth>newHeight) || (oldHeight<=newHeight && oldWidth>newWidth)){
            int be = (int) (oldWidth/(float)newWidth);
            if(be<=1){
                be=1;
                return be;

            }
        }else if(oldHeight>newHeight && oldWidth<=newWidth){
            int be = (int) (oldHeight/(float)newHeight);
            if(be<=1){
                be = 1;
                return be;

            }

        }
        return 1;

    }

    public static Bitmap PicZoom(Bitmap bmp,int width,int height){
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float)width/bmpWidth,(float)height/bmpHeight);

        return Bitmap.createBitmap(bmp,0, 0, bmpWidth,bmpHeight,matrix,true);
    }


    //读图片
    public static Bitmap getBitmapFromLocal(String tempFile) {
        try {

            File file = new File(tempFile);
            if(file.exists()) {
                return BitmapFactory.decodeFile(tempFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缩小图片，防止内存溢出
     * @param imgPath
     * @return
     */
    public static  Bitmap copressImage(String imgPath){
        Bitmap bmap =null;
        File picture = new File(imgPath);
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth  = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio > 1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if(bmap != null){
            //ivwCouponImage.setImageBitmap(bmap);
            return bmap;
        }
        return null;
    }
}
