package com.hiscene.flytech.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiscene.flytech.R;

import java.io.File;
import java.util.List;

/**
 * 文件选择适配器
 */
public class FileSelectorAdapter extends BaseAdapter {


    Context context;
    List<File> fileList;
    LayoutInflater layoutInflater;

    public FileSelectorAdapter( Context context, List<File> fileList ) {
        this.context = context;
        this.fileList = fileList;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public File getItem( int i ) {
        return fileList.get(i);
    }

    @Override
    public long getItemId( int i ) {
        return 0;
    }


    @Override
    public View getView(
            int i,
            View convertView,
            ViewGroup viewGroup ) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_file, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(fileList.get(i));

        return convertView;
    }


    static class ViewHolder {

        ImageView file_icon;
        TextView file_name;
        int id;


        public ViewHolder( View v ) {
            file_icon = (ImageView) v.findViewById(R.id.file_icon);
            file_name = (TextView) v.findViewById(R.id.file_name);
        }


        public void bindData( File file ) {
            file_icon.setImageResource(
                    file.isDirectory()
                            ? R.mipmap.wenjianjia
                            : (file.getName().endsWith(".pdf")) ? R.mipmap.pdf : R.mipmap.picture);
            file_icon.setContentDescription(file.isDirectory()?"进入"+file.getName()+"文件夹":"打开"+file.getName()+"文件");
            file_name.setText(file.getName());
        }
    }


}
