package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
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
    @BindView(R.id.device)
    TextView device;
    @BindView(R.id.item)
    TextView item;

    int step;
    String rate="1";
    ProcessModel processModel;


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
        if (processModel != null) {//第一次初始化setData还没Attach Activity
            initData(processModel,step,rate);
        }
    }

    public void setData( ProcessModel processModel, int step,String rate) {
        if (title != null) {
            initData(processModel,step,rate);
        } else {
            this.rate=rate;
            this.processModel = processModel;
            this.step = step;
        }
    }

    private void initData( ProcessModel processModel, int step ,String rate) {
        init();
        title.setText(processModel.content+"("+processModel.standard+")");
        tv_rate.setText(rate);
        switch (step){
            case 0:
                device.setText("主一装置通道");
                item.setText("项   目1");
                break;
            case 1:
                device.setText("主二装置通道");
                item.setText("项   目2");
                break;
            case 2:
                device.setText("光电转换装置通道");
                item.setText("项   目3");
                break;
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
