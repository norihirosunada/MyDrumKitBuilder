package com.norihirosunada.mydrumsetbuilder;

import android.graphics.Paint;

import java.security.PublicKey;

/**
 * Created by norihirosunada on 16/06/28.
 */
public class DrumParts {

    String instid;
    float cx=0;
    float cy=0;
    float radius=0;
    float width=0,depth=0;

    public DrumParts(){
        super();
    }

    public DrumParts(float x, float y, float radius, String instrument){
        this.cx = x;
        this.cy = y;
        this.radius = radius;
        this.instid = instrument;
    }

    public DrumParts(float x, float y, float width, float depth, String instrument){
        this.cx = x;
        this.cy = y;
        this.width = width;
        this.depth = depth;
        this.instid = instrument;
    }

    public void setPosition(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
    }

    public void setRadius(float radius){
        this.radius = radius;
    }
}
