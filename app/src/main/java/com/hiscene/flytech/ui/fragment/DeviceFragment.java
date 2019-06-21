package com.hiscene.flytech.ui.fragment;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class DeviceFragment extends BaseFragment {

    public static DeviceFragment newInstance() {
        DeviceFragment deviceFragment = new DeviceFragment();
        return deviceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initView() {

    }
}
