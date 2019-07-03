package com.hiscene.flytech.ui;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.weiss.core.UserManager;
import com.github.weiss.core.bus.RxBus;
import com.github.weiss.core.utils.AppUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.ScreenUtils;
import com.github.weiss.core.utils.StringUtils;
import com.github.weiss.core.utils.helper.RxSchedulers;
import com.google.zxing.Result;
import com.hiscene.camera.listener.OnQrRecognizeListener;
import com.hiscene.camera.view.CameraView;
import com.hiscene.camera.vision.QRVision;
import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.C;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.UserModel;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.recorder.ScreenRecorderManager;
import com.hiscene.flytech.ui.fragment.DeviceFragment;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.ui.fragment.LoginFragment;
import com.hiscene.flytech.ui.fragment.ScanDeviceFragment;
import com.hiscene.flytech.ui.fragment.ScanLoginFragment;
import com.hiscene.flytech.ui.fragment.StartEditExcelFragment;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.hiscene.flytech.App.userManager;


public class MainActivity extends BaseActivity implements LoginFragment.LoginScanListener, StartEditExcelFragment.StartEditListener {

    private static final int REQUEST_SCREEN_LIVE = 1;

    private int FLAG = -1;
    public static final int LOGIN = 0;//登录页
    public static final int SCAN_LOGIN = 1;//扫描登录页
    public static final int DEVICE = 2;//主界面页
    public static final int SCAN_DEVICE = 3;//扫描设备页
    public static final int START_EDIT = 4;//开始填写表单页

    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;

    CameraView cameraView;
    CameraRecorder qrVision;
    ScreenRecorderManager screenRecorderManager;
    boolean isLaunchHiLeia = false;

    LoginFragment loginFragment;

    ScanLoginFragment scanLoginFragment;

    DeviceFragment deviceFragment;

    ScanDeviceFragment scanDeviceFragment;

    StartEditExcelFragment startEditExcelFragment;

    ExcelFragmentManager excelFragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        screenRecorderManager = new ScreenRecorderManager(this);
        if(userManager.isLogin()){
            startEditExcelFragment=StartEditExcelFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,startEditExcelFragment).commitNowAllowingStateLoss();
        }else {
            loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, loginFragment).commitNowAllowingStateLoss();
        }
        new Thread(() -> excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager())).start();
        FLAG = LOGIN;
        cameraView = new CameraView(this);
        cameraLayout.addView(cameraView);
        qrVision = new CameraRecorder();
        qrVision.init();
        qrVision.setOnQrRecognizeListener(new OnQrRecognizeListener() {
            @Override
            public boolean OnRecognize(Result result) {
                addRxDestroy(Observable.just("didi")
                        .compose(RxSchedulers.io_main())
                        .subscribe(str -> {
                            LogUtils.d("OnQrRecognizeListener:" + result.getText());
                            String resultStr=result.getText();
                            resultStr="User:508f567cc3015cba395858d4493dd706";
                            String[] user= resultStr.split(":");
                            if(user.length>=1){
                                if("User:508f567cc3015cba395858d4493dd706".equals(resultStr)){
                                    userManager.login(new UserModel(user[1]));
                                    startEditExcelFragment=StartEditExcelFragment.newInstance();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment,startEditExcelFragment).commitNowAllowingStateLoss();
                                }
                            }
                        }, e -> e.printStackTrace()));
//                excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager());
                if(SCAN_LOGIN==FLAG||SCAN_DEVICE==FLAG) return false;
                return true;
            }
        });

    }


    @OnClick(R.id.hileia)
    protected void hileia() {
        if (AppUtils.isInstallApp(this, "com.hiscene.hileia")) {
            screenRecorderManager.startCaptureIntent();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("onStart");
        if (isLaunchHiLeia) {
            LogUtils.d("isLaunchHiLeia");
            isLaunchHiLeia = false;
            screenRecorderManager.cancelRecorder();
            qrVision.init();
//            cameraView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cameraView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLaunchHiLeia) {
            LogUtils.d("isLaunchHiLeia onDestroy");
            isLaunchHiLeia = false;
            screenRecorderManager.cancelRecorder();
        }else {
            qrVision.destroy();
            qrVision.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScreenRecorderManager.REQUEST_MEDIA_PROJECTION) {
            qrVision.destroy();
            LogUtils.d("onActivityResult");
            screenRecorderManager.onActivityResult(requestCode, resultCode, data);
            AppUtils.launchAppForURLScheme(this, "com.hiscene.hileia",
                    "hileia://host:8080/launch?username=15920400762&password=qq137987751");
            cameraLayout.postDelayed(() -> isLaunchHiLeia = true, 800);
        }
    }


    @Override
    public void scanLogin() {
        scanLoginFragment = ScanLoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanLoginFragment).commitNowAllowingStateLoss();
        FLAG = SCAN_LOGIN;
        qrVision.start();
        qrVision.startQRRecognize();
    }


//    @Override
//    public void scanDevice() {
//        scanDeviceFragment = ScanDeviceFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanDeviceFragment).commitNowAllowingStateLoss();
//        FLAG = SCAN_DEVICE;
//        qrVision.startQRRecognize();
//    }

    @Override
    public void startEdit() {
       if(excelFragmentManager!=null){
           excelFragmentManager.init();
       }
    }
}