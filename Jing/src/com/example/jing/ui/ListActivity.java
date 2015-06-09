package com.example.jing.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jing.R;
import com.example.jing.adapter.BoardAdapter;
import com.example.jing.base.BaseActivity;
import com.example.jing.entity.BoardEntity;
import com.example.jing.view.MyListView;
import com.example.jing.view.MyListView.OnEmptyListener;
import com.example.jing.view.MyListView.OnLoadMoreListener;
import com.example.jing.view.MyListView.OnRefreshListener;

public class ListActivity extends BaseActivity {
	private MyListView myListView;
	private ArrayList<BoardEntity> list;
	private BoardAdapter<BoardEntity> adapter;
	private ImageView emptyView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_list);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		myListView = (MyListView) findViewById(R.id.myListView);
		emptyView = (ImageView) findViewById(R.id.empty_view);
	}

	@Override
	protected void initEvents() {
		list = new ArrayList<BoardEntity>();
		myListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				setData(true, false);
			}
		});
		myListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				if (list.isEmpty()) {
					return;
				}
				setData(false, true);
			}
		});
		myListView.setOnEmptyListener(new OnEmptyListener() {
			
			@Override
			public void onAddEmpty() {
				if (emptyView != null) {
					emptyView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onRemoveEmpty() {
				if (emptyView != null) {
					emptyView.setVisibility(View.GONE);
				}
			}
		});
		adapter = new BoardAdapter<BoardEntity>(this, list);
		myListView.setAdapter(adapter);
		setData(false, false);
//		emptyView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				setData(true, false);
//			}
//		});
	}
	
	private void setData(boolean isRefresh, boolean isLoadMore){
		if (isRefresh) {
			list.clear();
		}
		ArrayList<BoardEntity> list2 = new ArrayList<BoardEntity>();
		for (int i = 0; i < 20; i++) {
			BoardEntity entity = new BoardEntity();
			entity.setMsg("msg"+i);
			list2.add(entity);
		}
//		if (isRefresh) {
			list.addAll(list2);
//		}
//		myListView.setOnEmptyListener(null);
		myListView.notifyDataSetChanged(adapter);;
		if (isRefresh) {
			myListView.onRefreshComplete();
		}
		if (isLoadMore) {
			myListView.onLoadMoreComplete();
		}
	}

}
