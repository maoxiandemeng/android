package com.example.jing.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.jing.MyApplication;
import com.example.jing.R;

public abstract class BaseActivity extends FragmentActivity {
	private static final String Tag = "BaseActivity";
	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	private FragmentManager mFragmentManager;
	private int mCurrentFragment = -1;

	private LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;

		IntentFilter filterSystem = new IntentFilter();
		filterSystem.addAction(Intent.ACTION_SCREEN_OFF);
		filterSystem.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mBatInfoReceiver, filterSystem);

		mFragmentManager = getSupportFragmentManager();
		mLoadingDialog = new LoadingDialog(this);
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.setCanceledOnTouchOutside(false);
	}

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	/**
	 * 检测手机屏幕的是打开或关闭
	 */
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Log.d(Tag, "-----------------screen is on...");
				MyApplication.getInstance().setScreenOff(false);
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.d(Tag, "----------------- screen is off...");
				MyApplication.getInstance().setScreenOff(true);
			}
		}
	};

	public void switchFragment(Fragment toFragment, int id) {
		switchFragment(toFragment, id, null);
	}

	/**
	 * 切换fragment
	 * 
	 * @param toFragment
	 * @param id
	 * @param bundle
	 */
	public void switchFragment(Fragment toFragment, int id, Bundle bundle) {
		if (mCurrentFragment == id) {
			if (bundle != null && !bundle.isEmpty()) {
				getCurrFragment().getArguments().putAll(bundle);
			}

			return;
		}
		Fragment fromFragment = mFragmentManager.findFragmentByTag(String
				.valueOf(mCurrentFragment));

		mCurrentFragment = id;

		if (toFragment == null) {
			toFragment = mFragmentManager.findFragmentByTag(String.valueOf(id));
			if (bundle != null && !bundle.isEmpty()) {
				toFragment.setArguments(bundle);
			}
		} else if (bundle != null && !bundle.isEmpty()) {
			toFragment.getArguments().putAll(bundle);
		}

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (fromFragment != null && !fromFragment.isHidden()) {
			ft.hide(fromFragment);
		}

		if (!toFragment.isAdded()) {
			ft.add(R.id.content, toFragment, String.valueOf(id));
		} else {
			ft.show(toFragment);
		}

		ft.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
	}

	protected Fragment getCurrFragment() {
		return getFragment(mCurrentFragment);
	}

	protected Fragment getFragment(int markId) {
		Fragment fragment = mFragmentManager.findFragmentByTag(String
				.valueOf(markId));
		return fragment;
	}

	public void showLoadingDialog() {
		if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
		}
	}

	public void hideLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.clear();
			mLoadingDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(Tag, "-----------------onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(Tag, "-----------------onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(Tag, "-----------------onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(Tag, "-----------------onDestroy");
		unregisterReceiver(mBatInfoReceiver);
	}

}
