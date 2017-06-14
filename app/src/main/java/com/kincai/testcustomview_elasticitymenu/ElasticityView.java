package com.kincai.testcustomview_elasticitymenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author KINCAI
 * .
 * description TODO
 * .
 * Time 2017-06-11 20:18
 */

public class ElasticityView extends View{
    private Paint mPaint;
    private Path mPath = new Path();
    private int mCurrentArcHeight;
    private int mMaxArcHeight;

    private final int STATUS_NONE = 0X100;
    private final int STATUS_UP = 0X101;
    private final int STATUS_DOWN = 0X102;
    private int mCurrentStatus = STATUS_NONE;

    public ElasticityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(getContext(),android.R.color.holo_blue_light));
        mMaxArcHeight = getResources().getDimensionPixelSize(R.dimen.elasticity_menu_arc_max_height);
    }

    public void show() {
        mCurrentStatus = STATUS_UP;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMaxArcHeight);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentArcHeight = (int) animation.getAnimatedValue();
                if(mCurrentArcHeight == mMaxArcHeight) {
                    bounce();
                }
                invalidate();
            }
        });

        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentPointY = 0;
        switch (mCurrentStatus) {
            case STATUS_NONE:
                currentPointY = 0;
                break;
            case STATUS_UP:
                currentPointY = (int) (getHeight() * ( 1- (float)mCurrentArcHeight / mMaxArcHeight) + mMaxArcHeight);
                break;
            case STATUS_DOWN:
                currentPointY = mMaxArcHeight;
                break;
        }

        mPath.reset();
        mPath.moveTo(0,currentPointY);//起始点
        mPath.quadTo(getWidth() / 2, currentPointY - mCurrentArcHeight, getWidth(), currentPointY);//拐点和终点
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    private void bounce() {
        mCurrentStatus = STATUS_DOWN;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mMaxArcHeight,0);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentArcHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.start();
    }
}
