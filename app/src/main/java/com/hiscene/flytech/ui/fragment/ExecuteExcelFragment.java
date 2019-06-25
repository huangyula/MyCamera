package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExcelStyle;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.excel.ExecuteExcel;

import java.util.List;

import butterknife.BindView;

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

    public ExecuteExcelFragment(ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    public static ExecuteExcelFragment newInstance(ExcelFragmentManager excelFragmentManager) {
        ExecuteExcelFragment executeExcelFragment = new ExecuteExcelFragment(excelFragmentManager);
        return executeExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_process;
    }


    @Override
    protected void initView() {
        if (data != null) {//第一次初始化setData还没Attach Activity
            initData(data);
        }
    }

    @Override
    public void setData(ExecuteModel data) {
        if (title != null) {
            initData(data);
        } else {
            this.data = data;
        }
    }


    private void initData(ExecuteModel data) {
        title.setText(data.content);

    }

    @Override
    protected void previousStep() {
        excelFragmentManager.previousStep();
    }

    @Override
    protected void nextStep() {
        excelFragmentManager.nextStep();
    }

    public void setData2(List<ExecuteModel> executeModelList, ExcelStep excelStep, int pos) {
        System.out.println("tiltle: "+executeModelList.get(excelStep.step).content);
        System.out.println("content: "+executeModelList.get(excelStep.chilSteps.get(pos).step).content);
    }
}
