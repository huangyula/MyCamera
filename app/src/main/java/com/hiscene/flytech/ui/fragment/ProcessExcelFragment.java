package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.weiss.core.utils.TimeUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachFirstModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ExecuteModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.excel.ProcessExcel;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class ProcessExcelFragment extends BaseExcelFragment<ProcessModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.standard)
    TextView standard;
    @BindView(R.id.liner_attach_first_excel)
    LinearLayout linearLayout;
    @BindView(R.id.number)
    EditText et_number;
    @BindView(R.id.attach_first_content)
    TextView attach_first_content;

    ProcessModel data;
    List<AttachFirstModel> attachFirstModels;
    int pos=0;
    ExcelStep excelStep;

    public ProcessExcelFragment(ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    public static ProcessExcelFragment newInstance(ExcelFragmentManager excelFragmentManager) {
        ProcessExcelFragment processExcelFragment = new ProcessExcelFragment(excelFragmentManager);
        return processExcelFragment;
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
    public void setData(ProcessModel data) {
        if (title != null) {
            initData(data);
        } else {
            this.data = data;
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

    private void initData(ProcessModel data) {
        standard.setVisibility(View.VISIBLE);
        executed.setVisibility(View.VISIBLE);
        unexecuted.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        title.setText(data.content);
        standard.setText(data.standard);
    }

    @Override
    protected void executed() {
        excelFragmentManager.setResult(1);
    }

    @Override
    protected void unexecuted() {
        excelFragmentManager.setResult(0);
    }

    public void setData2(ProcessModel data,List<AttachFirstModel> attachFirstModels, ExcelStep excelStep, int pos) {
        if (title != null) {
            initData(data,attachFirstModels,excelStep,pos);
        } else {
            this.data=data;
            this.attachFirstModels=attachFirstModels;
            this.excelStep=excelStep;
            this.pos=pos;
        }
    }

    private void initData(ProcessModel data,List<AttachFirstModel> dataList,ExcelStep excelStep,int pos) {
        if(pos<0){
            pos=0;
        }
        title.setText(data.content+"("+data.standard+")");
        standard.setVisibility(View.GONE);
        executed.setVisibility(View.GONE);
        unexecuted.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        attach_first_content.setText(dataList.get(pos).content+"\n"+
                dataList.get(pos).standard);
        if(excelStep.childCount>0){
            rate.setText(pos+1+"/"+excelStep.childCount);
        }
    }

}
