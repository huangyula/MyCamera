package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hiscene.flytech.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 无设备资料提醒框
 */
public class NoDeviceInfoDialog extends BaseDialog{
    @BindView(R.id.close)
    TextView close;
    public NoDeviceInfoDialog( @NonNull Context context ) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_no_device_info;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        this.setCancelable(false);
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey( DialogInterface dialogInterface, int i, KeyEvent keyEvent ) {
                if(i == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.close)
    protected void close(){
        dismiss();
    }

}
