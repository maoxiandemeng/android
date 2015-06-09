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

/**
 * 程序的导航栏，如上面或下面的几个tab切换fragment
 * 
 * @author
 *
 */
public class NavigationBar extends LinearLayout implements android.view.View.OnClickListener{
	private View[] views;
	private onTabSelectListern onTabSelectListern;
	private int selectId;
	
	public void setOnTabSelectListern(onTabSelectListern onTabSelectListern) {
		this.onTabSelectListern = onTabSelectListern;
	}

	public NavigationBar(Context context) {
		super(context);
		init(context);
	}

	public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NavigationBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context){
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
		setVisibility(View.GONE);
		setBackgroundColor(R.color.black);
	}
	
	/**
	 * 加载导航栏的信息
	 */
	public void loadItems(ArrayList<NavigationItem> items){
		if (items.isEmpty()) {
			setVisibility(View.GONE);
			return;
		}
		setVisibility(View.VISIBLE);
		views = new View[items.size()];
		LayoutInflater inflater = LayoutInflater.from(getContext());
		for (int i = 0; i < items.size(); i++) {
			View view = inflater.inflate(R.layout.navigationbar_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.item_image);
			TextView text = (TextView) view.findViewById(R.id.item_text);
			
			NavigationItem item = items.get(i);
			view.setId(item.nId);
			image.setImageResource(item.nImage);
			text.setText(item.nText);
			
			addView(view,new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
			view.setOnClickListener(this);
			views[i] = view;
		}
		
		reloadHight();
		
	}
	
	@Override
	public void onClick(View v) {
		clickListern(v.getId());
	}
	
	private void clickListern(int id){
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
		if (onTabSelectListern != null) {
			onTabSelectListern.setOnTabSelectListern(id);
		}
	}
	
	private void clearHight(){
		for (View view : views) {
			view.setSelected(false);
		}
	}
	
	private void reloadHight(){
		clearHight();
		views[selectId].setSelected(true);
	}
	
	public interface onTabSelectListern{
		public void setOnTabSelectListern(int id);
	}
	
	public static class NavigationItem{
		public int nId;
		public int nText;
		public int nImage;
		
		public NavigationItem(int nId, int nText, int nImage){
			this.nId = nId;
			this.nText = nText;
			this.nImage = nImage;
		}
	}
}
