package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.weiss.core.utils.LogUtils;
import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.TimeUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachFirstModel;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.END_TIME;
import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;

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
    List<AttachSecondModel> attachSecondModels;
    int pos=0;
    ExcelStep excelStep;

    public ProcessExcelFragment(ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    public static ProcessExcelFragment newInstance(ExcelFragmentManager excelFragmentManager) {
        //记录作业开始时间
        if(TextUtils.isEmpty(SPUtils.getString(START_TIME))){
            SPUtils.put(START_TIME, TimeUtils.getNowTimeString());
        }
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

    @Override
    protected void logout() {
        excelFragmentManager.exit();
        EventCenter.getInstance().post(MainActivity.BACK_TO_LOGIN);
    }

    private void initData(ProcessModel data) {
        init();
        standard.setVisibility(View.VISIBLE);
        executed.setVisibility(View.VISIBLE);
        unexecuted.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        title.setText(data.content);
        standard.setText(data.standard);
        rate.setText("");
        if(data.result==1){
            executed.setSelected(true);
            unexecuted.setSelected(false);
        }else if(data.result==0){
            executed.setSelected(false);
            unexecuted.setSelected(true);
        }else {
            executed.setSelected(false);
            unexecuted.setSelected(false);
        }
    }

    @OnClick(R.id.executed)
    protected void executed() {
        executed.setSelected(true);
        unexecuted.setSelected(false);
        excelFragmentManager.setResult(1);
    }

    @OnClick(R.id.unexecuted)
    protected void unexecuted() {
        executed.setSelected(false);
        unexecuted.setSelected(true);
        excelFragmentManager.setResult(0);
    }

    public void setData2(ProcessModel data,List<AttachFirstModel> attachFirstModels, ExcelStep excelStep, int pos) {
//         View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_main,null);
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
        init();
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
        et_number.setText(dataList.get(pos).result);
        et_number.setSelection(et_number.getText().length());
        if(excelStep.childCount>0){
            rate.setText(pos+1+"/"+excelStep.childCount);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
