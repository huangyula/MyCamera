package com.hiscene.flytech.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.flyco.systembar.SystemBarHelper;
import com.github.weiss.core.utils.AssetsUtils;
import com.github.weiss.core.utils.FileUtils;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;

import static com.hiscene.flytech.C.FILE_DEVICE_FILE;

/**
 * author weiss
 * email kleinminamo@gmail.com
 * created 2017/12/15.
 */
public class SplashActivity extends EasyPermissionsActivity {

    @Override
    protected int getLayoutId() {
        //隐藏标题栏以及状态栏
/*        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        *//**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**//*
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        /**
         * In order to work around problems with finding a suitable XML Parser,
         * currently the following system properties need to be set manually during startup of your application
         */
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        SystemBarHelper.immersiveStatusBar(this);
        FileUtils.createOrExistsDir(FILE_DEVICE_FILE);
    }

    @Override
    public void startMainActivity() {
        new Thread(() -> {
            AssetsUtils.copyAssetsToDst(SplashActivity.this, "res", C.ASSETS_PATH);
            handler.sendEmptyMessageDelayed(0, 1500);
        }).start();
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(0);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };

    public void getHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
