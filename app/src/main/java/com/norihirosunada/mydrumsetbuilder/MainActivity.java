package com.norihirosunada.mydrumsetbuilder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    enum ButtonState{OPEN,CLOSE}
    ButtonState addObj;

    Point point;

    static String TAG = "MainActivity";

    CanvasView canvasView;

    File file;

    LinearLayout linearLayout;
    NumberPicker numberPicker1,numberPicker2;
    Button button;
    TextView text;
    String instrument;

    SharedPreferences pref;
    
    int cx,cy;

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
        cx = point.x/2;
        cy = point.y/2;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        addObj = ButtonState.CLOSE;
        final FloatingActionButton addCymbal = (FloatingActionButton)findViewById(R.id.fab_cymbal);
        final FloatingActionButton addDrum = (FloatingActionButton)findViewById(R.id.fab_drum);
        final FloatingActionButton addBass = (FloatingActionButton)findViewById(R.id.fab_bass);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action
                if (addObj == ButtonState.CLOSE) {
                    addCymbal.show();
                    addDrum.show();
                    addBass.show();
                    addObj = ButtonState.OPEN;

                } else {
                    addCymbal.hide();
                    addDrum.hide();
                    addBass.hide();
                    addObj = ButtonState.CLOSE;

                }
            }
        });

        addCymbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action
                linearLayout.setVisibility(View.VISIBLE);
                instrument = "cymbal";
                text.setText("Set Scale");
            }
        });

        addDrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action
                linearLayout.setVisibility(View.VISIBLE);
                instrument = "drum";
                text.setText("Set Scale");
            }
        });

        addBass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FAB action
                linearLayout.setVisibility(View.VISIBLE);
                numberPicker2.setVisibility(View.VISIBLE);
                instrument = "bass";
                text.setText("Set Radius & Depth");
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

        pref = getSharedPreferences("pref",MODE_PRIVATE);
        if(pref.getBoolean("active",false)) {
            canvasView.onLoadJson(pref);
        }else {
            canvasView.post(new Runnable() {
                @Override
                public void run() {

                    preset();

                    canvasView.invalidate();
                }
            });
            pref.edit().putBoolean("active",true).apply();
        }

        file = new File(Environment.getExternalStorageDirectory() + "MyDrumSetBuilder/capture.jpeg");

        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        numberPicker1 = (NumberPicker)findViewById(R.id.numPicker1);
        numberPicker2 = (NumberPicker)findViewById(R.id.numPicker2);
        button = (Button)findViewById(R.id.button1);
        text = (TextView)findViewById(R.id.text);
        numberPicker1.setMinValue(8);
        numberPicker1.setMaxValue(24);
        numberPicker2.setMinValue(8);
        numberPicker2.setMaxValue(24);
        linearLayout.setVisibility(View.GONE);
        numberPicker2.setVisibility(View.GONE);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        point = new Point();

        Display display = this.getWindowManager().getDefaultDisplay();
        display.getSize(point);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_preset:
                preset();
                Log.d(TAG,"nav_preset");
                break;
            case R.id.nav_share:
                share(this,"#MyDrumSetting\nhttps://play.google.com/store/apps/details?id=com.norihirosunada.mydrumkitbuilder");
                Log.d(TAG, "nav_share Selected!");
                break;
            case R.id.nav_save:
                Log.d(TAG, "nav_save Selected!");
                saveToFile();
                canvasView.onSaveJson(pref);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Bitmap getViewBitmap(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();
        if(cache == null){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cache);
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void share(final Activity activity, final String text) {
        File imagePath= new File(Environment.getExternalStorageDirectory().getPath() + "/MyDrumSetGallery/setting.PNG");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imagePath.getPath()));

        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Client not found.", Toast.LENGTH_LONG).show();
        }
    }

    public void saveToFile() {
        if (!sdcardWriteReady()){
            return;
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/MyDrumSetGallery/");
            if (!file.exists()) {
                file.mkdir();
            }
            String AttachName = file.getAbsolutePath() + "/setting" + ".PNG";
            FileOutputStream out = new FileOutputStream(AttachName);
            getViewBitmap(canvasView).compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean sdcardWriteReady() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    public void setScale(View v){
        linearLayout.setVisibility(View.GONE);
        float scale1 = numberPicker1.getValue()*10;
        float scale2 = numberPicker2.getValue()*10;
        switch (instrument){
            case "cymbal":
                canvasView.addDrum(cx, cy, scale1, "cymbal");
                break;
            case "drum":
                canvasView.addDrum(cx,cy,scale1,"drum");
                break;
            case "bass":
                numberPicker2.setVisibility(View.GONE);
                canvasView.addDrum(cx,cy,scale1,scale2,"bass");
                break;
        }
        canvasView.invalidate();
    }

    public void preset(){
        canvasView.addDrum(cx, cy - 200, 220, 180, "bass");
        canvasView.addDrum(cx - 130, cy - 130, 100, "drum");  //high tom
        canvasView.addDrum(cx + 100, cy - 130, 120, "drum"); //low tom
        canvasView.addDrum(cx - 200, cy + 130, 140, "drum"); //snare drum
        canvasView.addDrum(cx + 200, cy + 150, 160, "drum"); //floor tom
        canvasView.addDrum(cx - 420, cy + 50, 140, "cymbal");   //hihat
        canvasView.addDrum(cx + 450, cy + 100, 200, "cymbal");   //ride
        canvasView.addDrum(cx - 350, cy - 200, 160, "cymbal");   //crash
        canvasView.addDrum(cx + 350, cy - 180, 180, "cymbal");   //crash
        canvasView.invalidate();
    }
}
