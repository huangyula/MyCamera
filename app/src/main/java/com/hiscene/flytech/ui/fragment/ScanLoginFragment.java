package com.hiscene.flytech.ui.fragment;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;

public class ScanLoginFragment extends BaseFragment {
    public static ScanLoginFragment newInstance() {
        ScanLoginFragment scanLoginFragment = new ScanLoginFragment();
        FLAG=SCAN_LOGIN;
        return scanLoginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scan_login;
    }

    @Override
    protected void initView() {

    }
}
