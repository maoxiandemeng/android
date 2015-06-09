package com.example.jing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseObjectAdapter;
import com.example.jing.entity.BoardEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class BoardAdapter<T> extends BaseObjectAdapter<T> {

	public BoardAdapter(Context context, ArrayList<T> list) {
		super(context, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.board_item, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.tv);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		BoardEntity boardEntity = (BoardEntity) list.get(position);
		holder.tv.setText(boardEntity.getMsg());
		ImageLoader.getInstance().displayImage(boardEntity.getUrl(), holder.icon, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		});
		return convertView;
	}
	
	public class ViewHolder{
		private TextView tv;
		private ImageView icon;
	}
}
