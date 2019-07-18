package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.ToastUtils;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.FileSelectorAdapter;
import com.hiscene.flytech.ui.PictureActivity;
import com.hiscene.flytech.view.ShowImagesDialog;
import com.hiscene.flytech.view.ShowPictureDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 无设备资料提醒框
 */
public class FileSeletorDialog extends BaseDialog{
    @BindView(R.id.close)
    TextView close;
    @BindView(R.id.back_to_dir)
    TextView back_to_dir;
    @BindView(R.id.gridview)
    GridView gridView;
    FileSelectorAdapter fileSelectorAdapter;
    List<File> fileList;
    public FileSeletorDialog( @NonNull Context context ) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_file_selector;
    }

    @Override
    protected void initView() {
        fileList = FileUtils.listFilesInDir(C.FILE_DEVICE_FILE,false);
        fileSelectorAdapter=new FileSelectorAdapter(mContext,fileList);
        gridView.setAdapter(fileSelectorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                File file = fileList.get(i);
                // 如果是目录 进入目录
                if (file.isDirectory()) {
                    back_to_dir.setVisibility(View.VISIBLE);
                    fileList=FileUtils.listFilesInDir(file,false);
                    if(fileList!=null&&fileList.size()>0){
                        fileSelectorAdapter=new FileSelectorAdapter(mContext,fileList);
                        gridView.setAdapter(fileSelectorAdapter);
                    }else {
                        ToastUtils.show("该文件夹下无文件");
                    }
                } else {
                    // 打开文件
                    if(file.getName().endsWith("pdf")){
                        new ShowPdfDialog(mContext,file).show();
                    }else if(file.getName().endsWith("png")||file.getName().endsWith("jpg")){
                        new ShowPictureDialog(mContext,file).show();
                    }else {
                        ToastUtils.show("不支持打开此类型文件！");
                    }
                }
            }
        });


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

    @OnClick(R.id.back_to_dir)
    protected void backToDir(){
        if(!CollectionUtils.isEmpty(fileList)){
            File parentFile=fileList.get(0).getParentFile();
            if(parentFile!=null){
                if(parentFile.getParentFile().getName().equals("info")) {
                    back_to_dir.setVisibility(View.GONE);
                }
                fileList=FileUtils.listFilesInDir(parentFile.getParent(),false);
                fileSelectorAdapter=new FileSelectorAdapter(mContext,fileList);
                gridView.setAdapter(fileSelectorAdapter);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        back_to_dir.setVisibility(View.GONE);
    }
}
