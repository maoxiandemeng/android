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
	// ����һ�������ã�������ƣ�
	//private HashMap<String, SoftReference<Bitmap>> mImageCaches;
	private ImageView mImageView;

	public void setImageView(String imagePath, ImageView mImageView) {
		this.mImageView = mImageView;
		getBitmap(imagePath);
	}

	private void getBitmap(final String imagePath) {
		// �жϻ������Ƿ����
		if (MyApplication.getInstance().mImageCaches.containsKey(imagePath)) {
			SoftReference<Bitmap> softReference = MyApplication.getInstance().mImageCaches.get(imagePath);
			Bitmap bitmap = softReference.get();
			// ��������д���bitmap����ֱ�ӷ��أ����ٿ��̻߳�ȡbitmap
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
		// �жϱ��ػ������Ƿ����bitmap
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
		// ������������ж�û��bitmap�������߳����ػ�ȡbitmap
		new Thread() {
			public void run() {
				Bitmap bitmap = BitmapFactory.decodeStream(HttpUtil
						.getInputStreamFromUrl(imagePath));
				if (bitmap != null) {
					// ��bitmap���뻺��
					MyApplication.getInstance().mImageCaches.put(imagePath, new SoftReference<Bitmap>(
							bitmap));
					Log.d(TAG, "imagePath:" + imagePath + ",bitmap:" + bitmap);
					BitmapUtil.saveBitmap(bitmap, imagePath);
					// ����Я����bitmap����Ϣ��֪ͨhandler����UI
					Message message = handler.obtainMessage(0, bitmap);
					handler.sendMessage(message);
				}
			};
		}.start();
		return;

	}
}
