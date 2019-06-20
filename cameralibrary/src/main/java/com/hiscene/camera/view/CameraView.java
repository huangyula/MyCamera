package com.hiscene.camera.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;


import com.hiar.utils.LogUtils;
import com.hiar.utils.OpenglUtil;
import com.hiscene.camera.core.CameraSource;
import com.hiscene.camera.renderer.RendererController;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by li on 2017/4/8.
 */

public class CameraView extends BaseGLView {

    public CameraView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        CameraRenderer mRenderer = new CameraRenderer();
        setEGLConfigChooser(8, 8, 8, 8, 24, 8);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    public void pause() {
        CameraSource.Instance().stopPreview();
        CameraSource.Instance().closeCamera();
        LogUtils.i("CameraSource.pause");
    }

    public void resume() {
        CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_BACK);
        LogUtils.i("CameraSource.resume");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_BACK);
        LogUtils.i("CameraView surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        CameraSource.Instance().stopPreview();
        CameraSource.Instance().closeCamera();
        LogUtils.i("CameraView surfaceDestroyed");
    }


    class CameraRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            LogUtils.i("CameraView onSurfaceCreated");
            RendererController.Instance().init();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            LogUtils.i("CameraView onSurfaceChanged");
            GLES20.glViewport(0, 0, width, height);
            RendererController.Instance().configScreen(width, height);
//            CameraSource.Instance().setPreviewSize(size.width, size.height);
            int startPreview = CameraSource.Instance().startPreview();
            LogUtils.i("Camera startPreview : "+startPreview);
//            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            RendererController.Instance().drawVideoBackground();
            if (needScreenShot && screenShotListener != null) {
                //FileUtil.getGLPixels(getWidth(),getHeight(),path+File.separator+"car.png");
                LogUtils.i("   time  camera start ==========  " + System.currentTimeMillis());
                screenShotListener.screenShot(OpenglUtil.getGLPixels(getWidth(), getHeight()), getWidth(), getHeight(), 1);
                needScreenShot = false;
            }
        }
    }

}
