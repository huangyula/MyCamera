package com.hiscene.flytech.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.weiss.core.utils.SPUtils;
import com.github.weiss.core.utils.TimeUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachFirstModel;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.excel.ProcessExcel;
import com.hiscene.flytech.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.fragment.ExcelFragmentManager.START_TIME;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class ProcessExcelDialog extends BaseExcelDialog<ProcessModel> {

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
    @BindView(R.id.un_exsit)
    Button un_exsit;

    ProcessModel data;
    List<AttachFirstModel> attachFirstModels;
    List<AttachSecondModel> attachSecondModels;
    int pos=0;
    ExcelStep excelStep;

    public ProcessExcelDialog( Context context, ExcelDialogManager excelDialogManager ) {
        super(context, excelDialogManager);
        //记录作业开始时间
        if(TextUtils.isEmpty(SPUtils.getString(START_TIME))){
            SPUtils.put(START_TIME, TimeUtils.getNowTimeString());
        }

    }


    public static ProcessExcelDialog newInstance(Context context, ExcelDialogManager excelDialogManager) {
        ProcessExcelDialog processExcelDialog=new ProcessExcelDialog(context,excelDialogManager);
        return processExcelDialog;
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
        dismiss();
    }

    private void initData(ProcessModel data) {
        init();
        standard.setVisibility(View.VISIBLE);
        executed.setVisibility(View.VISIBLE);
        unexecuted.setVisibility(View.VISIBLE);
        un_exsit.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        title.setText(data.id+". "+data.content);
        standard.setText(data.standard);
        rate.setText("");
        if(data.result==1){
            executed.setSelected(true);
            unexecuted.setSelected(false);
            un_exsit.setSelected(false);
        }else if(data.result==0){
            executed.setSelected(false);
            un_exsit.setSelected(false);
            unexecuted.setSelected(true);
        }else if(data.result==-1){
            un_exsit.setSelected(true);
            executed.setSelected(false);
            unexecuted.setSelected(false);
        }else{
            executed.setSelected(false);
            unexecuted.setSelected(false);
            un_exsit.setSelected(false);
        }
    }

    @OnClick(R.id.executed)
    protected void executed() {
        executed.setSelected(true);
        unexecuted.setSelected(false);
        un_exsit.setSelected(false);
        excelDialogManager.setResult(1);
    }

    @OnClick(R.id.unexecuted)
    protected void unexecuted() {
        executed.setSelected(false);
        un_exsit.setSelected(false);
        unexecuted.setSelected(true);
        excelDialogManager.setResult(0);
    }

    @OnClick(R.id.un_exsit)
    protected void unexsit(){
        un_exsit.setSelected(true);
        executed.setSelected(false);
        unexecuted.setSelected(false);
        excelDialogManager.setResult(-1);
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
        title.setText(data.id+". "+data.content+"("+data.standard+")");
        standard.setVisibility(View.GONE);
        executed.setVisibility(View.GONE);
        unexecuted.setVisibility(View.GONE);
        un_exsit.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        attach_first_content.setText(dataList.get(pos).content+"\n"+
                dataList.get(pos).standard);
        et_number.setText(dataList.get(pos).result);
        et_number.setSelection(et_number.getText().length());
        if(excelStep.childCount>0){
            rate.setText(pos+1+"/"+excelStep.childCount);
        }
    }

}
