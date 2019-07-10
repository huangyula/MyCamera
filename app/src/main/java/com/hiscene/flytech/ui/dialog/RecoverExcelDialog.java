package com.hiscene.flytech.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.ui.fragment.BaseExcelFragment;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huangyu
 * @des 二次措施单执行
 */
@SuppressLint("ValidFragment")
public class RecoverExcelDialog extends BaseExcelDialog<ExecuteModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.standard)
    TextView standard;

    ExecuteModel data;
    List<ExecuteModel> dataList;
    int pos=0;
    ExcelStep excelStep;

    public RecoverExcelDialog( Context context, ExcelDialogManager excelDialogManager ) {
        super(context, excelDialogManager);
    }


    public static RecoverExcelDialog newInstance(Context context, ExcelDialogManager excelDialogManager) {
        RecoverExcelDialog recoverExcelDialog=new RecoverExcelDialog(context,excelDialogManager);
        return recoverExcelDialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recover;
    }


    @Override
    protected void initView() {
        if (dataList != null) {//第一次初始化setData还没Attach Activity
            initData(dataList,excelStep,pos);
        }
    }

    @Override
    public void setData(ExecuteModel data) {
        if (title != null) {
            initData(dataList,excelStep,pos);
        } else {
            this.data = data;
        }
    }


    private void initData(List<ExecuteModel> dataList,ExcelStep excelStep,int pos) {
        if(dataList.get(excelStep.childSteps.get(pos).step).recover_result==1){
            executed.setSelected(true);
            unexecuted.setSelected(false);
        }else if(dataList.get(excelStep.childSteps.get(pos).step).recover_result==0){
            executed.setSelected(false);
            unexecuted.setSelected(true);
        }else {
            executed.setSelected(false);
            unexecuted.setSelected(false);
        }
        if(pos<0){
            pos=0;
        }
        title.setText(dataList.get(excelStep.step).id+dataList.get(excelStep.step).content.trim());
        standard.setText(dataList.get(excelStep.childSteps.get(pos).step).content);
        if(excelStep.childCount>0){
            rate.setText(pos+1+"/"+excelStep.childCount);
        }
    }

    @Override
    protected void previousStep() {
        excelDialogManager.previousStep();
    }

    @Override
    protected void nextStep() {
        excelDialogManager.nextStep();
    }

    @Override
    protected void logout() {
        excelDialogManager.exit();
        EventCenter.getInstance().post(MainActivity.BACK_TO_LOGIN);
    }

    @OnClick(R.id.executed)
    protected void executed() {
        executed.setSelected(true);
        unexecuted.setSelected(false);
        excelDialogManager.setResult(1);
    }

    @OnClick(R.id.unexecuted)
    protected void unexecuted() {
        unexecuted.setSelected(true);
        executed.setSelected(false);
        excelDialogManager.setResult(0);
    }


    public void setData2(List<ExecuteModel> executeModelList, ExcelStep excelStep, int pos) {
        if (title != null) {
            initData(executeModelList,excelStep,pos);
        } else {
            this.dataList=executeModelList;
            this.excelStep=excelStep;
            this.pos=pos;
        }
    }



    public void changePreButtonStatus(){
        previousStepBtn.setEnabled(false);
    }

    public void changeNextButtonStatus(){
        nextStepBtn.setEnabled(false);
    }
}
