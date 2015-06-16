package com.example.jing.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.jing.R;
import com.example.jing.adapter.GridViewAdapter;
import com.example.jing.base.BaseFragment;
import com.example.jing.utils.ToastUtils;
import com.example.jing.view.MySelectView;
import com.example.jing.view.MySelectView.onItemSelectListener;

public class GridViewFragment extends BaseFragment implements
		OnItemClickListener {
	private GridView mGridView;
	private GridViewAdapter mAdapter;

	private MySelectView mySelectView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_gridview, null);
		return mMainView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mGridView = (GridView) view.findViewById(R.id.gridView);
		mySelectView = (MySelectView) view.findViewById(R.id.selectView);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 14; i++) {
			list.add(i + 1 + "Íò");
		}
		mAdapter = new GridViewAdapter(getActivity(), list);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);

		list.clear();
		for (int i = 0; i < 5; i++) {
			list.add(i + 1 + "Íò");
		}
		mySelectView.setmList(list);
		mySelectView.setOnItemSelectListener(new onItemSelectListener() {

			@Override
			public void setOnItemSelect(AdapterView<?> parent, View view,
					int position, long id) {
				ToastUtils.show(getActivity(), position + "");
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mAdapter.setIndex(position);
		mAdapter.notifyDataSetChanged();
	}
}
