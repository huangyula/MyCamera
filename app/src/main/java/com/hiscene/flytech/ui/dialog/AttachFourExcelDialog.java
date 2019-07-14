package com.hiscene.flytech.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.ui.fragment.BaseExcelFragment;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class AttachFourExcelDialog extends BaseExcelDialog<AttachSecondModel> {

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

    public AttachFourExcelDialog( Context context, ExcelDialogManager excelDialogManager ) {
        super(context, excelDialogManager);
    }


    @Override
    public void setData( AttachSecondModel data ) {

    }

    public static AttachFourExcelDialog newInstance(Context context,ExcelDialogManager excelDialogManager) {
        AttachFourExcelDialog attachFourExcelDialog = new AttachFourExcelDialog(context,excelDialogManager);
        return attachFourExcelDialog;
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
        title.setText(processModel.id+". "+processModel.content+"("+processModel.standard+")");
        tv_rate.setText(rate);
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


}
