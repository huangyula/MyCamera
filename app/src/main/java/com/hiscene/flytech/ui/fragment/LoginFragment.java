package com.hiscene.flytech.ui.fragment;

import android.widget.ImageView;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;
import com.hiscene.flytech.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.SCAN_LOGIN;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class LoginFragment extends BaseFragment {
    @BindView(R.id.iv_scan)
    ImageView scan;
    LoginScanListener scanListener;
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
        if(getActivity()instanceof LoginScanListener){

            ((LoginScanListener)getActivity()).scanLogin();

        }
    }


    public interface LoginScanListener{
       void scanLogin();
    }

    public void setScanListener( LoginScanListener scanListener ) {
        this.scanListener = scanListener;
    }
}
