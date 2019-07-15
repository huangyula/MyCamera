package com.hiscene.flytech.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.hiscene.flytech.R;


/**
 * 加载框
 *
 * @author zajo
 */
public class CustomProgressDialog extends Dialog {
    CustomProgressDialog dialog;

    public CustomProgressDialog( Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.custom_progress_dialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);

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
            dismiss();
        return super.onKeyDown(keyCode, event);
    }


    public CustomProgressDialog  getInstance( Context context,int theme){
        if(dialog==null){
            dialog=new CustomProgressDialog(context,theme);
        }
        return dialog;


    }

}
