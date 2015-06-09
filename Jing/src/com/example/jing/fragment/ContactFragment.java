package com.example.jing.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.jing.R;
import com.example.jing.adapter.ContactAdapter;
import com.example.jing.base.BaseFragment;
import com.example.jing.constant.MyConstant;
import com.example.jing.entity.VideoInfo;
import com.example.jing.thread.ScanerVideo;
import com.example.jing.ui.PlayVideoActivity;
import com.example.jing.utils.ActivityUtils;

@SuppressLint("HandlerLeak") 
public class ContactFragment extends BaseFragment {
	private ListView mListView;
	private ArrayList<VideoInfo> mInfos = new ArrayList<VideoInfo>();
	private ContactAdapter adapter;
	
	private Handler mHandler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.what == MyConstant.SCANER_SUCCESS) {
				ArrayList<VideoInfo> infos = (ArrayList<VideoInfo>) msg.obj;
				adapter.addAll(infos);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_board, null);
		return mMainView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mListView = (ListView) view.findViewById(R.id.listView);
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new ContactAdapter(getActivity(), mInfos);
		mListView.setAdapter(adapter);
		ScanerVideo scanerVideo = new ScanerVideo(getActivity(), mHandler);
		scanerVideo.execute("");
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VideoInfo info = (VideoInfo) parent.getAdapter().getItem(position);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConstant.VIDEO_INFO_ID, info.getId());
				ActivityUtils.jumpActivity(getActivity(), PlayVideoActivity.class, bundle);
			}
		});
	}
	
}
