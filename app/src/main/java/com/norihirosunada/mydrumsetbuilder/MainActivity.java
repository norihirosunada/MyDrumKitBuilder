package com.norihirosunada.mydrumsetbuilder;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import org.rajawali3d.surface.RajawaliSurfaceView;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    enum ButtonState{OPEN,CLOSE}
    ButtonState addObj;

    static String TAG = "MainActivity";

    CanvasView canvasView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        Display display = this.getWindowManager().getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        addObj = ButtonState.CLOSE;
        final FloatingActionButton addCymbal = (FloatingActionButton)findViewById(R.id.fab_cymbal);
        final FloatingActionButton addDrum = (FloatingActionButton)findViewById(R.id.fab_drum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action
                if (addObj == ButtonState.CLOSE) {
                    addCymbal.show();
                    addDrum.show();
                    addObj = ButtonState.OPEN;

                } else {
                    addCymbal.hide();
                    addDrum.hide();
                    addObj = ButtonState.CLOSE;

                }
            }
        });

        addCymbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action

            }
        });

        addDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action

            }
        });

        //DrawerToggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // NavigationView Listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        canvasView = (CanvasView)findViewById(R.id.canvas_view);
        canvasView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
//                    Random random = new Random();
//                    canvasView.addDrum(random.nextInt(canvasView.getWidth()), random.nextInt(canvasView.getHeight()), 50);
                    canvasView.addDrum(point.x/2,point.y/2,100);
                }
                canvasView.invalidate();
            }
        });

    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                downY = event.getY();
////                renderer.getObjectAt(downX, downY, event);
//                canvasView.setPoints(downX, downY, 100, 100);
//                canvasView.addInstrument(id,downX,downY,20);
//                canvasView.clear();
//                Log.d("onTouched", "ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
////                renderer.getObjectAt(event.getX(), event.getY(), event);
//                canvasView.setPoints(event.getX(),event.getY(),100,100);
//                float radius = getDistance(downX,event.getX(),downY,event.getY());
//                canvasView.setRadius(id,radius);
//                canvasView.clear();
//                Log.d("onTouched", "ACTION_MOVE");
//                break;
//            default:
//                break;
//        }
//        return true;
////        return super.onTouchEvent(event);
////    }
//
//    public float getDistance(float x1, float x2, float y1, float y2){
//        return (float)Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point point = new Point();

        Display display = this.getWindowManager().getDefaultDisplay();
        display.getSize(point);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_edit:
                Log.d(TAG, "Item 1 Selected!");
                break;
            case R.id.nav_gallery:
                Log.d(TAG, "Item 2 Selected!");
                break;
            case R.id.nav_manage:
                Log.d(TAG, "Item 3 Selected!");
                break;
            case R.id.nav_share:
                Log.d(TAG, "Item 4 Selected!");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
