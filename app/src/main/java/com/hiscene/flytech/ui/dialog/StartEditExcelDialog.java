package com.hiscene.flytech.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.weiss.core.utils.LogUtils;
import com.hiscene.flytech.R;
import com.hiscene.flytech.event.EventCenter;
import com.hiscene.flytech.ui.TestActivity;
import butterknife.BindView;
import butterknife.OnClick;
import static com.hiscene.flytech.App.userManager;
import static com.hiscene.flytech.ui.TestActivity.START_EDIT_EXCEL;

/**
 * @author huangyu
 * @desc 开始填写表单也
 */
public class StartEditExcelDialog extends BaseDialog {
    @BindView(R.id.edit)
    Button edit;
    @BindView(R.id.logout)
    TextView logout;

    public StartEditExcelDialog( @NonNull Context context ) {
        super(context);
    }

    public static StartEditExcelDialog newInstance(Context context) {
        StartEditExcelDialog startEditExcelDialog = new StartEditExcelDialog(context);
        return startEditExcelDialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_start_edit_excel;
    }

    @Override
    protected void initView() {
        logout.setText("test");
    }

    @OnClick(R.id.edit)
    void edit(){
        LogUtils.d("edit");
        EventCenter.getInstance().post(START_EDIT_EXCEL);
    }

    @OnClick(R.id.logout)
    void logout(){
        LogUtils.d("logout");
//        /this.dismiss();
        userManager.logout();
        EventCenter.getInstance().post(TestActivity.BACK_TO_LOGIN);
    }

}
