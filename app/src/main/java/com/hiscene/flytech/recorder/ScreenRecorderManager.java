package com.hiscene.flytech.recorder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaCodecInfo;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.github.weiss.core.utils.ScreenUtils;
import com.github.weiss.core.utils.ToastUtils;
import com.hiscene.flytech.App;
import com.hiscene.flytech.C;
import com.hiscene.screenrecorder.AudioEncodeConfig;
import com.hiscene.screenrecorder.Notifications;
import com.hiscene.screenrecorder.ScreenRecorder;
import com.hiscene.screenrecorder.Utils;
import com.hiscene.screenrecorder.VideoEncodeConfig;

import java.io.File;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static com.hiscene.screenrecorder.Notifications.ACTION_STOP;
import static com.hiscene.screenrecorder.ScreenRecorder.AUDIO_AAC;
import static com.hiscene.screenrecorder.ScreenRecorder.VIDEO_AVC;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/26
 * @des
 */
public class ScreenRecorderManager {
    private MediaProjectionManager mMediaProjectionManager;
    public static final int REQUEST_MEDIA_PROJECTION = 998;
    private static final int REQUEST_PERMISSIONS = 2;
    private MediaCodecInfo[] mAvcCodecInfos; // avc codecs
    private MediaCodecInfo[] mAacCodecInfos; // aac codecs
    private Notifications mNotifications;
    /**
     * <b>NOTE:</b>
     * {@code ScreenRecorder} should run in background Service
     * instead of a foreground Activity in this demonstrate.
     */
    private ScreenRecorder mRecorder;
    private boolean audioEnabled = false;
    private boolean isLandscape = true;
    private Activity activity;
    // I-frames
    private static final int IFRAME_INTERVAL = 5; // 10 between
    private int fps = 30;
    private int bitRate = 800000;

    public ScreenRecorderManager(Activity activity) {
        this.activity = activity;
        mMediaProjectionManager = (MediaProjectionManager) App.getAppContext().getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        mNotifications = new Notifications(App.getAppContext());
    }

    private ScreenRecorder newRecorder(MediaProjection mediaProjection, VideoEncodeConfig video,
                                       AudioEncodeConfig audio, File output) {
        ScreenRecorder r = new ScreenRecorder(video, audio,
                1, mediaProjection, output.getAbsolutePath());
        r.setCallback(new ScreenRecorder.Callback() {
            long startTime = 0;

            @Override
            public void onStop(Throwable error) {
                activity.runOnUiThread(() -> stopRecorder());
                if (error != null) {
                    ToastUtils.show("Recorder error ! See logcat for more details");
                    error.printStackTrace();
                    output.delete();
                } else {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            .addCategory(Intent.CATEGORY_DEFAULT)
                            .setData(Uri.fromFile(output));
                    activity.sendBroadcast(intent);
                }
            }

            @Override
            public void onStart() {
                mNotifications.recording(0);
            }

            @Override
            public void onRecording(long presentationTimeUs) {
                if (startTime <= 0) {
                    startTime = presentationTimeUs;
                }
                long time = (presentationTimeUs - startTime) / 1000;
                mNotifications.recording(time);
            }
        });
        return r;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            // NOTE: Should pass this result data into a Service to run ScreenRecorder.
            // The following codes are merely exemplary.

            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                Log.e("@@", "media projection is null");
                return;
            }

            VideoEncodeConfig video = createVideoConfig();
            AudioEncodeConfig audio = createAudioConfig(); // audio can be null
            if (video == null) {
                ToastUtils.show("Create ScreenRecorder failure");
                mediaProjection.stop();
                return;
            }

