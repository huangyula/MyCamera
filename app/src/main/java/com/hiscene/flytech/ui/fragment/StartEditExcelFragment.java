package com.hiscene.flytech.ui.fragment;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.weiss.core.base.BaseFragment;
import com.github.weiss.core.utils.LogUtils;
import com.google.zxing.Result;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.UserModel;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.App.userManager;
import static com.hiscene.flytech.ui.MainActivity.LOGIN;
import static com.hiscene.flytech.ui.MainActivity.START_EDIT;
import static com.hiscene.flytech.ui.MainActivity.START_EDIT_EXCEL;

/**
 * @author huangyu
 * @desc 开始填写表单也
 */
public class StartEditExcelFragment extends BaseFragment {
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.logout)
    TextView logout;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_start_edit_excel;
    }

    @Override
    protected void initView() {

    }

    public static StartEditExcelFragment newInstance() {
        StartEditExcelFragment startEditExcelFragment = new StartEditExcelFragment();
        return startEditExcelFragment;
    }

    @OnClick(R.id.edit)
    protected void edit(){
        LogUtils.d("edit");
        EventCenter.getInstance().post(START_EDIT_EXCEL);
    }

    @OnClick(R.id.logout)
    protected void logout(){
        userManager.logout();
        EventCenter.getInstance().post(MainActivity.BACK_TO_LOGIN);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
