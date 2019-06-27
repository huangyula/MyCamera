package com.hiscene.flytech.recorder;

import android.hardware.Camera;

import com.github.weiss.core.utils.FileUtils;
import com.hiar.media.recorder.MediaRecorder;
import com.hiscene.camera.vision.QRVision;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des  摄像头录屏模块
 */
public class CameraRecorder extends QRVision {

    MediaRecorder mediaRecorder;
    ReentrantLock recorderLock = new ReentrantLock();

    boolean init = false;
    String url;

    public void init() {
        recorderLock.lock();
        mediaRecorder = new MediaRecorder();
        init = false;
        recorderLock.unlock();
    }

    @Override
    public void onNewFrame(byte[] data, Camera camera, int width, int height, int type) {
        if(mediaRecorder != null) {
            recorderLock.lock();
            if (init) {
                mediaRecorder.encodeAndWriteVideo(data.clone());
            } else {
                url = RecorderUtils.getFilePath();
                FileUtils.createFileByDeleteOldFile(url);
                mediaRecorder.init(url, width, height);
                init = true;
            }
            recorderLock.unlock();
        }
        super.onNewFrame(data, camera, width, height, type);
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void loop() {
        super.loop();
    }

    public void destroy() {
        recorderLock.lock();
        mediaRecorder.destroy();
        mediaRecorder = null;
        recorderLock.unlock();
    }
}
