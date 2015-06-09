package com.example.jing.ui.guide;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.jing.MainActivity;
import com.example.jing.R;
import com.example.jing.base.BaseActivity;
import com.example.jing.utils.ActivityUtils;

public class ViewFlipperActivity extends BaseActivity implements OnGestureListener{
	private ViewFlipper mViewFlipper;
	private GestureDetector mGestureDetector;
	private TextView mTv;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_viewflipper);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mViewFlipper = (ViewFlipper) findViewById(R.id.vf_activity);
		mTv = (TextView) findViewById(R.id.tvInNew);
	}

	@Override
	protected void initEvents() {
		mGestureDetector = new GestureDetector(this, this);
		mTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityUtils.jumpActivity(ViewFlipperActivity.this, MainActivity.class);
				finish();
			}
		});
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {
			mViewFlipper.showNext();
		}else if (e1.getX() < e2.getX()) {
			mViewFlipper.showPrevious();
		}else {
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
	
}
