package com.hiscene.flytech.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.hiscene.flytech.BaseActivity;
import com.hiscene.flytech.R;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;

import butterknife.BindView;

public class OptionsScannerActivity extends BaseActivity implements OnScannerCompletionListener {
    @BindView(R.id.scanner_view)
    ScannerView scannerView;
    public static void gotoActivity(Activity activity) {
        activity.startActivity(new Intent(activity, OptionsScannerActivity.class));
    }

    @Override
    protected int getLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_scanner_options;
    }

    @Override
    protected void initView() {
        scannerView.setOnScannerCompletionListener(this);
        //        mScannerView.toggleLight(true);
        ScannerOptions.Builder builder = new ScannerOptions.Builder();
        builder.setFrameStrokeColor(Color.WHITE)
                .setFrameStrokeWidth(1.5f)
                .setFrameSize(256, 256)
                .setFrameCornerLength(22)
                .setFrameCornerWidth(2)
                .setFrameCornerInside(true)
                .setFrameCornerColor(Color.WHITE)
                .setLaserLineColor(0xff06c1ae)
                .setLaserLineHeight(2)
                .setLaserStyle(ScannerOptions.LaserStyle.COLOR_LINE, Color.WHITE)
                .setScanFullScreen(false)
                .setFrameHide(false)
                .setFrameCornerHide(false)
                .setLaserMoveFullScreen(false)
                .setFrameOutsideColor(Color.GRAY)
                .setScanMode(BarcodeFormat.QR_CODE)
                .setTipText("对准")
                .setTipTextSize(19)
                .setTipTextColor(Color.WHITE)
                .setCameraZoomRatio(2)

        ;
        scannerView.setScannerOptions(builder.build());
    }

    @Override
    protected void onResume() {
        scannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scannerView.onPause();
        super.onPause();
    }

    @Override
    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        vibrate();
//        scannerView.restartPreviewAfterDelay(0);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
