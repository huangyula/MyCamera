package com.hiscene.flytech.ui.fragment;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class ScanDeviceFragment extends BaseFragment {

    public static ScanDeviceFragment newInstance() {
        ScanDeviceFragment scanDeviceFragment = new ScanDeviceFragment();
        FLAG=SCAN_DEVICE;
        return scanDeviceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scan_device;
    }

    @Override
    protected void initView() {

    }
}
