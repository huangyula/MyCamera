package com.hiscene.camera.vision;


import android.graphics.Rect;
import android.hardware.Camera;

import com.github.weiss.core.thread.LoopThread;
import com.github.weiss.core.utils.LogUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hiscene.camera.core.CameraSource;
import com.hiscene.camera.listener.NewFrameListener;
import com.hiscene.camera.listener.OnQrRecognizeListener;
import com.hiscene.camera.qr.DecodeFormatManager;
import com.hiscene.camera.qr.PlanarYUVLuminanceSource;
import com.hiscene.camera.renderer.RendererController;

import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des 二维码识别视觉模块
 */
public class QRVision extends LoopThread implements NewFrameListener {
    protected ByteBuffer recognizerBuffer = null;
    protected boolean bufferInit = false;
    protected ReentrantLock lock = new ReentrantLock();
    boolean needRecognize = false;


    enum State {
        RECOGNIZED,
        TRACKING,
        NONE
    }

    private State processState = State.NONE;


    @Override
    public void onNewFrame(byte[] data, Camera camera, int width, int height, int type) {
        frameWidth = width;
        frameHeight = height;
        if (!bufferInit) {
            recognizerBuffer = ByteBuffer.allocate(data.length);
            bufferInit = true;
        }
        if (processState == State.NONE) {
            if (lock.tryLock()) {
                if (!needRecognize) {
                    recognizerBuffer.position(0);
                    recognizerBuffer.put(data);
                    needRecognize = true;
                }
                lock.unlock();
            }
        }
        RendererController.Instance().onFrame(data, camera, width, height);
    }

    @Override
    public void setup() {
        CameraSource.Instance().setNewFrameListener(this);

        multiFormatReader = new MultiFormatReader();
        hints = new Hashtable<DecodeHintType, Object>(3);
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
/*        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }*/
//        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
        multiFormatReader.setHints(hints);
    }

    @Override
    public void loop() {
        if (processState == State.NONE && needRecognize) {
            lock.lock();
            processState = State.NONE;
            needRecognize = false;
            if (isNeedQRRecognize) {
                doQrRecognize(recognizerBuffer.array(), frameWidth, frameHeight);
            }
            loop4End();
            lock.unlock();
        }
/*        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void loop4End() {

    }

    @Override
    public void over() {

    }


    /**************          about  QR  recognize              ***************/
//    private QRCodeReader qr = new QRCodeReader();
    private MultiFormatReader multiFormatReader;
    private Result result;
    private boolean isNeedQRRecognize = false;
    private boolean isAR = true;
    protected int frameWidth, frameHeight;
    Hashtable<DecodeHintType, Object> hints;

    public void startQRRecognize() {
        isNeedQRRecognize = true;
    }

    private void doQrRecognize(byte[] data, int width, int height) {
        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, width, height);
        if (source != null) {
//            isNeedQRRecognize = false;
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = multiFormatReader.decodeWithState(bitmap);
                LogUtils.i("====  qr result: " + result + "====  width: " + width + "====  height: " + height);

                if (mOnQrRecognizeListener != null) {
                    isNeedQRRecognize = mOnQrRecognizeListener.OnRecognize(result);
                } else {
                    isNeedQRRecognize = false;
                }
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }
    }

    /**
     * 二维码回调接口
     */
    private OnQrRecognizeListener mOnQrRecognizeListener;

    public void setOnQrRecognizeListener(OnQrRecognizeListener onQrRecognizeListener) {
        mOnQrRecognizeListener = onQrRecognizeListener;
    }

    /**
     * CarPublicPraiseComment2 factory method to build the appropriate LuminanceSource object based on the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   CarPublicPraiseComment2 preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return CarPublicPraiseComment2 PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
//        rect.set(0, 0, width, height);
        if (rect == null) {
            rect = new Rect();
            rect.set(0, 0, width, height);
        }
//        LogUtils.i("====  QR FramingRectInPreview: ====  left: " + rect.left + "====  top: " + rect.top+ "====  width: " + rect.width()+ "====  height: " + rect.height());
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
    }

    private Rect framRect;

    private Rect getFramingRectInPreview() {
/*        if (framRect == null) {
            framRect = new Rect();
        }*/
        return framRect;
    }

    public void setFramingRectInPreview(Rect framRect) {
        this.framRect = framRect;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }
}
