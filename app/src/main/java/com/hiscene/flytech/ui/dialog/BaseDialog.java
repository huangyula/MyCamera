package com.hiscene.flytech.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.weiss.core.utils.DisplayUtil;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.ShowImagesAdapter;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.util.AnimUtils;
import com.hiscene.flytech.view.ShowImagesViewPager;
import com.lxj.xpopup.impl.FullScreenPopupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/5/3.
 * 嵌套了viewpager的图片浏览
 */

public abstract class BaseDialog extends Dialog {
    private View mView;
    public Context mContext;

    public BaseDialog( @NonNull Context context) {
        super(context, R.style.Dialog_Fullscreen);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = View.inflate(mContext, getLayoutId(), null);
        setContentView(mView);
        ButterKnife.bind(this,mView);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    protected void initData() {
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = (int) (DisplayUtil.getScreenWidth(mContext));
        wl.height = (int) (DisplayUtil.getScreenHeight(mContext));
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }

}
