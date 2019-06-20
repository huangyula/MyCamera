package com.hiscene.flytech.ui.fragment;

import android.widget.Button;
import android.widget.TextView;

import com.github.weiss.core.base.BaseRxFragment;
import com.hiscene.flytech.R;
import com.hiscene.flytech.view.ShowImagesDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/17
 * @des 根据当前ExcelStep的ExcelStyle显示不同的ExcelFragment
 */
public abstract class BaseExcelFragment<T> extends BaseRxFragment {

    @BindView(R.id.previousStepBtn)
    Button previousStepBtn;
    @BindView(R.id.nextStepBtn)
    Button nextStepBtn;
    @BindView(R.id.opration_risk)
    TextView oprationRisk;
    @BindView(R.id.device_info)
    TextView deviceInfo;
    protected ExcelFragmentManager excelFragmentManager;

    public BaseExcelFragment(ExcelFragmentManager excelFragmentManager) {
        this.excelFragmentManager = excelFragmentManager;
    }

    /**
     * 设置当前表格类型数据模型
     *
     * @param data
     */
    public abstract void setData(T data);

    @OnClick(R.id.previousStepBtn)
    protected abstract void previousStep();

    @OnClick(R.id.nextStepBtn)
    protected abstract void nextStep();

    @OnClick(R.id.opration_risk)
    protected void oprationRisk(){

    }

    @OnClick(R.id.device_info)
    protected void deviceInfo(){
        final List<String> urls = new ArrayList<>();
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-04-18299181_1306649979420798_1108869403736276992_n.jpg");
        urls.add("http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-02-926821_1453024764952889_775781470_n.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-28-18094719_120129648541065_8356500748640452608_n.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-27-17934080_117414798808566_8957027985114791936_n.jpg?imageslim");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-25-13651793_897557617014845_571817176_n.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-24-18013547_1532023163498554_215541963087151104_n.jpg");
        urls.add("http://7xi8d6.com1.z0.glb.clouddn.com/2017-04-24-18094714_158946097967074_5909424912493182976_n.jpg");

        new ShowImagesDialog(getActivity(), urls).show();

    }
}
