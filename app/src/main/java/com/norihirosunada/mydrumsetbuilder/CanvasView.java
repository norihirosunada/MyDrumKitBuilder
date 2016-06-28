package com.norihirosunada.mydrumsetbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by norihirosunada on 16/05/05.
 */
public class CanvasView extends View {
    boolean onFirsttime=true;

    Paint drumPaint = new Paint();
    Paint cymbalPaint = new Paint();
    String viewflg;
    float x=0,y=0,width=0,height=0,xc,yc;

    drumParts[] drums;

    public CanvasView(Context context) {
        this(context, null);
        drums = new drumParts[10];
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionViewClass);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();


    }

    public void clear(){
//        viewflg = flg;


        invalidate();
    }

    public void init(){
        for(int i=0; i<drums.length; i++){
            drums[i].setCX(0);
            drums[i].setCY(0);
            drums[i].setRadius(0);
        }
    }

    public void setPoints(float dx, float dy, float swidth, float sheight){
        x = dx;
        y = dy;
        width = swidth;
        height = sheight;
    }

    public void addInstrument(int id, float cx, float cy, float radius){
        drums[id].cx = cx;
        drums[id].cy = cy;
        drums[id].radius = radius;
    }

    public void setRadius(int id, float radius){
        drums[id].radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas){
        xc = canvas.getWidth();
        yc = canvas.getHeight();

        canvas.translate(x, y);

        drumPaint.setColor(Color.argb(255, 68, 125, 125));
        drumPaint.setStrokeWidth(30);
        drumPaint.setAntiAlias(true);
        drumPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        if(onFirsttime) {
            init();
            onFirsttime = false;
        }
        for (int i = 0; i < drums.length - 1; i++) {
            canvas.drawCircle(drums[i].cx, drums[i].cy, drums[i].radius, drumPaint);
        }

        canvas.translate(-x,-y);

    }

}
