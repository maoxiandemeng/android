package com.example.jing.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jing.R;

public class MySelectView extends GridView implements OnItemClickListener {
	private ArrayList<String> mList;
	private MySelectAdapter mySelectAdapter;
	private onItemSelectListener onItemSelectListener;

	public ArrayList<String> getmList() {
		return mList;
	}

	public void setmList(ArrayList<String> mList) {
		this.mList = mList;
		mySelectAdapter = new MySelectAdapter(getContext(), mList);
		setAdapter(mySelectAdapter);
	}

	public MySelectView(Context context) {
		super(context, null);
	}

	public MySelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MySelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MySelectView);
		int num = typedArray.getInteger(R.styleable.MySelectView_num, 1);
		typedArray.recycle();
		setNumColumns(num);
		setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mySelectAdapter.setIndex(position);
		mySelectAdapter.notifyDataSetChanged();
		if (onItemSelectListener != null) {
			onItemSelectListener.setOnItemSelect(parent, view, position, id);
		}
	}
	
	public onItemSelectListener getOnItemSelectListener() {
		return onItemSelectListener;
	}

	public void setOnItemSelectListener(onItemSelectListener onItemSelectListener) {
		this.onItemSelectListener = onItemSelectListener;
	}

	public interface onItemSelectListener{
		void setOnItemSelect(AdapterView<?> parent, View view, int position, long id);
	}

	class MySelectAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<String> list;
		private int index;
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public MySelectAdapter(Context context, ArrayList<String> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list.isEmpty()) {
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			if (list.isEmpty()) {
				return null;
			}
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("unused")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.grid_item, null);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(list.get(position));
			for (String str : list) {
				holder.tv.setSelected(false);
			}
			if (position == index) {
				holder.tv.setSelected(true);
			}
			return convertView;
		}
	}

	class ViewHolder {
		public TextView tv;
	}

}
