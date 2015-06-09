package com.example.jing.utils;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.jing.MyApplication;

public class ImageLoader {
	protected static final String TAG = "ImageLoader";
	// 定义一个软引用（缓存机制）
	//private HashMap<String, SoftReference<Bitmap>> mImageCaches;
	private ImageView mImageView;

	public void setImageView(String imagePath, ImageView mImageView) {
		this.mImageView = mImageView;
		getBitmap(imagePath);
	}

	private void getBitmap(final String imagePath) {
		// 判断缓存中是否存在
		if (MyApplication.getInstance().mImageCaches.containsKey(imagePath)) {
			SoftReference<Bitmap> softReference = MyApplication.getInstance().mImageCaches.get(imagePath);
			Bitmap bitmap = softReference.get();
			// 如果缓存中存在bitmap，则直接返回，不再开线程获取bitmap
			if (bitmap != null) {
				Log.d(TAG, "cachesBitmap==" + bitmap);
				if (mImageView != null) {
					mImageView.setImageBitmap(bitmap);
				}
				return;
			} else {
				MyApplication.getInstance().mImageCaches.remove(imagePath);
			}
		}
		// 判断本地缓存中是否存在bitmap
		if (BitmapUtil.bitmapExists(imagePath)) {
			Bitmap bitmap = BitmapUtil.getBitmapFromSDCard(imagePath);
			Log.d(TAG, "bitmap==" + bitmap);
			if (mImageView != null) {
				mImageView.setImageBitmap(bitmap);
			}
			return;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (mImageView != null) {
					mImageView.setImageBitmap(bitmap);
				}
			}
		};
		// 如果两个缓存中都没有bitmap，则开启线程下载获取bitmap
		new Thread() {
			public void run() {
				Bitmap bitmap = BitmapFactory.decodeStream(HttpUtil
						.getInputStreamFromUrl(imagePath));
				if (bitmap != null) {
					// 将bitmap放入缓存
					MyApplication.getInstance().mImageCaches.put(imagePath, new SoftReference<Bitmap>(
							bitmap));
					Log.d(TAG, "imagePath:" + imagePath + ",bitmap:" + bitmap);
					BitmapUtil.saveBitmap(bitmap, imagePath);
					// 发送携带了bitmap的消息，通知handler更新UI
					Message message = handler.obtainMessage(0, bitmap);
					handler.sendMessage(message);
				}
			};
		}.start();
		return;

	}
}
