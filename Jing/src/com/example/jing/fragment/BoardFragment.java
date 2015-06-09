package com.example.jing.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.jing.R;
import com.example.jing.adapter.BoardAdapter;
import com.example.jing.base.BaseFragment;
import com.example.jing.entity.BoardEntity;
import com.example.jing.ui.ListActivity;
import com.example.jing.utils.ActivityUtils;

public class BoardFragment extends BaseFragment {
	private static final String URL1 = "http://e.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb32396f5f0da33c895d0430ccd.jpg";
	private static final String URL2 = "http://h.hiphotos.baidu.com/image/pic/item/37d3d539b6003af355110221372ac65c1138b6cc.jpg";
	private static final String URL3 = "http://c.hiphotos.baidu.com/image/pic/item/6609c93d70cf3bc7b5c46dfcd000baa1cc112add.jpg";
	private static final String URL4 = "http://f.hiphotos.baidu.com/image/pic/item/ae51f3deb48f8c5471a15c2e38292df5e0fe7f45.jpg";
	private static final String URL5 = "http://g.hiphotos.baidu.com/image/pic/item/c8177f3e6709c93d3ed556bc9c3df8dcd1005451.jpg";
	private static final String URL6 = "http://b.hiphotos.baidu.com/image/pic/item/fd039245d688d43f76f37f527e1ed21b0ef43b3c.jpg";
	private static final String URL7 = "http://e.hiphotos.baidu.com/image/pic/item/3b292df5e0fe9925108bb71136a85edf8cb171e8.jpg";
	private ListView mListView;

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
		ArrayList<BoardEntity> list = new ArrayList<BoardEntity>();
		String[] urlStr = { URL1, URL2, URL3, URL4, URL5, URL6, URL7 };
		for (int i = 0; i < urlStr.length; i++) {
			BoardEntity entity = new BoardEntity();
			entity.setMsg("msg" + i);
			entity.setUrl(urlStr[i]);
			list.add(entity);
		}
		BoardAdapter<BoardEntity> adapter = new BoardAdapter<BoardEntity>(
				getActivity(), list);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActivityUtils.jumpActivity(getActivity(), ListActivity.class);
			}
		});

	}

}
