package com.example.jing;

import java.util.ArrayList;

import android.app.Application;

import com.example.jing.entity.VideoInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyApplication extends Application {
	private static MyApplication mApplication;
	private ArrayList<VideoInfo> mInfos;
	private boolean isScreenOff = false;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		initImagerLoder();
	}

	public static MyApplication getInstance() {
		return mApplication;
	}

	private void initImagerLoder() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();
		ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
				.build();
		ImageLoader.getInstance().init(config2);

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
