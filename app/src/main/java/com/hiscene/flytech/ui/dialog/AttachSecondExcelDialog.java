package com.hiscene.flytech.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.CollectionUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.MySpinnerAdapter;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.ui.fragment.BaseExcelFragment;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;
import com.hiscene.flytech.view.CustomAlertDialog;

import java.util.List;

import butterknife.BindView;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des
 */
@SuppressLint("ValidFragment")
public class AttachSecondExcelDialog extends BaseExcelDialog<AttachSecondModel> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rate)
    TextView tv_rate;
    @BindView(R.id.device_name)
    TextView device_name;
    @BindView(R.id.factory)
    TextView factory;
    @BindView(R.id.device_number)
    TextView device_number;
    @BindView(R.id.device_version)
    TextView device_version;
    @BindView(R.id.check_code)
    TextView check_code;
    @BindView(R.id.parent)
    LinearLayout linearLayout;

    String rate="1";
    ProcessModel processModel;
    AttachSecondModel attachSecondModel;
    List<AttachSecondModel> attachSecondModels;

    boolean first=true;

    private List<String> factory_array;
    private List<String> device_number_array;
    private List<String> check_code_array;
    private List<String> device_version_array;
    private MySpinnerAdapter factory_adapter,device_number_adapter,device_version_adapter,check_code_adapter;

    public AttachSecondExcelDialog( Context context, ExcelDialogManager excelDialogManager ) {
        super(context, excelDialogManager);
    }


    @Override
    public void setData( AttachSecondModel data ) {

    }

    public static AttachSecondExcelDialog newInstance(Context context, ExcelDialogManager excelDialogManager) {
        AttachSecondExcelDialog attachSecondExcelDialog = new AttachSecondExcelDialog(context,excelDialogManager);
        return attachSecondExcelDialog;
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
        init();
        device_number_array=CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.device_number));
        device_version_array=CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.device_version));
        factory_array=CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.factory));
        check_code_array=CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.check_code));
        setListener();

        title.setText(processModel.content+"("+processModel.standard+")");
        device_name.setText("装置名称："+attachSecondModel.name);
        if(attachSecondModel!=null){
            device_number.setText(attachSecondModel.number);
            device_version.setText(attachSecondModel.verison);
            check_code.setText(attachSecondModel.check_code);
            factory.setText(attachSecondModel.factory);
        }

        tv_rate.setText(rate);

    }

    private void setListener() {
        device_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CustomAlertDialog.showListDialog(getContext(), "",device_number_array, new CustomAlertDialog.IAlertListDialogItemClickListener() {
                    @Override
                    public void onItemClick( int position ) {
                        initDeviceData(position);
                    }
                });
            }
        });
//        device_version.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( View view ) {
//                CustomAlertDialog.showListDialog(getContext(), "",device_version_array, new CustomAlertDialog.IAlertListDialogItemClickListener() {
//                    @Override
//                    public void onItemClick( int position ) {
//                        initDeviceData(position);
//                    }
//                });
//            }
//        });
        check_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CustomAlertDialog.showListDialog(getContext(), "",check_code_array, new CustomAlertDialog.IAlertListDialogItemClickListener() {
                    @Override
                    public void onItemClick( int position ) {
                        initDeviceData(position);
                    }
                });
            }
        });
//        factory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( View view ) {
//                CustomAlertDialog.showListDialog(getContext(), "",factory_array, new CustomAlertDialog.IAlertListDialogItemClickListener() {
//                    @Override
//                    public void onItemClick( int position ) {
//                        initDeviceData(position);
//                    }
//                });
//            }
//        });
    }

    private void initDeviceData(int position){
        device_number.setText(device_number_array.get(position));
        device_version.setText(device_version_array.get(position));
        factory.setText(factory_array.get(position));
        check_code.setText(check_code_array.get(position));
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
