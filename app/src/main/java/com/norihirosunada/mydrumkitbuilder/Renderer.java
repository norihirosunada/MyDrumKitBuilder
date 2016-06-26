package com.norihirosunada.mydrumkitbuilder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Camera2D;
import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;
import org.rajawali3d.util.RajLog;

/**
 * Created by norihirosunada on 16/06/12.
 */
public class Renderer extends RajawaliRenderer implements OnObjectPickedListener{

    public Context context;

    private DirectionalLight directionalLight;
    private Sphere earthSphere,sphere2;
    private Cube cube;
    private Object3D floor,tom1,tom2,tom3,tom4,bass,snare;
    private ObjectColorPicker mPicker;
    private float downX,downY,movedX,movedY;


    public Renderer(Context context){
        super(context);
        this.context = context;
        getCurrentScene().addCamera(new Camera2D());
        setFrameRate(60);
    }

    public void onTouchEvent(MotionEvent event){

    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }

    public void initScene(){

        directionalLight = new DirectionalLight(0f,0f,1f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);


        Material matWhite = new Material();
        matWhite.enableLighting(true);
        matWhite.setDiffuseMethod(new DiffuseMethod.Lambert());
        matWhite.setColor(Color.WHITE);

        Material matBlack = new Material();
        matBlack.enableLighting(true);
//        matBlack.setAmbientColor(Color.WHITE);
//        matBlack.setColor(Color.BLACK);
        matBlack.setColor(Color.WHITE);

//        Material outline = new Material();
//        outline.enableLighting(true);

        Material toonMat = new Material();
        toonMat.enableLighting(true);
        toonMat.setDiffuseMethod(new DiffuseMethod.Toon());
//        toonMat.setDiffuseMethod(new DiffuseMethod.Toon(0xffffffff, 0xff000000, 0xff666666, 0xff000000));


        Texture texture = new Texture("Outline", R.drawable.outlinematerial);
        try{
            matBlack.addTexture(texture);

        } catch (ATexture.TextureException error){
            Log.d("DEBUG", "TEXTURE ERROR");
        }



//        earthSphere = new Sphere(1,24,24);
//        earthSphere.setMaterial(material);
//        earthSphere.setPosition();
//        earthSphere.setPosition(1.0, 1.0, 0.0);
//        Log.d("position", "" + earthSphere.getPosition());

//        CustomMaterial mcustomMaterial = new CustomMaterial();

//        sphere2 = new Sphere(1,12,12);
//        sphere2.setMaterial(matBlack);
//
        cube = new Cube(1.5f);
        cube.setMaterial(matBlack);

        LoaderOBJ parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.floor);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        floor = parser.getParsedObject();
        floor.setMaterial(matBlack);
        floor.setPosition(.0, .0, .0);
        floor.setMaterial(matWhite);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.tom1008);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        tom1 = parser.getParsedObject();
        tom1.setMaterial(matBlack);
        tom1.setPosition(.0, .0, .0);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.tom1209);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        tom2 = parser.getParsedObject();
        tom2.setMaterial(matBlack);
        tom2.setPosition(.0, .0, .0);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.tom1310);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        tom3 = parser.getParsedObject();
        tom3.setMaterial(matBlack);
        tom3.setPosition(.0, .0, .0);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.tom1614);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        tom4 = parser.getParsedObject();
        tom4.setMaterial(matBlack);
        tom4.setPosition(.0, .0, .0);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.bass2218);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        bass = parser.getParsedObject();
        bass.setMaterial(matBlack);
        bass.setPosition(.0, .0, .0);

        parser = new LoaderOBJ(getContext().getResources(), mTextureManager, R.raw.snare1455);
        try {
            parser.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        snare = parser.getParsedObject();
        snare.setMaterial(matBlack);
        snare.setScale(2.0);
        snare.setPosition(.0, .0, .0);
//        floor.setRotation(Vector3.Axis.X,-90.0);

        getCurrentScene().addChild(floor);
        getCurrentScene().addChild(tom1);
        getCurrentScene().addChild(tom2);
        getCurrentScene().addChild(tom3);
        getCurrentScene().addChild(tom4);
        getCurrentScene().addChild(bass);
        getCurrentScene().addChild(snare);

//        getCurrentScene().addChild(sphere2);
//        getCurrentScene().addChild(earthSphere);
        getCurrentScene().addChild(cube);
        getCurrentCamera().setZ(.0);
        getCurrentCamera().setY(20f);
//        getCurrentCamera().setPosition(.0, .0, 10f);
        getCurrentCamera().setRotZ(90f);

        directionalLight.setPosition(getCurrentCamera().getPosition());
//        directionalLight.setRotation(getCurrentCamera().getRotX(), getCurrentCamera().getRotY(), getCurrentCamera().getRotZ());
        getCurrentScene().addLight(directionalLight);



//        BlockSimpleMaterial simpleMaterial = new BlockSimpleMaterial();
//        simpleMaterial.setUseColor

        mPicker = new ObjectColorPicker(this);
        mPicker.setOnObjectPickedListener(this);
        mPicker.registerObject(snare);
        mPicker.registerObject(tom4);

        Plane plane = new Plane(1, 1, 1, 1, 1);
        plane.setMaterial(matWhite);
        getCurrentScene().addChild(plane);
        mPicker.registerObject(plane);


    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
//        earthSphere.rotate(Vector3.Axis.Y, 1.0);
//        floor.rotate(Vector3.Axis.X,1.0);
//        sphere2.rotate(Vector3.Axis.Y, 1.0);

    }


    public void onObjectPicked(@NonNull Object3D object){
        Log.d("position", "" + object.getX());
        Log.d("position", "" + object.getY());
        if(downX > movedX){
            object.setX(object.getX() - 0.05);
            Log.d("move", "left");
        }else if(downX < movedX){
            object.setX(object.getX() + 0.05);
            Log.d("move", "right");
        }
        if(downY > movedY){
            object.setZ(object.getZ() - 0.05);
            Log.d("move", "up");
        }else if(downY < movedY){
            object.setZ(object.getZ() + 0.05);
            Log.d("move", "down");
        }
        downX = movedX;
        downY = movedY;
    }

    public void getObjectAt(float x, float y, MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPicker.getObjectAt(x,y);
                downX = x;
                downY = y;
                Log.d("MotionEvent","X="+ x);
                Log.d("MotionEvent","Y="+ y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPicker.getObjectAt(x,y);
                movedX = x;
                movedY = y;
                Log.d("MotionEvent","moved");
                break;
            default:break;
        }

    }

//    @Override
    public void onNoObjectPicked() {
        RajLog.w("No object picked!");
    }

    public void addObject(String drum, int size){
        switch (drum){
            case "hihat":
                break;
            case "crash":
                break;
            case "ride":
                break;
            case "china":
                break;
            case "splash":
                break;
            default:break;
        }
    }

    public void addObject(String drum, int width, int depth){
        switch (drum){
            case "snare":
                break;
            case "tom":
                break;
            case "floor":
                break;
            case "bass":
                break;
            default:break;
        }
    }
}
