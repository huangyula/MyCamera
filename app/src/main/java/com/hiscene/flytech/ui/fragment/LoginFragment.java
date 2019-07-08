package com.hiscene.flytech.ui.fragment;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.SCAN_LOGIN;
import static com.hiscene.flytech.ui.MainActivity.START_SCAN_LOGIN;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class LoginFragment extends BaseFragment {
    @BindView(R.id.iv_scan)
    Button scan;
    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.iv_scan)
    protected void startFragment(){
        EventCenter.getInstance().post(START_SCAN_LOGIN);
    }


}
