package com.norihirosunada.mydrumsetbuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by norihirosunada on 16/05/05.
 */
public class CanvasView extends View {

    Paint drumPaint = new Paint();
    Paint cymbalPaint = new Paint();
    String viewflg;

    List<DrumParts> drums;
    private int selectDrumId = -1;
    private float lastTouchX, lastTouchY;


    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionViewClass);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();


    }

    public void clear(){
//        viewflg = flg;


        invalidate();
    }

    public void init(){
        drums = new ArrayList<>();
    }

    public void addDrum(float cx, float cy, float radius){
        drums.add(new DrumParts(cx, cy, radius));
    }

    public void setRadius(int id, float radius){
        drums.get(id).radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas){

        drumPaint.setStrokeWidth(30);
        drumPaint.setAntiAlias(true);
        drumPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < drums.size(); i++) {
            if (i == selectDrumId) {
                drumPaint.setColor(Color.YELLOW);
            } else {
                drumPaint.setColor(Color.argb(255, 68, 125, 125));
            }
            canvas.drawCircle(drums.get(i).cx, drums.get(i).cy, drums.get(i).radius, drumPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX(), touchY = event.getY();
        Log.d("CanvasView", "Touch(touchX:" + touchX + ", cy:" + touchY + ")");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("CanvasView", "ACTION_DOWN");
                for (int i = 0; i < drums.size(); i++) {
                    if (isTouch(drums.get(i), touchX, touchY)) {
                        selectDrumId = i;
                        Toast.makeText(getContext(), "タッチしました", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("CanvasView", "ACTION_MOVE");
                if (selectDrumId != -1) {
                    drums.get(selectDrumId).setPosition(
                            drums.get(selectDrumId).cx + (touchX - lastTouchX),
                            drums.get(selectDrumId).cy + (touchY - lastTouchY)
                    );
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("CanvasView", "ACTION UP OR CANCEL");
                selectDrumId = -1;
                break;
        }
        lastTouchX = touchX;
        lastTouchY = touchY;
        invalidate();
        return true;
    }

    boolean isTouch(DrumParts drumParts, float touchX, float touchY) {
        Log.d("CanvasView", "DrumPart(cx:" + drumParts.cx + ", cy:" + drumParts.cy + ")");
        double distance = Math.sqrt(Math.pow(drumParts.cx - touchX, 2) + Math.pow(drumParts.cy - touchY, 2));
        Log.d("CanvasView", "Distance:" + distance + ", Radius:" + drumParts.radius);
        return distance < drumParts.radius;
    }

}
