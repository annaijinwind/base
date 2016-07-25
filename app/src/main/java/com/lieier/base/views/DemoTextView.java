package com.lieier.base.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by fengjianqi on 2016/7/24.
 */
public class DemoTextView extends TextView{
    private float mViewWidth=0;
    private Paint mypaint1;
    private float mTramlate;
    private LinearGradient mLinearGradient;
    private Matrix mGradientDrawable;
    public DemoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth==0){
            mViewWidth=getMeasuredWidth();
            if (mViewWidth>0){
                mypaint1=getPaint();
                mLinearGradient=new LinearGradient(
                        0,
                        0,
                        mViewWidth,
                        0,
                        new int[]{
                                Color.BLUE,0xfffffff,
                                Color.BLUE},
                        null,
                        Shader.TileMode.CLAMP);
                mypaint1.setShader(mLinearGradient);
                mGradientDrawable=new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientDrawable!=null){
            mTramlate+=mViewWidth/5;
            if (mTramlate>2*mViewWidth){
                mTramlate=-mViewWidth;
            }
            mGradientDrawable.setTranslate(mTramlate,0);
            mLinearGradient.setLocalMatrix(mGradientDrawable);
            postInvalidateDelayed(100);

        }
    }
}
