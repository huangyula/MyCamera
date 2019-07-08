package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.squareup.haha.perflib.Main;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author huangyu
 * @des 二次措施单执行
 */
@SuppressLint("ValidFragment")
public class ExecuteExcelFragment extends BaseExcelFragment<ExecuteModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.standard)
    TextView standard;

    ExecuteModel data;
    List<ExecuteModel> dataList;
    int pos=0;
    ExcelStep excelStep;

    public ExecuteExcelFragment(ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    public static ExecuteExcelFragment newInstance(ExcelFragmentManager excelFragmentManager) {
        ExecuteExcelFragment executeExcelFragment = new ExecuteExcelFragment(excelFragmentManager);
        return executeExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_execute;
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
        init();
        if(dataList.get(excelStep.childSteps.get(pos).step).excute_result==1){
            executed.setSelected(true);
            unexecuted.setSelected(false);
        }else if(dataList.get(excelStep.childSteps.get(pos).step).excute_result==0){
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
        excelFragmentManager.previousStep();
    }

    @Override
    protected void nextStep() {
        excelFragmentManager.nextStep();
    }

    @Override
    protected void logout() {
        excelFragmentManager.exit();
        EventCenter.getInstance().post(MainActivity.BACK_TO_LOGIN);
    }

    @OnClick(R.id.executed)
    protected void executed() {
        executed.setSelected(true);
        unexecuted.setSelected(false);
        excelFragmentManager.setResult(1);
    }

    @OnClick(R.id.unexecuted)
    protected void unexecuted() {
        unexecuted.setSelected(true);
        executed.setSelected(false);
        excelFragmentManager.setResult(0);
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
