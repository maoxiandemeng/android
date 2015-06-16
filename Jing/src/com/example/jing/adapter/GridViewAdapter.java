package com.example.jing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseObjectAdapter;

public class GridViewAdapter extends BaseObjectAdapter<String> {
	private int index = 0;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public GridViewAdapter(Context context, ArrayList<String> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position));
		for (String str : list) {
			holder.tv.setSelected(false);
		}
		if (position == index) {
			holder.tv.setSelected(true);
		}
		return convertView;
	}
	
	class ViewHolder{
		public TextView tv;
	}

}
