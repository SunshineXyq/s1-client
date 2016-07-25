package com.bluedatax.s1_wdp_code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by xuyuanqiang on 5/6/16.
 */
public class RadarView extends View {
    private Paint mPaintLine, mPaintCircle;
    private int w, h;
    // 动画
    private Matrix matrix;
    // 旋转角度
    private int start;
    // Handler定时动画
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            start = start + 2;
            matrix = new Matrix();
            // 参数：旋转角度，围绕点坐标的x,y坐标点,旋转矩阵
            matrix.postRotate(start, w / 2, (h - 255) / 2);
            // 刷新重绘
            RadarView.this.invalidate();
            // 继续循环
            handler.postDelayed(run, 1);
        }
    };

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        // 获取高宽
        w = context.getResources().getDisplayMetrics().widthPixels;
        h = context.getResources().getDisplayMetrics().heightPixels;
        // 一致旋转
        handler.post(run);
    }

    private void initView() {
        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStrokeWidth(3);     //画笔的粗细
        mPaintLine.setAntiAlias(true);    //抗锯齿
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.RED);
        mPaintLine.setStrokeWidth(3);
        mPaintCircle.setAntiAlias(true);   //抗锯齿
        mPaintCircle.setFakeBoldText(true);
        matrix = new Matrix();
    }

    /**
     * 测量
     *
     * @author LGL
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置铺满
        setMeasuredDimension(w, h - 255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画四个圆形
        canvas.drawCircle(w / 2, (h - 255) / 2, w / 2, mPaintLine);     //第三圈
        canvas.drawCircle(w / 2, (h - 255) / 2, w / 3, mPaintLine);     //第二圈
        canvas.drawCircle(w / 2, (h - 255) / 2, w * 7 / 10, mPaintLine); //第四圈
        canvas.drawCircle(w / 2, (h - 255) / 2, w / 5, mPaintLine);     //第一圈

        // 绘制渐变圆,起始中心新x，y坐标，起始渲染颜色以及结束时渲染颜色
        Shader mShader = new SweepGradient(w / 2, (h - 255) / 2, Color.TRANSPARENT,
                Color.parseColor("#33FFFFFF"));
        // 绘制时渐变
        mPaintCircle.setShader(mShader);
        // 增加旋转动画，使用矩阵实现
        canvas.concat(matrix); // 前置动画
        canvas.drawCircle(w / 2, (h - 255) / 2, w * 7 / 10, mPaintCircle);
        matrix.reset();      //重置矩阵
    }
}
