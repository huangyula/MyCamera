package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.github.weiss.core.base.BaseRxFragment;
import com.github.weiss.core.utils.FileUtils;
import com.github.weiss.core.utils.ImageUtils;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.TestActivity;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.view.CustomAlertDialog;
import com.hiscene.flytech.view.CustomProgressDialog;
import com.hiscene.flytech.view.ShowImagesDialog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.HILEIA;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 根据当前ExcelStep的ExcelStyle显示不同的ExcelFragment
 */
public abstract class BaseExcelDialog<T> extends BaseDialog {

    @BindView(R.id.previousStepBtn)
    Button previousStepBtn;
    @BindView(R.id.nextStepBtn)
    Button nextStepBtn;
    @BindView(R.id.opration_risk)
    TextView oprationRisk;
    @BindView(R.id.device_info)
    TextView deviceInfo;
    @BindView(R.id.rate)
    TextView rate;
    @BindView(R.id.executed)
    @Nullable
    Button executed;
    @BindView(R.id.unexecuted)
    @Nullable
    Button unexecuted;
    @BindView(R.id.logout)
    TextView logout;
    protected ExcelDialogManager excelDialogManager;


    public BaseExcelDialog( Context context,ExcelDialogManager excelDialogManager) {
        super(context);
        this.excelDialogManager = excelDialogManager;
    }

    /**
     * 设置当前表格类型数据模型
     *
     * @param data
     * @param attachSecondModel
     */
    /**
     * 设置当前表格类型数据模型
     *
     * @param data
     */
    public abstract void setData(T data);

    @OnClick(R.id.previousStepBtn)
    protected abstract void previousStep();

    @OnClick(R.id.nextStepBtn)
    protected abstract void nextStep();

    @OnClick(R.id.opration_risk)
    protected void oprationRisk(){
        EventCenter.getInstance().post(HILEIA);
    }


    @OnClick(R.id.logout)
    protected abstract void logout();

    @OnClick(R.id.device_info)
    protected void deviceInfo(){
        CustomProgressDialog customProgressDialog=new CustomProgressDialog(mContext,R.style.transparentBgDialog);
        customProgressDialog.setMessage("正在加载设备资料,请稍后");
        customProgressDialog.show();
//
        List<File> fileList=FileUtils.listFilesInDir(new File(C.FILE_DEVICE_FILE));
        Collections.sort(fileList, new FileComparator());
//
        ShowImagesDialog showImagesDialog=new ShowImagesDialog(mContext,fileList);
        customProgressDialog.dismiss();
        showImagesDialog.show();

    }

    protected  void  init(){
    }

    /** * 将文件按名字降序排列 */
    class FileComparator implements Comparator<File> {
        @Override
        public int compare( File file1, File file2)
        {
            return file1.getName().compareTo(file2.getName());
        }
    }

}
