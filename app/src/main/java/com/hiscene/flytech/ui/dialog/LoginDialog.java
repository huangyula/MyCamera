package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;
import com.hiscene.flytech.event.EventCenter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.START_SCAN_LOGIN;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class LoginDialog extends BaseDialog {
    @BindView(R.id.iv_scan)
    Button scan;

    public LoginDialog( @NonNull Context context ) {
        super(context);
    }

    public static LoginDialog newInstance(Context context) {
        return new LoginDialog(context);
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
