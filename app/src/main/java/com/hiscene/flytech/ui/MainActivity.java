package com.hiscene.flytech.ui;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.weiss.core.UserManager;
import com.github.weiss.core.bus.RxBus;

import com.github.weiss.core.utils.AppUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.ScreenUtils;
import com.github.weiss.core.utils.StringUtils;
import com.github.weiss.core.utils.ToastUtils;
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
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.event.SimpleEventHandler;
import com.hiscene.flytech.excel.ProcessExcel;
import com.hiscene.flytech.lifecycle.IComponentContainer;
import com.hiscene.flytech.lifecycle.LifeCycleComponent;
import com.hiscene.flytech.lifecycle.LifeCycleComponentManager;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.recorder.ScreenRecorderManager;
import com.hiscene.flytech.ui.fragment.DeviceFragment;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.ui.fragment.LoginFragment;
import com.hiscene.flytech.ui.fragment.ScanDeviceFragment;
import com.hiscene.flytech.ui.fragment.ScanLoginFragment;
import com.hiscene.flytech.ui.fragment.StartEditExcelFragment;
import com.hiscene.flytech.util.PositionUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

import static com.github.weiss.core.utils.Utils.getContext;
import static com.hiscene.flytech.App.userManager;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.RECOVERY;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;


public class MainActivity extends BaseActivity implements IComponentContainer {
    private static final int REQUEST_SCREEN_LIVE = 1;

    private int FLAG = -1;
    public static final int LOGIN = 0;//登录页
    public static final int SCAN_LOGIN = 1;//扫描登录页
    public static final int DEVICE = 2;//主界面页
    public static final int SCAN_DEVICE = 3;//扫描设备页
    public static final int START_EDIT = 4;//开始填写表单页

    public static final String BACK_TO_LOGIN ="BACK_TO_LOGIN" ;
    public static final String START_EDIT_EXCEL ="START_EDIT_EXCEL" ;
    public static final String START_SCAN_LOGIN ="START_SCAN_LOGIN" ;
    public static final String HILEIA="HILEIA";

    public boolean success=true;
    public boolean isClick=false;//是否点击过表单
    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;

    CameraView cameraView;
    CameraRecorder cameraRecorder;
    //    ScreenRecorderManager screenRecorderManager;
    boolean isLaunchHiLeia = false;
    boolean isCameraRecord = false;

    LoginFragment loginFragment;

    ScanLoginFragment scanLoginFragment;

    DeviceFragment deviceFragment;

    ScanDeviceFragment scanDeviceFragment;

    StartEditExcelFragment startEditExcelFragment;

    ExcelFragmentManager excelFragmentManager;

