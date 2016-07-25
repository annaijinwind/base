package com.lieier.base.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by fengjianqi on 2016/7/24.
 */
public class TestView extends TextView{
    private Paint mypaint1;
    private Paint mypaint2;
    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mypaint1=new Paint();
        mypaint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        mypaint1.setStyle(Paint.Style.FILL);
        mypaint2=new Paint();
        mypaint2.setColor(Color.YELLOW);
        mypaint2.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(
                0,
                0,
                getMeasuredWidth(),
                getMeasuredHeight(),
                mypaint1
        );
        canvas.drawRect(
                10,
                10,
                getMeasuredWidth()-10,
                getMeasuredHeight()-10,
                mypaint2
        );
        canvas.save();
        canvas.translate(10,0);
        super.onDraw(canvas);
        canvas.restore();
    }
}
