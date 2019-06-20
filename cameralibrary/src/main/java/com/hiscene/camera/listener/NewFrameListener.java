package com.hiscene.camera.listener;

import android.graphics.ImageFormat;
import android.hardware.Camera;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2017/8/1 14:34
 */
public interface NewFrameListener {
    void onNewFrame(byte[] data, Camera camera, int width, int height, int type);
}
