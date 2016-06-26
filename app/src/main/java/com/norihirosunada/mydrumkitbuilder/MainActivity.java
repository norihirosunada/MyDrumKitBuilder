package com.norihirosunada.mydrumkitbuilder;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import org.rajawali3d.surface.RajawaliSurfaceView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    Renderer renderer;
    public RajawaliSurfaceView rajawaliSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rajawaliSurfaceView = (RajawaliSurfaceView)findViewById(R.id.rajawali_surface);
        rajawaliSurfaceView.setOnTouchListener(this);
        renderer = new Renderer(this);
        rajawaliSurfaceView.setSurfaceRenderer(renderer);

//        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
//        surface.setFrameRate(60.0);
//        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);
//        surface.setOnTouchListener(this);
//        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
//
//        renderer = new Renderer(this);
//        surface.setSurfaceRenderer(renderer);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event){

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                renderer.getObjectAt(event.getX(), event.getY(),event);
                Log.d("onTouched","ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                renderer.getObjectAt(event.getX(),event.getY(),event);
                Log.d("onTouched", "ACTION_MOVE");
                break;
            default:break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Point point = new Point();
//        point = new Point(0, 0).set(rajawaliSurfaceView.getWidth(), rajawaliSurfaceView.getHeight());

        Display display = this.getWindowManager().getDefaultDisplay();
        display.getSize(point);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
