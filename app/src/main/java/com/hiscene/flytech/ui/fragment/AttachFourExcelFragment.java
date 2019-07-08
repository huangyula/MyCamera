package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;

import butterknife.BindView;

import static com.hiscene.flytech.App.userManager;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class AttachFourExcelFragment extends BaseExcelFragment<AttachSecondModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rate)
    TextView tv_rate;
    @BindView(R.id.time_1)
    TextView time_1;
    @BindView(R.id.time_2)
    TextView time_2;
    int step;
    String rate="1";
    ProcessModel processModel;


    public AttachFourExcelFragment( ExcelFragmentManager excelFragmentManager) {
        super(excelFragmentManager);
    }

    @Override
    public void setData( AttachSecondModel data ) {

    }

    public static AttachFourExcelFragment newInstance( ExcelFragmentManager excelFragmentManager) {
        AttachFourExcelFragment attachFourExcelFragment = new AttachFourExcelFragment(excelFragmentManager);
        return attachFourExcelFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attach_four_excel;
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
