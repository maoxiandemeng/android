package com.example.jing.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.jing.R;
import com.example.jing.utils.Helpers;

public class MyViewPage extends RelativeLayout {
	private ViewPager mViewPager;
	private LinearLayout mIndicateLayout;
	private ArrayList<View> mPageViews;

	private OnItemSelectListener mOnItemSelectListener;
	private OnItemChangeListener mOnItemChangeListener;

	private int mCurrentIndex = 0;
	private int mCount = 0;
	private AutoHandle mAutoHandle;

	public MyViewPage(Context context) {
		super(context);
		init(context);
	}

	public MyViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mViewPager = new ViewPager(context);
		addView(mViewPager);

		RelativeLayout relBottom = new RelativeLayout(context);
		relBottom.setBackgroundColor(Color.BLUE);
		RelativeLayout.LayoutParams relParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, Helpers.dpToPx(30, getContext()));
		relParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relBottom.setLayoutParams(relParams);

		mIndicateLayout = new LinearLayout(context);
		mIndicateLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.rightMargin = Helpers.dpToPx(20, getContext());
		mIndicateLayout.setLayoutParams(params);
		relBottom.addView(mIndicateLayout);
		addView(relBottom);

		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mAutoHandle = new AutoHandle();
	}

	/**
	 * 设置资源文件
	 * 
	 * @param ids
	 */
	public void setDrawable(ArrayList<Integer> ids) {
		if (ids.isEmpty()) {
			return;
		}
		mPageViews = new ArrayList<View>();
		for (int i = 0; i < ids.size(); i++) {
			View view = new ImageButton(getContext());
			view.setBackgroundResource(ids.get(i));
			view.setOnClickListener(new ItemClickListener(i));
			mPageViews.add(view);
		}
		// 初始化指示器
		for (int index = 0; index < ids.size(); index++) {
			final View indicater = new ImageView(getContext());
			mIndicateLayout.addView(indicater, index);
		}
		mCount = mPageViews.size() - 1;
	}

	public void show() {
		refreshIndicateView();
		MyPagerAdapter mAdapter = new MyPagerAdapter(mPageViews);
		mViewPager.setAdapter(mAdapter);
		sendMessage();
	}

	protected void refreshIndicateView() {
		for (int index = 0; index <= mCount; index++) {
			final ImageView imageView = (ImageView) mIndicateLayout
					.getChildAt(index);
			if (mCurrentIndex == index) {
				imageView
						.setBackgroundResource(R.drawable.view_imageindicator_image_indicator_focus);
			} else {
				imageView
						.setBackgroundResource(R.drawable.view_imageindicator_image_indicator);
			}
		}
		mViewPager.setCurrentItem(mCurrentIndex, false);

	}

	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			mCurrentIndex = index;
			refreshIndicateView();
			if (mOnItemChangeListener != null) {
				mOnItemChangeListener.onPosition(mCurrentIndex, mCount);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	private class ItemClickListener implements View.OnClickListener {
		private int position = 0;

		public ItemClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if (mOnItemSelectListener != null) {
				mOnItemSelectListener.onItemSelect(view, position);
			}
		}
	}

	private class AutoHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i("info", "mCurrentIndex: " + mCurrentIndex);
			if (mCurrentIndex == mCount) {
				mCurrentIndex = 0;
			} else if (mCurrentIndex >= 0
					&& mCurrentIndex < mCount) {
				mCurrentIndex++;
			}
			refreshIndicateView();
			sendEmptyMessageDelayed(mCurrentIndex, 3000);
		}
	}
	
	public void sendMessage(){
		Log.i("info", "sendMessage----");
		mAutoHandle.sendEmptyMessageDelayed(mCurrentIndex, 3000);
	}
	
	public void removeMessage(){
		Log.i("info", "removeMessage----");
		mAutoHandle.removeMessages(mCurrentIndex);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeMessage();
	}

	private class MyPagerAdapter extends PagerAdapter {
		private ArrayList<View> pageViews = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> pageViews) {
			this.pageViews = pageViews;
		}

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 条目点击事件监听接口
	 */
	public interface OnItemSelectListener {
		public void onItemSelect(View view, int position);
	}

	public void setOnItemSelectListener(
			OnItemSelectListener onItemSelectListener) {
		this.mOnItemSelectListener = onItemSelectListener;
	}

	/**
	 * 滑动监听事件
	 */
	public interface OnItemChangeListener {
		public void onPosition(int position, int totalCount);
	}

	public void setOnItemChangeListener(
			OnItemChangeListener onItemChangeListener) {
		this.mOnItemChangeListener = onItemChangeListener;
	}

	public int getmCurrentIndex() {
		return mCurrentIndex;
	}

	public void setmCurrentIndex(int mCurrentIndex) {
		this.mCurrentIndex = mCurrentIndex;
	}

}
