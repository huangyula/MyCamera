package com.hiscene.flytech.ui;

import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.R;
import com.hiscene.flytech.ui.fragment.ExcelFragmentManager;

public class MainActivity extends BaseActivity {

    ExcelFragmentManager excelFragmentManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        excelFragmentManager = new ExcelFragmentManager(getSupportFragmentManager());
    }

}