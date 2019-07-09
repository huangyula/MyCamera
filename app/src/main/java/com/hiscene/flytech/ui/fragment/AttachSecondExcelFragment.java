package com.hiscene.flytech.ui.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.weiss.core.base.BaseApp;
import com.github.weiss.core.utils.CollectionUtils;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.MySpinnerAdapter;
import com.hiscene.flytech.entity.AttachSecondModel;
import com.hiscene.flytech.entity.ExcelStep;
import com.hiscene.flytech.entity.ProcessModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;
import com.hiscene.flytech.view.CustomAlertDialog;

import org.bouncycastle.jcajce.provider.symmetric.ARC4;

import java.util.ArrayList;
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
    @BindView(R.id.factory)
    Spinner factory;
    @BindView(R.id.device_number)
    Spinner device_number;
    @BindView(R.id.device_version)
    Spinner device_version;
    @BindView(R.id.check_code)
    Spinner check_code;
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
//        rootView.postDelayed(()->rootView.performClick(),500);
//        rootView.requestFocusFromTouch();
//        linearLayout.performClick();
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
        factory_array= CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.factory));
        factory_adapter=new MySpinnerAdapter(BaseApp.getAppContext(),factory_array);
        factory.setAdapter(factory_adapter);
                LogUtils.d(factory.getSelectedItem().toString());

        device_number_array= CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.device_number));
        device_number_adapter=new MySpinnerAdapter(BaseApp.getAppContext(),device_number_array);
        device_number.setAdapter(device_number_adapter);

        device_version_array=  CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.device_version));
        device_version_adapter=new MySpinnerAdapter(BaseApp.getAppContext(),device_version_array);
        device_version.setAdapter(device_version_adapter);

        check_code_array= CollectionUtils.arrayToList(BaseApp.getAppResources().getStringArray(R.array.check_code));
        check_code_adapter=new MySpinnerAdapter(BaseApp.getAppContext(),check_code_array);
        check_code.setAdapter(check_code_adapter);

        device_version.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ) {
                if(first) {
                    first = false;
                    return;
                }
            }

            @Override
            public void onNothingSelected( AdapterView<?> adapterView ) {

            }
        });

        int index=factory_array.indexOf(attachSecondModel.factory);
        factory.setSelection(index);
        LogUtils.d("factory_index"+index);
        index=device_number_array.indexOf(attachSecondModel.number);
        device_number.setSelection(index);
        index=check_code_array.indexOf(attachSecondModel.check_code);
        check_code.setSelection(index);
        index=device_version_array.indexOf(attachSecondModel.verison);
        device_version.setSelection(index);



        title.setText(processModel.content+"("+processModel.standard+")");
        device_name.setText("装置名称："+attachSecondModel.name);
        tv_rate.setText(rate);

        device_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                CustomAlertDialog.showListDialog(getContext(), "", BaseApp.getAppResources().getStringArray(R.array.check_code), new CustomAlertDialog.IAlertListDialogItemClickListener() {
                    @Override
                    public void onItemClick( int position ) {
                        LogUtils.d("itemclick:"+position);
                    }
                });
            }
        });
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
