package com.norihirosunada.mydrumsetbuilder;

import android.graphics.Paint;

/**
 * Created by norihirosunada on 16/06/28.
 */
public class DrumParts {

    String instruments;
    float cx=0;
    float cy=0;
    float radius=0;
    String paint;
    boolean view;


    public DrumParts(float x, float y, float radius){
        this.cx = x;
        this.cy = y;
        this.radius = radius;
    }

    public void setPosition(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
    }

    public String paint(){
        return paint;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }
}
