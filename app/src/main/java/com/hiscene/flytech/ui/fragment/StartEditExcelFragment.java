package com.hiscene.flytech.ui.fragment;

import android.widget.ImageView;
import android.widget.TextView;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.App.userManager;

/**
 * @author huangyu
 * @desc 开始填写表单也
 */
public class StartEditExcelFragment extends BaseFragment {
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.logout)
    TextView logout;
    StartEditListener listener;
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
        if(getActivity()instanceof StartEditListener){
            ((StartEditListener) getActivity()).startEdit();
        }
    }

    @OnClick(R.id.logout)
    protected void logout(){
        userManager.logout();
        getActivity().finish();
    }

    public interface StartEditListener{
        void startEdit();
    }

    public void setStartEditListener(StartEditListener listener) {
        this.listener = listener;
    }

}
