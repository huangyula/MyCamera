package com.hiscene.flytech.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.github.weiss.core.base.BaseFragment;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.flytech.R;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class ScanDeviceFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener{

    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    @BindView(R.id.scan_line)
    ImageView scan_line;
    boolean flag=true;
    public static ScanDeviceFragment newInstance() {
        ScanDeviceFragment scanDeviceFragment = new ScanDeviceFragment();
        return scanDeviceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scan_device;
    }

    @Override
    protected void initView() {
        iv_scan.getViewTreeObserver().addOnGlobalLayoutListener(this::onGlobalLayout);
        scan_line.getViewTreeObserver().addOnGlobalLayoutListener(this::onGlobalLayout);
    }

    @Override
    public void onGlobalLayout() {
        if(flag){
            flag=false;
            int height= iv_scan.getHeight()-scan_line.getHeight()*12;
            LogUtils.d("height:"+height);
            iv_scan.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            scan_line.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            TranslateAnimation translateAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,height);
            translateAnimation.setDuration(3000);
            translateAnimation.setRepeatCount(Animation.INFINITE);
            translateAnimation.setRepeatMode(Animation.REVERSE);
            scan_line.startAnimation(translateAnimation);
        }
    }
}
