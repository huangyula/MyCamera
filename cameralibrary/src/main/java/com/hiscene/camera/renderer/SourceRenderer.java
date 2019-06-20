package com.hiscene.camera.renderer;

import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hiar.utils.LogUtils;
import com.hiar.utils.OpenglUtil;

import java.nio.Buffer;

/**
 * Created by li on 2016/11/23.
 */

abstract class SourceRenderer extends QueueRunnable {


    protected static int screenWidth = -1;
    protected static int screenHeight = -1;
    protected int previewWidth = -1;
    protected int previewHeight = -1;
    protected int deviceOrientation = -1;// 0: Portrait , 1: LandLeft, 2: UpSideDown,3: LandRight
    protected float[] mvpMatrix = new float[16];

    protected Buffer quadVertices, quadTexCoords, quadIndices;

    private boolean texCoordsInit = false;
    protected boolean textureInit = false;

    public void resetTextureTag(){
        textureInit = false;
    }

    public boolean isTextureInit() {
        return textureInit;
    }

    public void updateCameraLabel(){}

    SourceRenderer(){
        quadVertices = OpenglUtil.makeDoubleBuffer(quadVerticesArray);
        quadIndices = OpenglUtil.makeByteBuffer(quadIndicesArray);
        quadTexCoords = OpenglUtil.makeDoubleBuffer(quadTexCoordsArray);
    }

    public void setPictureSize(int preWidth, int preHeight) {
        previewWidth = preWidth;
        previewHeight = preHeight;
        configTexCoords();
    }

    public void configScreen(int sWidth, int sHeight) {
        texCoordsInit = false;
        screenWidth = sWidth;
        screenHeight = sHeight;
        configTexCoords();
    }

    public void configOrientation(int orientation){
        LogUtils.i("orientation: "+ orientation);
        deviceOrientation = orientation;
        Matrix.setIdentityM(mvpMatrix,0);
        switch (orientation){
            case 0:
                Matrix.rotateM(mvpMatrix,0,270,0,0,1);
                break;
            case 1:
                break;
            case 2:
                Matrix.rotateM(mvpMatrix,0,90,0,0,1);
                break;
            case 3:
                Matrix.rotateM(mvpMatrix,0,180,0,0,1);
                break;
            default:
                break;
        }
    };

    protected double quadVerticesArray[] = {-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f};

    protected double quadTexCoordsArray[] = {0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

    protected short quadIndicesArray[] = {0, 1, 2, 2, 3, 0};

    protected void configTexCoords(){
        if(texCoordsInit)return;
        /*
         * Since the ratio of screen and preview is different, we should clip
         * some part of the preview in order to get right
         */
        if(previewWidth == -1||previewHeight== -1||screenHeight==-1||screenWidth==-1)return;
        Matrix.setIdentityM(mvpMatrix,0);
        if(deviceOrientation == -1 && screenWidth < screenHeight ){
            Matrix.rotateM(mvpMatrix,0,270,0,0,1);
        }
        quadTexCoordsArray[0] = 0.0f;
        quadTexCoordsArray[1] = 1.0f;
        quadTexCoordsArray[2] = 1.0f;
        quadTexCoordsArray[3] = 1.0f;
        quadTexCoordsArray[4] = 1.0f;
        quadTexCoordsArray[5] = 0.0f;
        quadTexCoordsArray[6] = 0.0f;
        quadTexCoordsArray[7] = 0.0f;
        int sWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        int sHeight = screenWidth > screenHeight ? screenHeight : screenWidth;
        float screenRatio = sWidth * 1.0f / sHeight;
        float previewRatio = previewWidth * 1.0f / previewHeight;
        if (screenRatio > previewRatio) {
            float preHeight = sWidth * previewHeight * 1.0f / previewWidth;
            float ratio = sHeight / preHeight;
            quadTexCoordsArray[1] = ratio / 2 + 0.5f;
            quadTexCoordsArray[3] = ratio / 2 + 0.5f;
            quadTexCoordsArray[5] = (1 - ratio) / 2.0f;
            quadTexCoordsArray[7] = (1 - ratio) / 2.0f;
        } else if (screenRatio < previewRatio) {
            float preWidth = (sHeight * previewWidth * 1.0f) / previewHeight;
            float ratio = sWidth * 1.0f / preWidth;
            quadTexCoordsArray[0] = (1 - ratio) / 2.0f;
            quadTexCoordsArray[2] = ratio / 2 + 0.5f;
            quadTexCoordsArray[4] = ratio / 2 + 0.5f;
            quadTexCoordsArray[6] = (1 - ratio) / 2.0f;
        }//对比下和我做的那个版本的差距
        quadTexCoords = OpenglUtil.makeDoubleBuffer(quadTexCoordsArray);
        texCoordsInit = true;
    }

    abstract void draw();
    abstract void setNV21Data(byte[] data, final Camera camera, int width, int height);

    int[] oldGLBind = new int[10];

    /**
     * 记录GL状态
     */
    public void recordGLStatus() {
        OpenglUtil.checkGLError("recordGLStatus Begin");
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, oldGLBind, 0);
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_2D, oldGLBind, 1);
        GLES20.glGetIntegerv(GLES20.GL_TEXTURE_BINDING_CUBE_MAP, oldGLBind, 2);
        GLES20.glGetIntegerv(GLES20.GL_CURRENT_PROGRAM, oldGLBind, 3);
        GLES20.glGetIntegerv(GLES20.GL_RENDERBUFFER_BINDING, oldGLBind, 4);
        GLES20.glGetIntegerv(GLES20.GL_ACTIVE_TEXTURE, oldGLBind, 5);
        GLES20.glGetIntegerv(GLES20.GL_ELEMENT_ARRAY_BUFFER_BINDING, oldGLBind, 6);
        GLES20.glGetIntegerv(GLES20.GL_ARRAY_BUFFER_BINDING, oldGLBind, 7);
        OpenglUtil.checkGLError("recordGLStatus End");
    }

    /**
     * 恢复GL状态
     */
    public void restoreGLStatus() {
        OpenglUtil.checkGLError("restoreGLStatus Begin");
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, oldGLBind[0]);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, oldGLBind[4]);
        GLES20.glActiveTexture(oldGLBind[5]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, oldGLBind[1]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, oldGLBind[2]);
        GLES20.glUseProgram(oldGLBind[3]);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, oldGLBind[6]);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, oldGLBind[7]);
        OpenglUtil.checkGLError("restoreGLStatus End");
    }
}