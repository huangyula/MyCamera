package com.hiar.media.recorder;


import com.hiar.utils.LogUtils;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/18
 * @des
 */
public class MediaRecorder {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("soundtouch");
        System.loadLibrary("media_player");
        System.loadLibrary("video_editor");
    }

    private long ptr;

    public long getPtr() {
        return ptr;
    }

    public MediaRecorder() {
        ptr = create();
    }

    public void init(String url, int width, int height) {
        if (ptr > 0)
            init(ptr, url, width, height);
    }

    public void encodeAndWriteVideo(byte[] data) {
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
        if (ptr > 0) {
            destroy(ptr);
            ptr = -1;
        }
    }


    protected static native long create();

    protected static native void init(long mediaRecorder, String url, int width, int height);

    protected static native void encodeAndWriteVideo(long mediaRecorder, byte[] data);

    protected static native void writeH264Video(long mediaRecorder, byte[] data, int length);

    protected static native void destroy(long mediaRecorder);
}
