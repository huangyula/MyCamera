package com.hiscene.camera.listener;

import com.google.zxing.Result;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2017/11/15.
 */
public interface OnQrRecognizeListener {
    /**
     *
     * @param result
     * @return true 识别成功不再识别 false 继续识别
     */
    boolean OnRecognize(Result result);
}
