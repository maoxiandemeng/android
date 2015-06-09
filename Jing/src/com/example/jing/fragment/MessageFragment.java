package com.example.jing.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jing.R;
import com.example.jing.base.BaseFragment;
import com.example.jing.utils.ImageLoader;
import com.example.jing.view.ChartView;

public class MessageFragment extends BaseFragment {
	private ChartView mChartView;
	private ImageView mImage;
	private int j = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_nearby, null);
		return mMainView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mChartView = (ChartView) view.findViewById(R.id.chatView);
		mImage = (ImageView) view.findViewById(R.id.image);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ArrayList<Float> list = new ArrayList<Float>();
		float max = 0;
		for (int i = 1; i < 11; i++) {
			list.add((float)i);
			if (max < i) {
				max = i;
			}
		}
		
		mChartView.setList(list);
		mChartView.setRectCount(6);
		mChartView.setMax(max);
		mChartView.setMin(0);
		
		mChartView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (j % 2 == 0) {
					mChartView.setRectCount(list.size());
					mChartView.postInvalidate();
				}else {
					mChartView.setRectCount(100);
					mChartView.postInvalidate();
				}
				j++;
			}
		});
		
		new ImageLoader().setImageView("http://e.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb32396f5f0da33c895d0430ccd.jpg", mImage);
	}
}
