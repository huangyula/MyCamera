package com.hiscene.flytech.recorder;

import android.hardware.Camera;

import com.github.weiss.core.utils.FileUtils;
import com.hiar.media.recorder.MediaRecorder;
import com.hiscene.camera.vision.QRVision;

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

    public void init(String url) {
        this.url = url;
        FileUtils.createFileByDeleteOldFile(url);
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public void onNewFrame(byte[] data, Camera camera, int width, int height, int type) {
        if(init) {
            mediaRecorder.encodeAndWriteVideo(data.clone());
        }else {
            mediaRecorder.init(url,width,height);
            init = true;
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

    @Override
    public void over() {
        super.over();
        mediaRecorder.destroy();
    }
}
