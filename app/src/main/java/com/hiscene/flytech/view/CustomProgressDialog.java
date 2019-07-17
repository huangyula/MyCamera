package com.hiscene.flytech.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.weiss.core.utils.DisplayUtil;
import com.hiscene.flytech.R;
import com.hiscene.flytech.ui.dialog.BaseDialog;


/**
 * 加载框
 *
 * @author zajo
 */
public class CustomProgressDialog extends Dialog {
    Context mContext;

    public CustomProgressDialog( Context context, int theme) {
        super(context, theme);
        mContext=context;
        setContentView(R.layout.custom_progress_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    /**
     *
     * [Summary] setTitile 标题
     *
     *
     */
    public CustomProgressDialog setTitile( String strTitle) {
        return this;
    }

    /**
     *
     * [Summary] setMessage 提示内容
     *
     *
     */
    public CustomProgressDialog setMessage( String strMessage) {
        TextView tvMsg = (TextView)findViewById(R.id.load_info_text);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.width = (int) (DisplayUtil.getScreenWidth(mContext));
        wl.height = (int) (DisplayUtil.getScreenHeight(mContext));
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }
}
