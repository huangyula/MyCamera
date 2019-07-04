package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
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
public class AttachSecondExcelFragment extends BaseExcelFragment<AttachSecondModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rate)
    TextView tv_rate;
    @BindView(R.id.device_name)
    TextView device_name;

    String rate="1";
    ProcessModel processModel;
    AttachSecondModel attachSecondModel;
    List<AttachSecondModel> attachSecondModels;

    public AttachSecondExcelFragment( ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    @Override
    public void setData( AttachSecondModel data ) {

    }

    public static AttachSecondExcelFragment newInstance( ExcelFragmentManager excelFragmentManager) {
        AttachSecondExcelFragment attachSecondExcelFragment = new AttachSecondExcelFragment(excelFragmentManager);
        return attachSecondExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attach_second_excel;
    }


    @Override
    protected void initView() {
        if (processModel != null&&attachSecondModel!=null) {//第一次初始化setData还没Attach Activity
            initData(processModel,attachSecondModel,rate);
        }
    }

    public void setData( ProcessModel processModel, AttachSecondModel attachSecondModel,String rate) {
        if (title != null) {
            initData(processModel,attachSecondModel,rate);
        } else {
            this.rate=rate;
            this.processModel = processModel;
            this.attachSecondModel = attachSecondModel;
        }
    }

    private void initData( ProcessModel processModel, AttachSecondModel attachSecondModel,String rate) {
        title.setText(processModel.content+"("+processModel.standard+")");
        device_name.setText("装置名称："+attachSecondModel.name);
        tv_rate.setText(rate);
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
