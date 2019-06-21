package com.hiscene.flytech.ui;

import android.widget.LinearLayout;

import com.github.weiss.core.utils.LogUtils;
import com.google.zxing.Result;
import com.hiscene.camera.listener.OnQrRecognizeListener;
import com.hiscene.camera.view.CameraView;
import com.hiscene.camera.vision.QRVision;
import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.C;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.ui.fragment.DeviceFragment;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.ui.fragment.LoginFragment;
import com.hiscene.flytech.ui.fragment.ScanDeviceFragment;
import com.hiscene.flytech.ui.fragment.ScanLoginFragment;

import java.util.logging.Handler;

import butterknife.BindView;

import static com.github.weiss.core.base.BaseFragment.DEVICE;
import static com.github.weiss.core.base.BaseFragment.FLAG;
import static com.github.weiss.core.base.BaseFragment.LOGIN;
import static com.github.weiss.core.base.BaseFragment.SCAN_DEVICE;
import static com.github.weiss.core.base.BaseFragment.SCAN_LOGIN;

public class MainActivity extends BaseActivity {
    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;

    CameraView cameraView;
    CameraRecorder qrVision;

    LoginFragment loginFragment;

    ScanLoginFragment scanLoginFragment;

    DeviceFragment deviceFragment;

    ScanDeviceFragment scanDeviceFragment;

    ExcelFragmentManager excelFragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        loginFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, loginFragment).commit();
        cameraView = new CameraView(this);
        cameraLayout.addView(cameraView);
        qrVision = new CameraRecorder();
        qrVision.init(C.TEMP_PATH+"test.mp4");
        qrVision.start();
        qrVision.setOnQrRecognizeListener(new OnQrRecognizeListener() {
            @Override
            public boolean OnRecognize(Result result) {
                LogUtils.d("OnQrRecognizeListener:" + result.getText());
//                if(FLAG==LOGIN){
//                    scanLoginFragment = ScanLoginFragment.newInstance();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanLoginFragment).commit();
//                }else if(FLAG==SCAN_LOGIN){
//                    deviceFragment = DeviceFragment.newInstance();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, deviceFragment).commit();
//                }else if(FLAG==DEVICE){
//                    scanDeviceFragment = ScanDeviceFragment.newInstance();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanDeviceFragment).commit();
//                } else if(FLAG==SCAN_DEVICE) {
//                    excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager());
//                }
                excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager());
                return true;
            }
        });
        qrVision.startQRRecognize();
    }

    @Override
    public void finish() {
        super.finish();
        qrVision.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}