    LoadingPopupView xPopup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EventCenter.bindContainerAndHandler(this, mEventHandler);
//        screenRecorderManager = new ScreenRecorderManager(this);
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
        cameraView.setVisibility(View.GONE);
        cameraRecorder = new CameraRecorder();
        cameraRecorder.setOnQrRecognizeListener(new OnQrRecognizeListener() {
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
                                    cameraView.setVisibility(View.GONE);
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
        cameraRecorder.start();

    }

    public void hileia() {
        if (AppUtils.isInstallApp(this, "com.hiscene.hileia")) {
            launchHileia();
//            screenRecorderManager.startCaptureIntent();
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
        mComponentContainer.onBecomesVisibleFromPartiallyInvisible();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("onStart");
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
        if (isLaunchHiLeia) {
            LogUtils.d("isLaunchHiLeia");
            isLaunchHiLeia = false;
//            screenRecorderManager.cancelRecorder();
//            cameraView.resume();
        }
        if (isCameraRecord) {
            cameraRecorder.init();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
        mComponentContainer.onBecomesPartiallyInvisible();
//        cameraView.pause();
        if (isCameraRecord) {
            cameraRecorder.destroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComponentContainer.onDestroy();
//        //需要恢复,true :
//        excelFragmentManager.laststep=false;
//        //不需要恢复
//        excelFragmentManager.laststep=true && success=true
        SPUtils.put(RECOVERY, excelFragmentManager.laststep);
        SPUtils.put(PositionUtil.POSITION, excelFragmentManager.pos);

        if (isLaunchHiLeia) {
            LogUtils.d("isLaunchHiLeia onDestroy");
            isLaunchHiLeia = false;
//            screenRecorderManager.cancelRecorder();
        } else {
            cameraRecorder.destroy();
            cameraRecorder.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScreenRecorderManager.REQUEST_MEDIA_PROJECTION) {
//            screenRecorderManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void launchHileia() {
//        cameraRecorder.destroy();
        LogUtils.d("onActivityResult");
        AppUtils.launchAppForURLScheme(this, "com.hiscene.hileia",
                "hileia://host:8080/launch?username=15920400762&password=qq137987751");
        isLaunchHiLeia = true;
//        cameraLayout.postDelayed(() -> isLaunchHiLeia = true, 800);
    }

    public void scanLogin() {
        scanLoginFragment = ScanLoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanLoginFragment).commitNowAllowingStateLoss();
        FLAG = SCAN_LOGIN;
        cameraRecorder.startQRRecognize();
        cameraView.setVisibility(View.VISIBLE);
    }

    public void Login(){
        if(loginFragment==null){
            loginFragment=LoginFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, loginFragment).commit();
    }


//    @Override
//    public void scanDevice() {
//        scanDeviceFragment = ScanDeviceFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanDeviceFragment).commitNowAllowingStateLoss();
//        FLAG = SCAN_DEVICE;
//        cameraRecorder.startQRRecognize();
//    }

    public void startEdit() {
        isClick=true;
        if (excelFragmentManager != null) {
         excelFragmentManager.init();
        cameraView.setVisibility(View.VISIBLE);
        cameraRecorder.init();
        isCameraRecord = true;
        isClick=false;
       }else {
           ToastUtils.show("正在加载表格中,请稍后");
       }
    }

    private SimpleEventHandler mEventHandler = new SimpleEventHandler() {
        @Subscribe
        public void onEvent(String code) {
            LogUtils.d("code:"+code);
            switch (code){
                case BACK_TO_LOGIN:
                    Login();
                    break;
                case START_EDIT_EXCEL:
                    startEdit();
                    break;
                case START_SCAN_LOGIN:
                    scanLogin();
                    break;
                case HILEIA:
                    hileia();
                    break;
                case C.LOADING:
                    xPopup=new XPopup.Builder(MainActivity.this)
                            .asLoading("正在生成文件中...");
                    xPopup.show();
                    break;
            }
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void  onEvent( com.hiscene.flytech.entity.Result result ){
            switch (result.code){
                case C.EXCEL_LOADED:
                    if(isClick)
                        startEdit();
                    break;
                case C.EXCEL_WRITE_ERROR:
                    success=false;
                    if(xPopup!=null){
                        xPopup.dismiss();
                    }
                    LogUtils.d("文件写入数据出错："+result.msg);
                    break;
                case C.EXCEL_WRITE_SUCCESS:
                    if(xPopup!=null){
                        xPopup.dismiss();
                    }
                    ToastUtils.show("当前已经表单操作的最后一步,文件已经生成成功,请在  /中调定检助手/output/ 中查看文件",4000);
                    success=true;
                   // 退出应用,清除缓存数据,recovery,position,表单填写时间需要重置
//                    new XPopup.Builder(MainActivity.this).asConfirm("导出文件成功", "导出文件成功,是否结束本次表单填写？",
//                            new OnConfirmListener() {
//                                @Override
//                                public void onConfirm() {
//                                    recovery=false;
//                                    SPUtils.put(START_TIME,"");
//                                    //删除temp目录下所有的.json文件
////                                    FileUtils.deleteFilesInDir(C.TEMP_PATH);
//                                    finish();
//                                }
//                            }).show();
                    break;
            }
        }

    };


    @Override
    public void addComponent( LifeCycleComponent component ) {
        mComponentContainer.addComponent(component);
    }

    public void removeAllFragments(){
        List<Fragment>  fragments=getSupportFragmentManager().getFragments();
        for (Fragment fragment:fragments){
            getSupportFragmentManager().beginTransaction().remove(fragment);
        }
    }
}