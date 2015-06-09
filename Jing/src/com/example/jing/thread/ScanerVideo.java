package com.example.jing.thread;

import java.util.ArrayList;

import com.example.jing.MyApplication;
import com.example.jing.base.BaseActivity;
import com.example.jing.constant.MyConstant;
import com.example.jing.entity.VideoInfo;
import com.example.jing.utils.Helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

public class ScanerVideo extends AsyncTask<String, Void, ArrayList<VideoInfo>> {
	private Context context;
	private Handler handler;

	public ScanerVideo(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		((BaseActivity) context).showLoadingDialog();
	}

	@Override
	protected ArrayList<VideoInfo> doInBackground(String... params) {
		ArrayList<VideoInfo> infos = new ArrayList<VideoInfo>();
		// 如果SD卡不可用
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return infos;
		}
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				null, null, null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Video.Media._ID));
				long size = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Video.Media.SIZE));
				String title = cursor.getString(cursor
						.getColumnIndex(MediaStore.Video.Media.TITLE));
				String data = cursor.getString(cursor
						.getColumnIndex(MediaStore.Video.Media.DATA));
				String displayName = cursor.getString(cursor
						.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
				String mimeType = cursor.getString(cursor
						.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr,
						id, Images.Thumbnails.MICRO_KIND, options);
				Log.i("info", id + "---" + "---" + title + "---" + data + "---"
						+ displayName + "---" + mimeType);
				Log.i("info", "size--" + size);
				VideoInfo videoInfo = new VideoInfo(id, Helpers.getSizeText(
						context, size), title, data, displayName, mimeType,
						bitmap);
				infos.add(videoInfo);
			}
			cursor.close();
		}
		return infos;
	}

	@Override
	protected void onPostExecute(ArrayList<VideoInfo> result) {
		super.onPostExecute(result);
		((BaseActivity) context).hideLoadingDialog();
		MyApplication.getInstance().setmInfos(result);
		handler.sendMessage(handler.obtainMessage(MyConstant.SCANER_SUCCESS,
				result));
	}

}
