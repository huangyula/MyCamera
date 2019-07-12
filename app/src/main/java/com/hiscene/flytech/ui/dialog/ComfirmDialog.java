package com.hiscene.flytech.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.github.weiss.core.utils.DisplayUtil;
import com.hiscene.flytech.C;
import com.hiscene.flytech.R;
import com.hiscene.flytech.entity.Result;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.TestActivity;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.START_SCAN_LOGIN;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class ComfirmDialog extends Dialog {
    @BindView(R.id.continue_edit)
    Button continue_edit;
    @BindView(R.id.restart_edit)
    Button restart_edit;

    public Context mContext;

    public ComfirmDialog(@NonNull Context context ) {
        super(context, R.style.transparentBgDialog);
        this.mContext = context;
    }

    public static ComfirmDialog newInstance(Context context) {
        return new ComfirmDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = View.inflate(mContext, R.layout.dialog_comfirm, null);
        setContentView(mView);
        ButterKnife.bind(this,mView);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.continue_edit)
    protected void continue_edit(){
        dismiss();
    }

    @OnClick(R.id.restart_edit)
    protected void restart_edit(){
        Result result=new Result(C.RESTART_EDIT,"");
        EventCenter.getInstance().post(result);
        dismiss();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = (int) (DisplayUtil.getScreenWidth(mContext)*(2/3f));
        wl.height = (int) (DisplayUtil.getScreenHeight(mContext));
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }
}
