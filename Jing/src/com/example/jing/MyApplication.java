package com.example.jing;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.jing.entity.VideoInfo;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	private static MyApplication mApplication;
	private ArrayList<VideoInfo> mInfos;
	private boolean isScreenOff = false;
	// 定义一个软引用（缓存机制）
	public HashMap<String, SoftReference<Bitmap>> mImageCaches = new HashMap<String, SoftReference<Bitmap>>();

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
	}

	public static MyApplication getInstance() {
		return mApplication;
	}

	public ArrayList<VideoInfo> getmInfos() {
		return mInfos;
	}

	public void setmInfos(ArrayList<VideoInfo> mInfos) {
		this.mInfos = mInfos;
	}

	public boolean isScreenOff() {
		return isScreenOff;
	}

	public void setScreenOff(boolean isScreenOff) {
		this.isScreenOff = isScreenOff;
	}
}
