package com.norihirosunada.mydrumkitbuilder;

import android.opengl.GLES20;

import org.rajawali3d.loader.awd.BlockSimpleMaterial;

/**
 * Created by norihirosunada on 16/06/14.
 */
public class CustomMaterial extends BlockSimpleMaterial {
    protected static final String mCustomFShader =
            "precision mediump float;" +

                    "uniform float uTime;" +
                    "varying vec2 vTextureCoord;" +

                    "void main() {" +
                    "   vec4 newColor = vec4(1.0, 0, 0, 1.0);" +
                    "   float x = min(vTextureCoord.s, 1.0 - vTextureCoord.s);" +
                    "   float y = vTextureCoord.t;" +
                    "   newColor.g = sin(x * cos(uTime/15.0) * 120.0) + " +
                    "       cos(y * sin(uTime/10.0) * 120.0) + " +
                    "       sin(sqrt(y * y + x * x) * 40.0);" +
                    "   gl_FragColor = newColor;" +
                    "}";

    protected int muTimeHandle;


    public CustomMaterial() {
//        super(mVShader, mCustomFShader);
    }

//    @Override
//    public void setShaders(String vertexShader, String fragmentShader)
//    {
////        super.setShaders(vertexShader, fragmentShader);
////        muTimeHandle = GLES20.glGetUniformLocation(mProgram, "uTime");
////        if(muTimeHandle == -1) {
////            throw new RuntimeException("Could not get uniform location for uTime");
////        }
//    }

    public void setTime(float time) {
        GLES20.glUniform1f(muTimeHandle, time);
    }
}
