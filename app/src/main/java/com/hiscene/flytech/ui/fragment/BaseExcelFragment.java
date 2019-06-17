package com.hiscene.flytech.ui.fragment;

import android.widget.Button;

import com.github.weiss.core.base.BaseRxFragment;
import com.hiscene.flytech.R;

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

}
