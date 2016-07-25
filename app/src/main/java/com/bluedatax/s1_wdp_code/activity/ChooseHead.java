package com.bluedatax.s1_wdp_code.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bluedatax.s1_wdp_code.R;
import com.bluedatax.s1_wdp_code.utils.SharedPrefsUtil;
import com.bluedatax.s1_wdp_code.utils.TackPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ChooseHead extends ActionBarActivity implements View.OnClickListener{

    private RelativeLayout takePhoto;
    private RelativeLayout choosePhoto;
    private ImageView ivSetHead;
    private ImageButton ibReturn;
    private static String imagePath = null;


    Handler handler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            //更新UI
            Bundle data = msg.getData();
            String path = data.getString("path");
            Bitmap copressImage = TackPhoto.copressImage(path);
            ivSetHead.setImageBitmap(copressImage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_head);
        initView();
        isShowPhoto();
    }

    private void initView() {
        takePhoto = (RelativeLayout) findViewById(R.id.take_photo);
        choosePhoto = (RelativeLayout) findViewById(R.id.choose_photo);
        ivSetHead = (ImageView) findViewById(R.id.ivSetHead);
        ibReturn = (ImageButton) findViewById(R.id.ibReturn);
        ibReturn.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"S1card.jpg"));
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(i, 100);
                break;
            case R.id.choose_photo:
                Intent iPhoto = new Intent(Intent.ACTION_PICK);
                iPhoto.setType("image/*");//相片类型
                startActivityForResult(iPhoto, 101);
                break;
            case R.id.ibReturn:
                finish();
                break;
        }
    }


    // 接收从相册 相机选择的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && null != data) {
            Uri imageUri = data.getData();
            //获取图片的路径
            String photoPath = getPhotoPath(imageUri);
            imagePath = photoPath;
            Message message = new Message();
            Bundle bundle= new Bundle();
            bundle.putString("path", photoPath);
            message.setData(bundle);
            handler.sendMessage(message);
            SharedPrefsUtil.putValue(getApplicationContext(), "photoPath", photoPath);

        } else if (requestCode == 100 ) {
            //相机
            if (resultCode == RESULT_OK){
                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/S1card.jpg");
                if(bitmap!=null){
                    //按比例缩放
                    int scale = TackPhoto.reckonThumbnail(bitmap.getWidth(),bitmap.getHeight(),200,300);
                    Bitmap picZoom = TackPhoto.PicZoom(bitmap, bitmap.getWidth()/scale, bitmap.getHeight()/scale );
                    bitmap.recycle();//释放内存
                    String photoPath = Environment.getExternalStorageDirectory()+"/card.jpg";
                    imagePath = photoPath;
                    Message message = new Message();
                    Bundle bundle= new Bundle();
                    bundle.putString("path", photoPath);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    SharedPrefsUtil.putValue(getApplicationContext(), "photoPath",photoPath);
                }
            }
        }
    }

    private String getPhotoPath(Uri imageUri) {
        ContentResolver resolver = getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(resolver, imageUri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] path = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(imageUri, path, null, null, null);
        //获取索引值
        int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //光标移到开头
        cursor.moveToFirst();
        //根据索引获取到图片的路径
        String photoPath = cursor.getString(index);
//			headImag.setImageBitmap(bitmap);
        return photoPath;
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
