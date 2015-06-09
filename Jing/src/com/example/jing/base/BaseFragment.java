package com.example.jing.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	private static final String TAG = "BaseFragment";
	protected View mMainView;
	private BaseActivity mActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (BaseActivity) getActivity();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "-----------------onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "-----------------onPause");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "-----------------onStop");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "-----------------onDetach");
	}
}
