package com.hiscene.camera.renderer;

import android.hardware.Camera;

/**
 * Created by li on 2016/11/17.
 */

public class RendererController {
    private static class SingletonHolder {
        static final RendererController _instance = new RendererController();
    }

    RendererController(){

    }

    public void init(){
        nv21Renderer = new SourceNV21Renderer();
        if(orientation != -1){
            nv21Renderer.configOrientation(orientation);
        }
    }

    private int orientation = -1;
    private SourceRenderer nv21Renderer;

    public boolean isTextureInit() {
        return nv21Renderer.isTextureInit();
    }
    public void release(){
        nv21Renderer.resetTextureTag();
    }

    public void updateCameraLabel(){
        nv21Renderer.updateCameraLabel();
    }

    public static RendererController Instance(){
        return SingletonHolder._instance;
    }

    public void configScreen(int width,int height){
        synchronized (nv21Renderer){
            nv21Renderer.configScreen(width,height);
        }
    }

    public void configOrientation(int ori){
        orientation = ori;
        if(nv21Renderer!=null){
            nv21Renderer.configOrientation(ori);
        }
    }

    public void drawVideoBackground(){
        synchronized (nv21Renderer){
            nv21Renderer.draw();
        }
    }

    public void onFrame(byte[] data, Camera camera, int width, int height) {
        synchronized (nv21Renderer){
            nv21Renderer.setPictureSize(width,height);
            nv21Renderer.setNV21Data(data,camera,width,height);
        }
    }
}
