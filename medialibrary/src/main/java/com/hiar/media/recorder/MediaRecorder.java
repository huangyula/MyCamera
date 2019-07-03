package com.hiar.media.recorder;


import com.github.weiss.core.thread.QueueRunnable;
import com.hiar.media.recorder.audio.AudioRecordRecorder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/18
 * @des
 */
public class MediaRecorder extends QueueRunnable implements AudioRecordRecorder.OnAudioRecordListener{
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("soundtouch");
        System.loadLibrary("media_player");
        System.loadLibrary("video_editor");
    }

    /**
     * 录制原始的音频数据
     */
    private AudioRecordRecorder audioRecorder;
    protected ReentrantLock lock = new ReentrantLock();

    private long ptr;

    public long getPtr() {
        return ptr;
    }

    public MediaRecorder() {
        ptr = create();
        audioRecorder = new AudioRecordRecorder(null);
        audioRecorder.setOnAudioRecordListener(this);
        if (audioRecorder != null) {
            audioRecorder.initRecorder();
        }
    }

    public void init(String url, int width, int height) {
        if (audioRecorder != null) {
            audioRecorder.recordStart();
        }
        if (ptr > 0) {
            init(ptr, url, width, height);
        }
    }

    public void encodeAndWriteVideo(byte[] data) {
        runAll();
        if (ptr > 0) {
            encodeAndWriteVideo(ptr, data);
//            LogUtils.d("encodeAndWriteVideo data size:"+data.length);
        }
    }

    public void writeH264Video(byte[] data, int length) {
        if (ptr > 0) {
            writeH264Video(ptr, data, length);
//            LogUtils.d("encodeAndWriteVideo data size:"+data.length);
        }
    }

    public void destroy() {
        lock.lock();
        if (ptr > 0) {
            destroy(ptr);
            ptr = -1;
        }
        lock.unlock();
        if (audioRecorder != null) {
            audioRecorder.setOnAudioRecordListener(null);
            audioRecorder.recordStop();
            audioRecorder = null;
        }
    }

    @Override
    public void onAudioBuffer(byte[] data, int length) {
        queueEvent(()->{
            lock.lock();
            if (ptr > 0) {
                encodeAndWriteAudio(ptr, data.clone(), length);

            }
            lock.unlock();
        });
    }


    protected static native long create();

    protected static native void init(long mediaRecorder, String url, int width, int height);

    protected static native void encodeAndWriteVideo(long mediaRecorder, byte[] data);

    protected static native void encodeAndWriteAudio(long mediaRecorder, byte[] data, int length);

    protected static native void writeH264Video(long mediaRecorder, byte[] data, int length);

    protected static native void destroy(long mediaRecorder);

}
