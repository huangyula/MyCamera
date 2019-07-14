package com.hiscene.flytech.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MySpinnerAdapter extends BaseAdapter {
    private List<String> items;
    private Context mContext;
    public MySpinnerAdapter( Context context,List<String> items ){
        this.mContext=context;
        this.items=items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem( int i ) {
        return items.get(i);
    }

    @Override
    public long getItemId( int i ) {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup ) {
        // 修改Spinner选择后结果的字体颜色
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(android.R.layout.simple_spinner_item, viewGroup, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(items.get(i));
        tv.setTextSize(18f);
        tv.setTextColor(Color.WHITE);
        return view;

    }


}
