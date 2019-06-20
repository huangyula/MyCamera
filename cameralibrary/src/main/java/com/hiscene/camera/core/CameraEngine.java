package com.hiscene.camera.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hiar.utils.LogUtils;
import com.hiscene.camera.utils.CameraConfigurationUtils;
import com.hiscene.camera.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author: liwenfei.
 * data: 2018/7/6 13:42.
 */
public class CameraEngine implements ICameraEngine {
    private final String TAG = "CameraEngine";
    private Camera camera = null;
    private SurfaceTexture mSurfaceTexture;

    private final int HEAD_CAMERA_ID = 1;//G200 头戴端
    private final int HAND_CAMERA_ID = 0;//G200 手持端
    private int camId = 0;

    private boolean isFrontCamera = false;

    public static final int FLASH_ERROR = 365;

//    @Inject
    public CameraEngine() {
    }

    /**
     * 预览宽
     */
    private int preViewWidth = 1280;

    /**
     * 预览高
     */
    private int preViewHeight = 720;

    /**
     * 当前是否为前置相机
     *
     * @return 是否为前置相机
     */
    @Override
    public boolean isFrontCamera() {
        if (Constant.IS_G200) {
            return false;
        }
        return isFrontCamera;
    }

    private int getCameraId(boolean isFront) {
        if (Constant.IS_G200) {
            return getCameraIdForG200(isFront);
        }
        camId = 0;
        int cameraNum = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cameraNum; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if ((isFront) && (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                camId = i;
                break;
            } else if ((!isFront) && (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)) {
                camId = i;
                break;
            }
        }
        return camId;
    }

    private int getCameraIdForG200(boolean isFront) {
        camId = 0;
        int cameraNum = Camera.getNumberOfCameras();
        if (cameraNum > 1 && isFront) {
            camId = 1;
        }
        return camId;
    }

    @Override
    public int getPreViewWidth() {
        return preViewWidth;
    }

    @Override
    public int getPreViewHeight() {
        return preViewHeight;
    }

    /**
     * 打开相机
     *
     * @param isFront 是否打开前置相机
     */
    @Override
    public synchronized int openCamera(boolean isFront, SurfaceTexture surfaceTexture) {
/*        LogUtils.i(TAG, "openCamera: front %s,thread id=%s,thread name=%s",
                isFront, Thread.currentThread().getId(), Thread.currentThread().getName());*/
        preViewWidth = 1280;
        preViewHeight = 720;
        try {
            if (null != camera) {
                closeCamera();
            }
            int index = getCameraId(isFront);
            camera = Camera.open(index);
            camera.setErrorCallback(cameraErrorCallback);
            setParameters();
            setCameraCallback();
            mSurfaceTexture = surfaceTexture;
            camera.setPreviewTexture(mSurfaceTexture);
            camera.startPreview();
            previewCount = 0;
            LogUtils.i(TAG, "startPreview");
            isFrontCamera = (index == Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
            e.printStackTrace();
            camera = null;
            return -1;
        }
        return 0;
    }

    private void setCameraCallback() {
        Camera.Parameters parameters = camera.getParameters();
        int mFrameWidth = parameters.getPreviewSize().width;
        int mFrameHeight = parameters.getPreviewSize().height;
        int frameSize = mFrameWidth * mFrameHeight;
        frameSize = frameSize * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
        byte[] mBuffer = new byte[frameSize];
        int bufferCount = 3;
        for (int i = 0; i < bufferCount; i++) {
            camera.addCallbackBuffer(mBuffer);
        }
        camera.setPreviewCallbackWithBuffer(previewCallback);
    }

    private void setParameters() {
        try {
            setDesiredParameters(false);
        } catch (Exception e) {
            // Failed, use safe mode
            try {
                setDesiredParameters(true);
            } catch (Exception e2) {
                // Well, darn. Give up
                LogUtils.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
            }
        }
    }

    /**
     * 关闭相机
     */
    @Override
    public synchronized int closeCamera() {
        LogUtils.i(TAG, "closeCamera");
        if (camera != null) {
            camera.setPreviewCallbackWithBuffer(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        return 0;
    }

    /**
     * 切换前后相机
     */
    @Override
    public synchronized int switchCamera(SurfaceTexture mSurfaceTexture) {
        LogUtils.i(TAG, "switchCamera");
        isFrontCamera = !isFrontCamera;
        int res;
        res = closeCamera();
        if (res == 0) {
            res = openCamera(isFrontCamera, mSurfaceTexture);
        }
        //切换摄像头时，闪光灯会被关了
        flashMode = false;
        return res;
    }

    /**
     * 切换对焦模式
     *
     * @param type 类型
     * @return 是否切换对焦模式成功
     */
    private boolean setFocusMode(int type) {
        if (camera == null) {
            LogUtils.e(TAG, "setFocusMode");
            return false;
        }

        Camera.Parameters params = camera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();

        switch (type) {
            case 0:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "Auto Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "Auto Mode not supported");
                }
                break;
            case 1:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "Continuous Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "Continuous Mode not supported");
                }
                break;
            case 2:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_EDOF)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "EDOF Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "EDOF Mode not supported");
                }
                break;
            case 3:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "Fixed Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "Fixed Mode not supported");
                }
                break;
            case 4:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "Infinity Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "Infinity Mode not supported");
                }
                break;
            case 5:
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                    camera.setParameters(params);
                    LogUtils.i(TAG, "Macro Mode");
                    return true;
                } else {
                    LogUtils.i(TAG, "Macro Mode not supported");
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 处理触摸对焦
     *
     * @param centerX 点击的中心x坐标
     * @param centerY 点击的中心y坐标
     */
    @Override
    public synchronized void handleFocusMetering(int centerX, int centerY) {
        if (Constant.IS_G200 && camId == HEAD_CAMERA_ID) {
            return;
        }
        LogUtils.d(TAG, "handleFocusMetering");
        if (camera == null) {
            return;
        }

        Rect focusRect = calculateTapArea(centerX, centerY, 1f);
        Rect meteringRect = calculateTapArea(centerX, centerY, 1.5f);

        camera.cancelAutoFocus();
        Camera.Parameters params = camera.getParameters();

        //设置对焦区域
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            LogUtils.i(TAG, "focus areas not supported");
        }

        //设置测光区域
        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 800));
            params.setMeteringAreas(meteringAreas);
        } else {
            LogUtils.i(TAG, "metering areas not supported");
        }

        //对支持对焦的设备进行对焦操作
        final String currentFocusMode = params.getFocusMode();

        int tryTimes = 5;
        for (int i = 0; i <= tryTimes; i++) {
            if (setFocusMode(i)) {
                break;
            }
        }

