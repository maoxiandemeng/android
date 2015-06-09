package com.example.jing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseObjectAdapter;
import com.example.jing.entity.FileItem;

public class FileAdapter extends BaseObjectAdapter<FileItem> {
	private final int[] mFileIcon = {R.drawable.icon_list_folder, R.drawable.icon_list_unknown};

	public FileAdapter(Context context, ArrayList<FileItem> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_list_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.file_icon);
            holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItem item = (FileItem)list.get(position);
        holder.icon.setImageResource(mFileIcon[item.getFileType()]);
        holder.fileName.setText(item.getFileName());
        return convertView;
	}
	
	class ViewHolder {
        ImageView icon;
        TextView fileName;
    }

}
