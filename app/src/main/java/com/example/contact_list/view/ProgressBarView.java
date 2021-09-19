package com.example.contact_list.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ProgressBarView extends View {
    private final int radius = 50;
    private Paint mPaintBall1;
    private Paint mPaintBall2;
    private Paint mPaintBall3;
    private Paint mPaintBall4;
    private float mCurrentXBall1 = 480;
    private float mCurrentXBall2 = 610;
    private float mCurrentXBall3 = 480;
    private float mCurrentXBall4 = 610;
    private float mCurrentYBall1 = 900;
    private float mCurrentYBall2 = 900;
    private float mCurrentYBall3 = 1020;
    private float mCurrentYBall4 = 1020;
    private int mColorBall1 = Color.GREEN;
    private int mColorBall2 = Color.BLUE;
    private int mColorBall3 = Color.RED;
    private int mColorBall4 = Color.YELLOW;

    public ProgressBarView(Context context) {
        super(context);
        initView(null);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attributeSet) {
        mPaintBall1 = new Paint();
        mPaintBall2 = new Paint();
        mPaintBall3 = new Paint();
        mPaintBall4 = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintBall1.setColor(mColorBall1);
        mPaintBall2.setColor(mColorBall2);
        mPaintBall3.setColor(mColorBall3);
        mPaintBall4.setColor(mColorBall4);
        canvas.drawCircle(mCurrentXBall1, mCurrentYBall1, radius, mPaintBall1);
        canvas.drawCircle(mCurrentXBall2, mCurrentYBall2, radius, mPaintBall2);
        canvas.drawCircle(mCurrentXBall3, mCurrentYBall3, radius, mPaintBall3);
        canvas.drawCircle(mCurrentXBall4, mCurrentYBall4, radius, mPaintBall4);
    }
}



