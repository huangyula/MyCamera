package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.ToastUtils;
import com.hiscene.flytech.R;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * pdf浏览框
 */
public class ShowPdfDialog extends BaseDialog{
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.pdfView)
    PDFView pdfView;
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
    File file;
    public ShowPdfDialog( @NonNull Context context, File file ) {
        super(context);
        this.file=file;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_show_pdf;
    }

    @Override
    protected void initView() {
        pdfView.fromFile(file)
                .enableSwipe(true)
//                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }

                })
                //设置翻页监听
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                    }

                })
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        return false;
                    }
                })
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(0)
                .pageFling(true)
                .load();
    }

    @Override
    protected void initData() {
        this.setCancelable(false);
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey( DialogInterface dialogInterface, int i, KeyEvent keyEvent ) {
                if(i == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.close)
    protected void close(){
        dismiss();
    }


    @OnClick(R.id.left)
    protected void left(){
        pdfView.moveRelativeTo(pdfView.getWidth()/2,0);
    }

    @OnClick(R.id.right)
    protected void right(){
        pdfView.moveRelativeTo(-(pdfView.getWidth()/2),0);
    }

    @OnClick(R.id.top)
    protected void top(){
        pdfView.setPositionOffset((pdfView.getPositionOffset()-(1f/pdfView.getPageCount())*0.5f));
    }

    @OnClick(R.id.bottom)
    protected void bottom(){
        pdfView.setPositionOffset((pdfView.getPositionOffset()+(1f/pdfView.getPageCount())*0.5f));
    }

    @OnClick(R.id.zoom_in)
    protected void zoom_in(){
        if(pdfView.getZoom()<pdfView.getMidZoom()){
            ToastUtils.show("当前已经缩到最小");
        }else if(pdfView.getZoom()<pdfView.getMaxZoom()){
            pdfView.zoomWithAnimation(pdfView.getWidth()/2,pdfView.getHeight()/2,pdfView.getMinZoom());
        }else {
            pdfView.zoomWithAnimation(pdfView.getWidth()/2,pdfView.getHeight()/2,pdfView.getMidZoom());
        }
    }

    @OnClick(R.id.zoom_out)
    protected void zoom_out(){
        if(pdfView.getZoom()<pdfView.getMidZoom()){
            pdfView.zoomWithAnimation(pdfView.getWidth()/2,pdfView.getHeight()/2,pdfView.getMidZoom());
        }else if(pdfView.getZoom()<pdfView.getMaxZoom()){
            pdfView.zoomWithAnimation(pdfView.getWidth()/2,pdfView.getHeight()/2,pdfView.getMaxZoom());
        }else {
            ToastUtils.show("当前已经放到最大");
        }
    }

}
