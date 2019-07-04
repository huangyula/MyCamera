package com.hiscene.flytech.recorder;

import android.hardware.Camera;

import com.github.weiss.core.utils.FileUtils;
import com.hiar.media.recorder.MediaRecorder;
import com.hiscene.camera.renderer.RendererController;
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

    boolean init = false;
    String url;

    public void init() {
        lock.lock();
        mediaRecorder = new MediaRecorder();
        init = false;
        lock.unlock();
    }

    @Override
    public void onNewFrame(byte[] data, Camera camera, int width, int height, int type) {
        super.onNewFrame(data, camera, width, height, type);
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void loop4End() {
        if(mediaRecorder != null) {
            if (init) {
                mediaRecorder.encodeAndWriteVideo(recognizerBuffer.array());
            } else {
                url = RecorderUtils.getFilePath();
                FileUtils.createFileByDeleteOldFile(url);
                mediaRecorder.init(url, frameWidth, frameHeight);
                init = true;
            }
        }
    }

    public void destroy() {
        if(mediaRecorder != null) {
            lock.lock();
            mediaRecorder.destroy();
            mediaRecorder = null;
            lock.unlock();
        }
    }
}
