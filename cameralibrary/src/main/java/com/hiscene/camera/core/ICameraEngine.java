package com.hiscene.camera.core;

import android.content.Context;
import android.graphics.SurfaceTexture;

/**
 * @author: liwenfei.
 * data: 2018/11/8 20:20.
 */
public interface ICameraEngine {
    interface OnNewFrameListener {
        void onNewFrame(byte[] data, int width, int height);

        void onError(int error);
    }

    int openCamera(boolean isFront, SurfaceTexture surfaceTexture);

    int closeCamera();

    int switchCamera(SurfaceTexture mSurfaceTexture);

    int getPreViewWidth();

    int getPreViewHeight();

    void setOnNewFrameListener(OnNewFrameListener onNewFrame);

    boolean isFrontCamera();

    void handleZoom(float ratio);

    void handleFocusMetering(int centerX, int centerY);

    boolean switchFlashLight(Context context);

    void closeFlashLight(Context context);
}
