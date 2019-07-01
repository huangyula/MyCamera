package com.hiscene.flytech.ui.fragment;

import android.widget.ImageView;

import com.github.weiss.core.base.BaseFragment;
import com.hiscene.flytech.R;
import com.hiscene.flytech.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hiscene.flytech.ui.MainActivity.DEVICE;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des
 */
public class DeviceFragment extends BaseFragment {
    @BindView(R.id.iv_scan)
    ImageView scan;
    private DeviceListener deviceListener;

    public static DeviceFragment newInstance() {
        DeviceFragment deviceFragment = new DeviceFragment();
        return deviceFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device;
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.iv_scan)
    protected void startFragment(){
        if(getActivity()instanceof DeviceListener){

            ((DeviceListener)getActivity()).scanDevice();

        }
    }


    public interface DeviceListener{
        void scanDevice();
    }

    public void setScanListener( DeviceListener scanListener ) {
        this.deviceListener=deviceListener;
    }
}
