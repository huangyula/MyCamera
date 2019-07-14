package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.github.weiss.core.base.BaseFragment;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.camera.qr.Intents;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;

import butterknife.BindView;

public class ScanLoginDialog extends BaseDialog implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    @BindView(R.id.scan_line)
    ImageView scan_line;
    boolean flag=true;

    public ScanLoginDialog( @NonNull Context context ) {
        super(context);
    }

    public static ScanLoginDialog newInstance(Context context) {
        ScanLoginDialog scanLoginFragment = new ScanLoginDialog(context);
        return scanLoginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scan_login;
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
