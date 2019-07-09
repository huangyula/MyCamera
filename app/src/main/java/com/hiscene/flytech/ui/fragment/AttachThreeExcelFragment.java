package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.AttachThreeModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;

import java.util.List;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class AttachThreeExcelFragment extends BaseExcelFragment<AttachSecondModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rate)
    TextView tv_rate;
    @BindView(R.id.item_name)
    TextView item_name;
    @BindView(R.id.item_no)
    TextView item_no;
    @BindView(R.id.item_title)
    TextView item_title;
    @BindView(R.id.et_A)
    EditText et_A;
    @BindView(R.id.et_B)
    EditText et_B;

    int step;
    String rate="1";
    ProcessModel processModel;
    AttachThreeModel attachThreeModel;


    public AttachThreeExcelFragment( ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    @Override
    public void setData( AttachSecondModel data ) {

    }

    public static AttachThreeExcelFragment newInstance( ExcelFragmentManager excelFragmentManager) {
        AttachThreeExcelFragment attachThreeExcelFragment = new AttachThreeExcelFragment(excelFragmentManager);
        return attachThreeExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attach_three_excel;
    }


    @Override
    protected void initView() {
        if (processModel != null&&attachThreeModel!=null) {//第一次初始化setData还没Attach Activity
            initData(processModel,attachThreeModel,step,rate);
        }
    }

    public void setData( ProcessModel processModel,AttachThreeModel attachThreeModel, int step,String rate) {
        if (title != null) {
            initData(processModel,attachThreeModel,step,rate);
        } else {
            this.rate=rate;
            this.processModel = processModel;
            this.attachThreeModel=attachThreeModel;
            this.step = step;
        }
    }

    private void initData( ProcessModel processModel, AttachThreeModel attachThreeModel,int step , String rate) {
        init();
        title.setText(processModel.content+"("+processModel.standard+")");
        tv_rate.setText(rate);
        if(attachThreeModel!=null){
            item_name.setText(attachThreeModel.item_name);
            item_no.setText(attachThreeModel.item_no);
            item_title.setText(attachThreeModel.title);
            et_A.setText(attachThreeModel.a);
            et_B.setText(attachThreeModel.b);
            et_A.setSelection(et_A.getText().toString().length());
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


}
