package com.example.jing.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jing.R;
import com.example.jing.base.BaseActivity;
import com.example.jing.fragment.AppListFragment;
import com.example.jing.view.DrawerView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SlidingActivity extends BaseActivity implements OnClickListener {
	private SlidingMenu mSlidingMenu;
	private ImageView mLeftImage, mRightImage;
	private TextView mTitle;
	private AppListFragment mAppListFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.sliding_main);
		initViews();
		initEvents();
		switchFragment(mAppListFragment, R.id.menu1);
	}

	@Override
	protected void initViews() {
		mLeftImage = (ImageView) findViewById(R.id.left_image);
		mRightImage = (ImageView) findViewById(R.id.right_image);
		mTitle = (TextView) findViewById(R.id.title);
		
		mAppListFragment = new AppListFragment();

		mSlidingMenu = new DrawerView(this).initSlidingMenu();
	}

	@Override
	protected void initEvents() {
		mLeftImage.setOnClickListener(this);
		mRightImage.setOnClickListener(this);
		
		mTitle.setText("app");
	}
	
	public void setSlidingTitle(String str){
		mTitle.setText(str);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_image:
			if(mSlidingMenu.isMenuShowing()){
				mSlidingMenu.showContent();
			}else{
				mSlidingMenu.showMenu();
			}
			break;
		case R.id.right_image:
			if(mSlidingMenu.isSecondaryMenuShowing()){
				mSlidingMenu.showContent();
			}else{
				mSlidingMenu.showSecondaryMenu();
			}
			break;

		default:
			break;
		}
	}
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mSlidingMenu.isMenuShowing() ||mSlidingMenu.isSecondaryMenuShowing()){
				mSlidingMenu.showContent();
			}else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			}
			return true;
		}
		//拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
