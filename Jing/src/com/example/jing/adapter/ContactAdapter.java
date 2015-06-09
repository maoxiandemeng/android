package com.example.jing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseObjectAdapter;
import com.example.jing.entity.VideoInfo;
import com.example.jing.utils.Helpers;

public class ContactAdapter extends BaseObjectAdapter<VideoInfo> {

	public ContactAdapter(Context context, ArrayList<VideoInfo> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.size = (TextView) convertView.findViewById(R.id.size);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		VideoInfo info = (VideoInfo) list.get(position);
//		Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(info.getData(), Images.Thumbnails.MICRO_KIND);
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, Helpers.dpToPx(100, context), Helpers.dpToPx(100, context));
		holder.image.setImageBitmap(info.getBitmap());
		holder.name.setText(info.getDisplayName());
		holder.size.setText(info.getSizeText());
		return convertView;
	}
	
	public class ViewHolder{
		private ImageView image;
		private TextView name;
		private TextView size;
	}

}
