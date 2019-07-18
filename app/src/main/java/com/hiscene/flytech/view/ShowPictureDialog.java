package com.hiscene.flytech.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.weiss.core.utils.DisplayUtil;
import com.github.weiss.core.utils.ImageLoaderUtil;
import com.github.weiss.core.utils.ToastUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.ShowImagesAdapter;
import com.hiscene.flytech.util.AnimUtils;
import com.lxj.xpopup.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 *查看图片：上下左右滑动,放大缩小
 */

public class ShowPictureDialog extends Dialog{
//    @BindView(R.id.vp_images)
//    ShowImagesViewPager mViewPager;
    @BindView(R.id.pre_picture)
    TextView prePrcture;
    @BindView(R.id.next_picture)
    TextView nextPicture;
    @BindView(R.id.iv_cancel)
    TextView ivCancel;
    @BindView(R.id.left)
    Button left;
    @BindView(R.id.right)
    Button right;
    @BindView(R.id.top)
    Button top;
    @BindView(R.id.bottom)
    Button bottom;
    @BindView(R.id.zoom_in)
    Button zoom_in;
    @BindView(R.id.zoom_out)
    Button zoom_out;

    private View mView;
    private ShowImagesAdapter mAdapter;
    private Context mContext;
    private List<File> fileList;
    private List<String> mTitles;
    private List<View> mViews;
    @BindView(R.id.photoview)
    PhotoView mPhotoView;
    @BindView(R.id.horizontal_scrollview)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    File file;


    public ShowPictureDialog( @NonNull Context context, File file) {
        super(context, R.style.transparentBgDialog);//R.style.transparentBgDialog
        this.mContext = context;
        this.file = file;
        initView();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
    }

    private void initView() {
        mView = View.inflate(mContext, R.layout.dialog_show_image, null);
        ButterKnife.bind(this,mView);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        ImageLoaderUtil.loadImg(mPhotoView,file);
    }

    private void initData() {

    }


    @OnClick(R.id.iv_cancel)
    void cancelDialog() {
        AlphaAnimation alpha = (AlphaAnimation) new AnimUtils().getAnimation(getContext(), R.anim.alpha_1_to_0);
        ivCancel.startAnimation(alpha);
        dismiss();
    }

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

    @OnClick(R.id.left)
    protected void left(){
        scrollView.smoothScrollBy(-20,0);
    }

    @OnClick(R.id.right)
    protected void right(){

    }

    @OnClick(R.id.top)
    protected void top(){

    }

    @OnClick(R.id.bottom)
    protected void bottom(){

    }

    @OnClick(R.id.zoom_in)
    protected void zoom_in(){
        if(mPhotoView.getScale()<mPhotoView.getMediumScale()){
            ToastUtils.show("当前已缩到最小");
        }else if(mPhotoView.getScale()<mPhotoView.getMaximumScale()){
            mPhotoView.setScale(mPhotoView.getMinimumScale());
        }else {
            mPhotoView.setScale(mPhotoView.getMediumScale());
        }
    }

    @OnClick(R.id.zoom_out)
    protected void zoom_out(){
        if(mPhotoView.getScale()<mPhotoView.getMediumScale()){
            mPhotoView.setScale(mPhotoView.getMediumScale());
        }else if(mPhotoView.getScale()<mPhotoView.getMaximumScale()){
            mPhotoView.setScale(mPhotoView.getMaximumScale());
        }else {
            ToastUtils.show("当前已放大最大");
        }
    }
}
