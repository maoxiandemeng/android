package com.example.jing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jing.base.BaseFragment;
import com.example.jing.view.LockPatternView;

public class NearbyFragment extends BaseFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = new LockPatternView(getActivity());
		return mMainView;
	}
	
}
