package com.norihirosunada.mydrumsetbuilder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ShareCompat;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

//import org.rajawali3d.surface.RajawaliSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
        canvasView.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
//                    Random random = new Random();
//                    canvasView.addDrum(random.nextInt(canvasView.getWidth()), random.nextInt(canvasView.getHeight()), 50);
                    canvasView.addDrum(point.x/2,point.y/2,100,"drum");
                }
                canvasView.invalidate();
            }
        });

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

        pref = getSharedPreferences("pref",MODE_PRIVATE);
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
            case R.id.nav_edit:
                Log.d(TAG, "nav_edit Selected!");
                break;
            case R.id.nav_gallery:
                Log.d(TAG, "nav_gallery Selected!");
                canvasView.onLoadJson(pref);
                break;
            case R.id.nav_share:
//                tweetImage();
//                shareImage();
                share(this,"#MyDrumSetting");
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

    public void shareImage() {
        String appKeyword = "twitter";
        List<Intent> shareIntentList = new ArrayList<Intent>();
        //起動するインテントのリスト
        List<ResolveInfo> resolveInfoList =
                getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_SEND).setType("image/*"), 0);
        //SNSアプリの一覧
        for(ResolveInfo info : resolveInfoList){
            Intent shareIntent = new Intent(Intent.ACTION_SEND).setType("image/*");

            if(info.activityInfo.packageName.toLowerCase().contains(appKeyword)){
//                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                File image = new File(file.getAbsoluteFile()+"setting.PNG");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
                shareIntent.setPackage(info.activityInfo.packageName);
                shareIntentList.add(shareIntent);
            }
        }

        //もし該当するアプリが1つでもあれば起動する
        if(!shareIntentList.isEmpty()){
            Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0),
                    getString(R.string.sns_chooser_title));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    shareIntentList.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        }
    }

    public void tweetImage(){
        String message="テスト ＃コメント";
        File imagePath=new File(file.getAbsolutePath()+"setting.PNG");

        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(this);

// データをセットする
        builder.setChooserTitle("Choose App");
        builder.setText(message);
        builder.setType("image/png");
        builder.setStream(Uri.fromFile(imagePath));
// アプリ選択画面を起動
        builder.startChooser();
    }

    public void share(final Activity activity, final String text) {
        File imagePath=new File(file.getAbsolutePath()+"setting.PNG");
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
                canvasView.addDrum(point.x / 2, point.y / 2, scale1, "cymbal");
                break;
            case "drum":
                canvasView.addDrum(point.x/2,point.y/2,scale1,"drum");
                break;
            case "bass":
                numberPicker2.setVisibility(View.GONE);
                canvasView.addDrum(point.x/2,point.y/2,scale1,scale2,"bass");
                break;
        }
        canvasView.invalidate();
    }
}