//        LogUtils.d(TAG, "auto focus thread: %s", Thread.currentThread().getName());
        //对焦后切换为之前的对焦模式
        try {
            camera.autoFocus((success, camera) -> {
                if (success) {
                    LogUtils.i(TAG, "focus success");
                    camera.cancelAutoFocus();
                    Camera.Parameters params1 = camera.getParameters();
                    params1.setFocusMode(currentFocusMode);
                    try {
                        camera.setParameters(params1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.e(TAG, "onAutoFocus error");
                    }
                } else {
                    LogUtils.d(TAG, "autoFocus failed");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtils.e(TAG, "handleFocusMetering", "error %s", e.getMessage());
        }
    }

    private boolean flashMode = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean switchFlashLight(Context context) {
//        LogUtils.d(TAG, "switchFlashLight now flag : %s", flashMode);
        if (camera != null) {
            changeFlashLight(!flashMode, context);
            CameraConfigurationUtils.setBestExposure(camera.getParameters(), !flashMode);
            return true;
        }
        LogUtils.e(TAG, "switchFlashLight no camera");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void closeFlashLight(Context context) {
        if (camera != null) {
            changeFlashLight(false, context);
            CameraConfigurationUtils.setBestExposure(camera.getParameters(), false);
            flashMode = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeFlashLight(boolean openOrClose, Context context) {
        //判断API是否大于24（安卓7.0系统对应的API）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                LogUtils.d(TAG, "changeFlashLight");
                //获取CameraManager
                CameraManager mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                //获取当前手机所有摄像头设备ID
                String[] ids = mCameraManager.getCameraIdList();
                for (String id : ids) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    //查询该摄像头组件是否包含闪光灯
                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flashAvailable != null && flashAvailable
                            && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        //打开或关闭手电筒
                        if (Integer.parseInt(id) != camId) {
                            mCameraManager.setTorchMode(id, openOrClose);
                            flashMode = !flashMode;
                        } else {
                            Camera.Parameters parameters = camera.getParameters();
                            try {
                                parameters.setFlashMode(openOrClose ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                                camera.setParameters(parameters);
                                flashMode = !flashMode;
                            } catch (Exception e) {
//                                LogUtils.e(TAG, "switchFlashLight setFlashMode error : %s", e.getLocalizedMessage());
                                if (newFrameListener != null) {
                                    newFrameListener.onError(FLASH_ERROR);
                                }
                            }
                        }
                    }
                }
            } catch (CameraAccessException e) {
//                LogUtils.e(TAG, "changeFlashLight setTorchMode error : %s", e.getLocalizedMessage());
                if (newFrameListener != null) {
                    newFrameListener.onError(FLASH_ERROR);
                }
            }
        }
    }

    /**
     * 计算点击的区域，由中心点转换成一个矩形区域
     *
     * @param centerX     中心x坐标
     * @param centerY     中心y坐标
     * @param coefficient 放大系数
     * @return 矩形区域
     */
    private Rect calculateTapArea(int centerX, int centerY, float coefficient) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(clamp(centerX - halfAreaSize, -1000, 1000)
                , clamp(centerY - halfAreaSize, -1000, 1000)
                , clamp(centerX + halfAreaSize, -1000, 1000)
                , clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    /**
     * 限定x的值在min与max之间
     *
     * @param x   需要限制值
     * @param min 左边界
     * @param max 右边界
     * @return 限制在左右边界的值
     */
    private int clamp(int x, int min, int max) {
        return x > max ? max : x < min ? min : x;
    }

    private int previewCount = 0;
    private Camera.PreviewCallback previewCallback = (data, camera) -> {
        if (data == null || data.length == 0) {
            LogUtils.e(TAG, "previewCallback data is NULL");
            return;
        }
        if (this.newFrameListener != null) {
            this.newFrameListener.onNewFrame(data, preViewWidth, preViewHeight);
        }
        if (previewCount == 0 || previewCount % 90 == 0) {
//            LogUtils.d(TAG, "PreviewCallback len=%s", data.length);
        }
        previewCount++;

        if (camera != null) {
            camera.addCallbackBuffer(data);
        }
    };

    private OnNewFrameListener newFrameListener = null;

    @Override
    public void setOnNewFrameListener(OnNewFrameListener onNewFrame) {
        this.newFrameListener = onNewFrame;
    }

    /**
     * 选取最佳预览尺寸
     */
    private Camera.Size getBestSize(List<Camera.Size> sizes) {
        Collections.sort(sizes, new SortComparator());
        Camera.Size temp = null;
        Camera.Size temp2 = null;
        for (Camera.Size s : sizes) {
            if (s.width == preViewWidth && s.height == preViewHeight) {
                temp = s;
                break;
            }
            if (((s.width * 9) == (s.height * 16)) && (s.width >= 640)) {
                temp = s;
            }
            if (((s.width * 3) == (s.height * 4)) && (s.width >= 640)) {
                temp2 = s;
            }
        }
        if (temp != null) {
//            LogUtils.i(TAG, "getBestSize: width- %d, height- %d", temp.width, temp.height);
        } else if (temp2 != null) {
            temp = temp2;
//            LogUtils.i(TAG, "getBestSize: width- %d, height- %d", temp.width, temp.height);
        } else {
            for (Camera.Size s : sizes) {
//                LogUtils.i(TAG, "support size: width- %d, height- %d", s.width, s.height);
            }
            LogUtils.e(TAG, "getBestSize: is null");
        }
        return temp;
    }

    static final class SortComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            return ((lhs.width * lhs.height < rhs.width * rhs.height) ? 0 : -1);
        }
    }

    /**
     * 处理 zoom 数据
     */
    @Override
    public synchronized void handleZoom(float ratio) {
        if (camera != null) {
            Camera.Parameters params = camera.getParameters();
            if (params.isZoomSupported()) {
                int maxZoom = params.getMaxZoom();
                int zoom = params.getZoom();
                if (Math.abs(ratio) < 0.001 && zoom < maxZoom) {
                    zoom++;
                } else if (zoom > 0) {
                    zoom--;
                }
                params.setZoom(zoom);
                camera.setParameters(params);
            } else {
                LogUtils.e(TAG, "zoom not supported");
            }
        }
    }

    private void setDesiredParameters(boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();

        //noinspection ConstantConditions
        if (parameters == null) {
            LogUtils.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }

        if (safeMode) {
            LogUtils.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }

        if (!safeMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                CameraConfigurationUtils.setVideoStabilization(parameters);
                CameraConfigurationUtils.setFocusArea(parameters);
                CameraConfigurationUtils.setMetering(parameters);
            }
        }

        //设置预览尺寸
        Camera.Size size = getBestSize(parameters.getSupportedPictureSizes());
        if (size == null) {
            parameters.setPreviewSize(preViewWidth, preViewHeight);
        } else {
            parameters.setPreviewSize(size.width, size.height);
            preViewWidth = size.width;
            preViewHeight = size.height;
        }
//        LogUtils.i(TAG, "preview size: w-%d,h-%d", preViewWidth, preViewHeight);
        //设置数据格式
        parameters.setPreviewFormat(ImageFormat.NV21);

        //非 G200，或 G200 手持端支持自动对焦
        if (!Constant.IS_G200 || camId == HAND_CAMERA_ID) {
            //设置自动对焦
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }


        //set camera white balance
        List<String> whiteBalance = parameters.getSupportedWhiteBalance();

        String whiteBalanceValue = "auto";
        boolean findFlag = false;
        for (String str : whiteBalance) {
            LogUtils.i(TAG, " whiteBalanceValue:" + str);

            if (str.contains("auto")) {
                whiteBalanceValue = str;
                findFlag = true;
                LogUtils.i(TAG, "find...");
                break;
            }
        }

        if (findFlag) {
            parameters.setWhiteBalance(whiteBalanceValue);
        }

        // We need to set the FPS on Google Glass devices, otherwise the preview is scrambled.
        // FIXME - can/should we do this for other devices as well?
        CameraConfigurationUtils.setBestPreviewFPS(parameters);

        camera.setParameters(parameters);

        int[] r = {0, 0};
        parameters.getPreviewFpsRange(r);
//        LogUtils.i(TAG, "setCameraParameters range %d,%d", r[0], r[1]);
    }

    /******************************** 相机使用过程中错误  ************************/

    private Camera.ErrorCallback cameraErrorCallback = (error, camera) -> {
//        LogUtils.e(TAG, "cameraErrorCallback onError %d", error);
        if (newFrameListener != null) {
            newFrameListener.onError(error);
        }
    };

}