            File dir = getSavingDir();
            if (!dir.exists() && !dir.mkdirs()) {
                cancelRecorder();
                return;
            }
            final File file = new File(RecorderUtils.getScreenRecorderFilePath());
            Log.d("@@", "Create recorder with :" + video + " \n " + audio + "\n " + file);
            mRecorder = newRecorder(mediaProjection, video, audio, file);
            if (hasPermissions()) {
                startRecorder();
            } else {
                cancelRecorder();
            }
        }
    }

    private void startRecorder() {
        if (mRecorder == null) return;
        mRecorder.start();
//        mButton.setText("Stop Recorder");
        activity.registerReceiver(mStopActionReceiver, new IntentFilter(ACTION_STOP));
//        activity.moveTaskToBack(true);
    }

    private void stopRecorder() {
        mNotifications.clear();
        if (mRecorder != null) {
            mRecorder.quit();
        }
        mRecorder = null;
//        mButton.setText("Restart recorder");
        try {
            activity.unregisterReceiver(mStopActionReceiver);
        } catch (Exception e) {
            //ignored
        }
    }

    public void cancelRecorder() {
        if (mRecorder == null) return;
        ToastUtils.show("Permission denied! Screen recorder is cancel");
        stopRecorder();
    }

    public void startCaptureIntent() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        activity.startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
    }

    private void onDestroy() {
        stopRecorder();
    }

    private AudioEncodeConfig createAudioConfig() {
        if (!audioEnabled) return null;
        String codec = Utils.selectCodec(AUDIO_AAC).getName();
        if (codec == null) {
            return null;
        }
        int bitrate = 80000;
        int samplerate = 48000;
        int channelCount = 1;
        int profile = 0;

        return new AudioEncodeConfig(codec, AUDIO_AAC, bitrate, samplerate, channelCount, profile);
    }

    private VideoEncodeConfig createVideoConfig() {
        String codec = Utils.selectCodec(VIDEO_AVC).getName();
        if (codec == null) {
            // no selected codec ??
            return null;
        }
        int width = ScreenUtils.getScreenWidth();
        int height = ScreenUtils.getScreenHeight();
        int framerate = fps;
        int iframe = IFRAME_INTERVAL;
        int bitrate = bitRate;
        MediaCodecInfo.CodecProfileLevel profileLevel = null;
        return new VideoEncodeConfig(width, height, bitrate,
                framerate, iframe, codec, VIDEO_AVC, profileLevel);
    }

    private static File getSavingDir() {
        return new File( C.TEMP_PATH+"Recorder"+File.separator);
/*        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                "ScreenCaptures");*/
    }

    private boolean hasPermissions() {
        PackageManager pm = activity.getPackageManager();
        String packageName = activity.getPackageName();
        int granted = (audioEnabled ? pm.checkPermission(RECORD_AUDIO, packageName) : PackageManager.PERMISSION_GRANTED)
                | pm.checkPermission(WRITE_EXTERNAL_STORAGE, packageName);
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    private MediaCodecInfo getVideoCodecInfo(String codecName) {
        if (codecName == null) return null;
        if (mAvcCodecInfos == null) {
            mAvcCodecInfos = Utils.findEncodersByType(VIDEO_AVC);
        }
        MediaCodecInfo codec = null;
        for (int i = 0; i < mAvcCodecInfos.length; i++) {
            MediaCodecInfo info = mAvcCodecInfos[i];
            if (info.getName().equals(codecName)) {
                codec = info;
                break;
            }
        }
        if (codec == null) return null;
        return codec;
    }

    private MediaCodecInfo getAudioCodecInfo(String codecName) {
        if (codecName == null) return null;
        if (mAacCodecInfos == null) {
            mAacCodecInfos = Utils.findEncodersByType(AUDIO_AAC);
        }
        MediaCodecInfo codec = null;
        for (int i = 0; i < mAacCodecInfos.length; i++) {
            MediaCodecInfo info = mAacCodecInfos[i];
            if (info.getName().equals(codecName)) {
                codec = info;
                break;
            }
        }
        if (codec == null) return null;
        return codec;
    }

    private BroadcastReceiver mStopActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            File file = new File(mRecorder.getSavedPath());
            if (ACTION_STOP.equals(intent.getAction())) {
                stopRecorder();
            }
            Toast.makeText(context, "Recorder stopped!\n Saved file " + file, Toast.LENGTH_LONG).show();
            StrictMode.VmPolicy vmPolicy = StrictMode.getVmPolicy();
            try {
                // disable detecting FileUriExposure on public file
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
                viewResult(file);
            } finally {
                StrictMode.setVmPolicy(vmPolicy);
            }
        }

        private void viewResult(File file) {
            Intent view = new Intent(Intent.ACTION_VIEW);
            view.addCategory(Intent.CATEGORY_DEFAULT);
            view.setDataAndType(Uri.fromFile(file), VIDEO_AVC);
            view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                activity.startActivity(view);
            } catch (ActivityNotFoundException e) {
                // no activity can open this video
            }
        }
    };
}
