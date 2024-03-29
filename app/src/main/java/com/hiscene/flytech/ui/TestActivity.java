package com.hiscene.flytech.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.github.weiss.core.utils.AppUtils;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.ToastUtils;
import com.github.weiss.core.utils.helper.RxSchedulers;
import com.google.zxing.Result;
import com.hiscene.camera.listener.OnQrRecognizeListener;
import com.hiscene.camera.view.CameraView;
import com.hiscene.camera.vision.QRVision;
import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.UserModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.event.SimpleEventHandler;
import com.hiscene.flytech.lifecycle.IComponentContainer;
import com.hiscene.flytech.lifecycle.LifeCycleComponent;
import com.hiscene.flytech.lifecycle.LifeCycleComponentManager;
import com.hiscene.flytech.recorder.CameraRecorder;
import com.hiscene.flytech.recorder.ScreenRecorderManager;
import com.hiscene.flytech.ui.dialog.ExcelDialogManager;
import com.hiscene.flytech.ui.dialog.LoginDialog;
import com.hiscene.flytech.ui.dialog.ScanLoginDialog;
import com.hiscene.flytech.ui.dialog.StartEditExcelDialog;
import com.hiscene.flytech.util.POIUtil;
import com.hiscene.flytech.util.PositionUtil;
import com.hiscene.flytech.view.CustomProgressDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

import static com.hiscene.flytech.App.userManager;
import static com.hiscene.flytech.ui.dialog.ExcelDialogManager.END_TIME;
import static com.hiscene.flytech.ui.dialog.ExcelDialogManager.EXCEL_END_TIME;
import static com.hiscene.flytech.ui.dialog.ExcelDialogManager.RECOVERY;
import static com.hiscene.flytech.ui.dialog.ExcelDialogManager.START_TIME;


public class TestActivity extends BaseActivity implements IComponentContainer {
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

    public boolean success=false;
    public boolean isClick=false;//是否点击过表单
    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    @BindView(R.id.cameraLayout)
    LinearLayout cameraLayout;

    CameraView cameraView;
    QRVision cameraRecorder;
    ScreenRecorderManager screenRecorderManager;
    boolean isLaunchHiLeia = false;
    boolean isCameraRecord = false;

    LoginDialog loginDialog;

    ScanLoginDialog scanloginDialog;
    
    StartEditExcelDialog startEditExcelDialog;

    ExcelDialogManager excelDialogManager;
    
//    LoadingPopupView xLoadExcelPopup;
//
//    LoadingPopupView xExportExcelPopup;

    CustomProgressDialog progressDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EventCenter.bindContainerAndHandler(this, mEventHandler);
        screenRecorderManager = new ScreenRecorderManager(this);
        progressDialog=new CustomProgressDialog(this,R.style.transparentBgDialog);
        if(userManager.isLogin()){
            startEditExcelDialog=StartEditExcelDialog.newInstance(this);
            startEditExcelDialog.show();
        }else {
            loginDialog = LoginDialog.newInstance(this);
            loginDialog.show();
        }
        new Thread(() -> excelDialogManager = new ExcelDialogManager(this)).start();
        FLAG = LOGIN;
        cameraView = new CameraView(this);
        cameraLayout.addView(cameraView);
        cameraView.setVisibility(View.GONE);
        cameraRecorder = new QRVision();
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
                                    if(scanloginDialog.isShowing()){
                                        scanloginDialog.dismiss();
                                    }
                                    startEditExcelDialog=StartEditExcelDialog.newInstance(TestActivity.this);
                                    startEditExcelDialog.show();
                                }
                            }
                        }, e -> e.printStackTrace()));
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
//            cameraRecorder.init();
            screenRecorderManager.startCaptureIntent();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
        mComponentContainer.onBecomesPartiallyInvisible();
