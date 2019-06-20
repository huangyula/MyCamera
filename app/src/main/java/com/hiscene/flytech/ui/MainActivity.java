package com.hiscene.flytech.ui;

import android.widget.LinearLayout;

import com.github.weiss.core.utils.LogUtils;
import com.google.zxing.Result;
import com.hiscene.camera.listener.OnQrRecognizeListener;
import com.hiscene.camera.view.CameraView;
import com.hiscene.camera.vision.QRVision;
import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.R;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.ui.fragment.LoginFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;

    CameraView cameraView;
    QRVision qrVision;

    LoginFragment loginFragment;

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
        qrVision = new QRVision();
        qrVision.start();
        qrVision.setOnQrRecognizeListener(new OnQrRecognizeListener() {
            @Override
            public boolean OnRecognize(Result result) {
                LogUtils.d("OnQrRecognizeListener:" + result.getText());
                excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager());
                return true;
            }
        });
        qrVision.startQRRecognize();
    }

}