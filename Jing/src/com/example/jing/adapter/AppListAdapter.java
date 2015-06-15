package com.example.jing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseObjectAdapter;

public class AppListAdapter extends BaseObjectAdapter<ApplicationInfo> {

	public AppListAdapter(Context context, ArrayList<ApplicationInfo> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_app, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ApplicationInfo item = (ApplicationInfo) getItem(position);
		holder.iv_icon.setImageDrawable(item.loadIcon(context.getPackageManager()));
		holder.tv_name.setText(item.loadLabel(context.getPackageManager()));
		return convertView;
	}
	
	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(this);
		}
	}

}
