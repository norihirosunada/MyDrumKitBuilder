package com.norihirosunada.mydrumsetbuilder;

import android.graphics.Paint;

/**
 * Created by norihirosunada on 16/06/28.
 */
public class drumParts {

    String instruments;
    float cx=0;
    float cy=0;
    float radius=0;
    String paint;
    boolean view;

    public drumParts(float x, float y, int i){
        this.cx = x;
        this.cy = y;
    }

    public String paint(){
        return paint;
    }

    public void setCX(int x){
        this.cx = x;
    }

    public void setCY(int y){
        this.cy = y;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }
}
