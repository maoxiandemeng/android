package com.example.jing.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jing.R;

public class SelectView extends LinearLayout implements
		android.view.View.OnClickListener {
	private View[] views;
	private onTextSelectListern onTextSelectListern;
	private int selectId;
	private int rowX = 1; // 设置一行的个数

	public int getRowX() {
		return rowX;
	}

	public void setRowX(int rowX) {
		this.rowX = rowX;
	}

	public void setOnTextSelectListern(onTextSelectListern onTextSelectListern) {
		this.onTextSelectListern = onTextSelectListern;
	}

	public SelectView(Context context) {
		super(context);
		init(context);
	}

	public SelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		setVisibility(View.GONE);
		setBackgroundColor(getResources().getColor(R.color.black));
	}

	public void loadItems(ArrayList<TextItem> items) {
		if (items.isEmpty()) {
			setVisibility(View.GONE);
			return;
		}
		setVisibility(View.VISIBLE);
		views = new View[items.size()];
		LayoutInflater inflater = LayoutInflater.from(getContext());
		int row = items.size() % rowX == 0 ? items.size() / rowX : items.size()
				/ rowX + 1;
		for (int i = 0; i < row; i++) {
			LinearLayout ll = new LinearLayout(getContext());
			ll.setOrientation(LinearLayout.HORIZONTAL);
			int remain = items.size() > rowX * (i + 1) ? rowX : items.size()
					- rowX * i;
			for (int j = 0; j < remain; j++) {
				View view = inflater.inflate(R.layout.text_item, null);
				TextView text = (TextView) view.findViewById(R.id.item_text);

				TextItem item = items.get(rowX * i + j);
				view.setId(item.nId);
				text.setText(item.text);

				ll.addView(view, new LinearLayout.LayoutParams(0,
						LayoutParams.WRAP_CONTENT, 1.0f));
				view.setOnClickListener(this);
				views[rowX * i + j] = view;
			}
			addView(ll, new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 0, 1.0f));
		}
		// for (int i = 0; i < items.size(); i++) {
		// View view = inflater.inflate(R.layout.text_item, null);
		// TextView text = (TextView) view.findViewById(R.id.item_text);
		//
		// TextItem item = items.get(i);
		// view.setId(item.nId);
		// text.setText(item.text);
		//
		// addView(view, new LinearLayout.LayoutParams(0,
		// LayoutParams.WRAP_CONTENT, 1.0f));
		// view.setOnClickListener(this);
		// views[i] = view;
		// }

		reloadHight();

	}

	@Override
	public void onClick(View v) {
		clickListern(v.getId());
	}

	private void clickListern(int id) {
		int oldId = selectId;
		for (int i = 0; i < views.length; i++) {
			if (views[i].getId() == id) {
				selectId = i;
			}
		}

		if (oldId == selectId) {
			return;
		}

		reloadHight();
		if (onTextSelectListern != null) {
			onTextSelectListern.setOnTextSelectListern(id);
		}
	}

	private void clearHight() {
		for (View view : views) {
			view.setSelected(false);
		}
	}

	private void reloadHight() {
		clearHight();
		views[selectId].setSelected(true);
	}

	public interface onTextSelectListern {
		public void setOnTextSelectListern(int id);
	}

	public static class TextItem {
		public int nId;
		public String text;

		public TextItem(int nId, String text) {
			this.nId = nId;
			this.text = text;
		}
	}
}
