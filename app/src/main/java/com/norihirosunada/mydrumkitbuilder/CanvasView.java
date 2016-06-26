package com.norihirosunada.mydrumkitbuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by norihirosunada on 16/05/05.
 */
public class CanvasView extends View {
    Paint paint = new Paint();
    String viewflg;
    float x=0,y=0,width=0,height=0,xc,yc;

    public CanvasView(Context context){
        super(context);
    }

    public void clear(String flg){
        viewflg = flg;
        invalidate();
    }

    public void setPoints(float dx, float dy, float swidth, float sheight){
        x = dx;
        y = dy;
        width = swidth;
        height = sheight;
    }

    @Override
    protected void onDraw(Canvas canvas){
        xc = canvas.getWidth();
        yc = canvas.getHeight();

        canvas.translate(x,y);

        paint.setColor(Color.argb(255,68,125,125));
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawCircle(0,0,width,paint);
        canvas.translate(-x,-y);
    }
}