//        cameraView.pause();
        if (isCameraRecord) {
            screenRecorderManager.cancelRecorder();
//            cameraRecorder.destroy();
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
        boolean result;
        if (isLaunchHiLeia) {
            LogUtils.d("isLaunchHiLeia onDestroy");
            isLaunchHiLeia = false;
//            screenRecorderManager.cancelRecorder();
        } else {
//            cameraRecorder.destroy();
            cameraRecorder.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScreenRecorderManager.REQUEST_MEDIA_PROJECTION) {
            screenRecorderManager.onActivityResult(requestCode, resultCode, data);
            isCameraRecord = true;
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
        scanloginDialog = ScanLoginDialog.newInstance(this);
        scanloginDialog.show();
        FLAG = SCAN_LOGIN;
        cameraRecorder.startQRRecognize();
        cameraView.setVisibility(View.VISIBLE);
    }

    public void Login(){
        if(loginDialog==null){
            loginDialog=LoginDialog.newInstance(this);
        }
        loginDialog.show();
    }


//    @Override
//    public void scanDevice() {
//        scanDeviceFragment = ScanDeviceFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, scanDeviceFragment).commitNowAllowingStateLoss();
//        FLAG = SCAN_DEVICE;
//        cameraRecorder.startQRRecognize();
//    }

    public void startEdit() {
        if (excelDialogManager != null) {

            excelDialogManager.init();

            startEditExcelDialog.dismiss();
            cameraView.setVisibility(View.VISIBLE);
            screenRecorderManager.startCaptureIntent();
//          cameraRecorder.init();
            isClick=false;
       }else {
            isClick=true;
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
                    screenRecorderManager.cancelRecorder();
                    LogUtils.d("正在生成文件中");
                    if(progressDialog==null){
                        progressDialog=new CustomProgressDialog(TestActivity.this,R.style.transparentBgDialog);
                    }
                    showProgress("正在生成文件中,请稍后");
//                    xExportExcelPopup=new XPopup.Builder(TestActivity.this)
//                            .asLoading("正在生成文件中...");
//                    xExportExcelPopup.show();
                    break;
                case C.CONTINUE_EDIT:

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
                    hideProgress();
                    SPUtils.put(RECOVERY,true);
                    LogUtils.d("文件写入数据出错："+result.msg);
                    break;
                case C.EXCEL_WRITE_SUCCESS:
                    excelDialogManager.reset();
                    excelDialogManager=null;
                    new Thread(() -> excelDialogManager = new ExcelDialogManager(TestActivity.this)).start();

                    hideProgress();
//                    if(xExportExcelPopup!=null){
//                        xExportExcelPopup.dismiss();
//                    }
                    ToastUtils.show("表单已保存至"+"/"+getString(R.string.app_name)+"/export/"+SPUtils.getString(EXCEL_END_TIME)+"/文件夹",5000);
                    if(startEditExcelDialog==null){
                        startEditExcelDialog=StartEditExcelDialog.newInstance(TestActivity.this);
                    }
                    startEditExcelDialog.show();
                    break;
                case C.RESTART_EDIT://准备重新打开表单,重新读取数据
                    showProgress("正在重新加载表单,请稍后...");
//                    xLoadExcelPopup=new XPopup.Builder(TestActivity.this)
//                            .asLoading("正在重新加载表单,请稍后...");
//                    xLoadExcelPopup.show();
                    excelDialogManager.restart();
                    break;
                case C.RESTART_EXCEL://重新打开表单成功
                    hideProgress();
//                    if(xLoadExcelPopup!=null){
//                        xLoadExcelPopup.dismiss();
//                    }
//                    if(xExportExcelPopup!=null&&xExportExcelPopup.isShow()){
//                        xExportExcelPopup.dismiss();
//                    }
                    excelDialogManager.reset();
                    excelDialogManager.init();
                    success=false;
                    break;
                case C.SETTING_ERROR:
                    ToastUtils.show(result.msg);
                    finish();
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            LogUtils.d("activity back");
            finish();
        }
        return super.dispatchKeyEvent(event);
    }

    public void showProgress( String message ){
        if(progressDialog==null){
            progressDialog=new CustomProgressDialog(this,R.style.transparentBgDialog);
        }
        progressDialog.setMessage(message);
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    private void hideProgress(){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}