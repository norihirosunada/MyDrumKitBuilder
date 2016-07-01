package com.norihirosunada.mydrumsetbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by norihirosunada on 16/05/05.
 */
public class CanvasView extends View {

    Paint drumPaint = new Paint();
    Paint stroke = new Paint();

    ArrayList<DrumParts> drums;
    private int selectDrumId = -1;
    private float lastTouchX, lastTouchY;
    GestureDetector gestureDetector;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionViewClass);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        gestureDetector = new GestureDetector(context,onGestureListener);

    }

    public void init(){
        drums = new ArrayList<>();
    }

    public void addDrum(float cx, float cy, float radius, String instid){
        drums.add(new DrumParts(cx, cy, radius, instid));
    }

    public void addDrum(float cx, float cy, float width, float height, String instid){
        drums.add(new DrumParts(cx, cy, width, height, instid));
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.WHITE);

        stroke.setStrokeWidth(10);
        stroke.setAntiAlias(true);
        stroke.setStyle(Paint.Style.STROKE);

        drumPaint.setAntiAlias(true);
        drumPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < drums.size(); i++) {
            if (i == selectDrumId) {
                stroke.setColor(Color.RED);
            } else {
                stroke.setColor(Color.BLACK);
            }
            String instId = drums.get(i).instid;
            if(instId == "drum"){
                drumPaint.setColor(Color.WHITE);
                canvas.drawCircle(drums.get(i).cx, drums.get(i).cy, drums.get(i).radius, drumPaint);
                canvas.drawCircle(drums.get(i).cx, drums.get(i).cy, drums.get(i).radius, stroke);
            }else if(instId == "cymbal"){
                drumPaint.setColor(Color.YELLOW);
                canvas.drawCircle(drums.get(i).cx, drums.get(i).cy, drums.get(i).radius, drumPaint);
                canvas.drawCircle(drums.get(i).cx, drums.get(i).cy, drums.get(i).radius, stroke);
            }else if(instId == "bass"){
                drumPaint.setColor(Color.GRAY);
                canvas.drawRect(drums.get(i).cx - drums.get(i).width,
                        drums.get(i).cy - drums.get(i).depth,
                        drums.get(i).cx + drums.get(i).width,
                        drums.get(i).cy + drums.get(i).depth,
                        drumPaint);
                canvas.drawRect(drums.get(i).cx-drums.get(i).width,
                        drums.get(i).cy-drums.get(i).depth,
                        drums.get(i).cx+drums.get(i).width,
                        drums.get(i).cy+drums.get(i).depth,
                        stroke);
            }

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        float touchX = event.getX(), touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("CanvasView", "Touch(touchX:" + touchX + ", cy:" + touchY + ")");
                Log.d("CanvasView", "ACTION_DOWN");
                for (int i = 0; i < drums.size(); i++) {
                    if (isTouch(drums.get(i), touchX, touchY)) {
                        selectDrumId = i;
//                        Toast.makeText(getContext(), "タッチしました", Toast.LENGTH_SHORT).show();
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

    public final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public void onLongPress(MotionEvent event){
            Log.d("CanvasView", "ACTION_LONGPRESS");
//            onSaveJson();
//            Toast.makeText(getContext(), "Saved Data", Toast.LENGTH_SHORT).show();
            super.onLongPress(event);
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            float touchX = event2.getX(), touchY = event2.getY();
            Log.d("CanvasView", "Fling(flingX:" + touchX + ", flingy:" + touchY + ")");
            if(velocityY > 10000 && selectDrumId != -1) {
                drums.remove(selectDrumId);
                invalidate();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }

            Log.d("CanvasView", "ACTION_FLING"+velocityX+" "+velocityY);
            return false;
        }

    };

    boolean isTouch(DrumParts drumParts, float touchX, float touchY) {
        boolean bool;
        Log.d("CanvasView", "DrumPart(cx:" + drumParts.cx + ", cy:" + drumParts.cy + ")");
        if (drumParts.instid.equals("drum") || drumParts.instid.equals("cymbal")){
            double distance = Math.sqrt(Math.pow(drumParts.cx - touchX, 2) + Math.pow(drumParts.cy - touchY, 2));
            Log.d("CanvasView", "Distance:" + distance + ", Radius:" + drumParts.radius);
            bool =  distance < drumParts.radius;
        }else if(drumParts.instid.equals("bass")){
            double distanceWidth = Math.abs(drumParts.cx-touchX);
            double distanceHeight = Math.abs(drumParts.cy-touchY);
            bool = distanceWidth < drumParts.width && distanceHeight < drumParts.depth;
        }else{
            bool = false;
        }
        return bool;
    }

    public void onSaveJson(SharedPreferences pref) {
//        File file = new File(Environment.getExternalStorageDirectory() + "MyDrumSetBuilder/Settings/");
//        setSharedPreferencesJSONArray(this.getContext(), file.toString(), Context.MODE_PRIVATE, "Setting1", drums);

        Gson gson = new Gson();
//        gson.toJson(drums);
        pref.edit().putString("MySetting",gson.toJson(drums)).commit();

        Log.d("debugJson",gson.toJson(drums));


//        pref.edit().putInt("size",drums.size()).commit();
//        for(int i=0; i<drums.size(); i++){
//            pref.edit().putString("drum"+i,gson.toJson(drums.get(i))).commit();
//        }

    }

    public void onLoadJson(SharedPreferences pref){
//        File file = new File(Environment.getExternalStorageDirectory() + "MyDrumSetBuilder/Settings/");
//        if(drums != null)
//            drums=getSharedPreferencesStringList(this.getContext(),file.toString(), Context.MODE_PRIVATE, "Setting1", null);

        Gson gson = new Gson();
//        drums = gson.fromJson(pref.getString("MySetting",""),new TypeToken<List<DrumParts>>(){}.getType());
//        drums.addAll(gson.fromJson(pref.getString("MySetting",""),DrumParts[].class));

//        int size = pref.getInt("size",0);
//        for(int i=0; i<size; i++){
//            drums.set(i,gson.fromJson(pref.getString("drums"+i,""),DrumParts.class));
//        }

        String getJson = pref.getString("MySetting", "");
        drums.clear();
        drums = gson.fromJson(getJson, new TypeToken<List<DrumParts>>(){}.getType());
        Log.d("debugJson", pref.getString("MySetting", ""));

        for(int i=0; i<drums.size(); i++){
            Log.d("debugJson","cx:"+drums.get(i).cx);
            Log.d("debugJson",drums.get(i).instid);
        }
        Log.d("debugJson", "" + drums.size());

        invalidate();
    }

}
