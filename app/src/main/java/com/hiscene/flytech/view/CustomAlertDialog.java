package com.hiscene.flytech.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hiscene.flytech.R;
import com.hiscene.flytech.adapter.ListViewDialogAdapter;

import java.util.List;

public class CustomAlertDialog {
    public static void showListDialog( Context context, String title, List<String> items, final IAlertListDialogItemClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.transparentBgDialog);
        dialog.setContentView(R.layout.dialog_listview);
        ListView lv = (ListView) dialog.findViewById(R.id.listview);
        Button btnClose=(Button)dialog.findViewById(R.id.close);
        lv.setAdapter(new ListViewDialogAdapter(context, items));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null)
                    listener.onItemClick(position);
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface IAlertListDialogItemClickListener {
        void onItemClick(int position);
    }
}
