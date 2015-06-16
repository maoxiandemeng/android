package com.example.jing.view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseActivity;
import com.example.jing.fragment.AppListFragment;
import com.example.jing.fragment.GridViewFragment;
import com.example.jing.ui.SlidingActivity;
import com.example.jing.utils.ToastUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

/**
 * 自定义SlidingMenu 测拉菜单类
 * */
public class DrawerView implements OnClickListener {
	private Activity activity;
	private SlidingMenu localSlidingMenu;
	private ImageView rightImage;
	private TextView message, setting;
	
	private AppListFragment mAppListFragment;
	private GridViewFragment mGridViewFragment;

	public DrawerView(Activity activity) {
		this.activity = activity;
	}

	public SlidingMenu initSlidingMenu() {
		localSlidingMenu = new SlidingMenu(activity);
		localSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置左右滑菜单
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// 设置要使菜单滑动，触碰屏幕的范围
		// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//设置了这个会获取不到菜单里面的焦点，所以先注释掉
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);// 使SlidingMenu附加在Activity右边
		// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//设置SlidingMenu菜单的宽度
		localSlidingMenu.setMenu(R.layout.sliding_left);// 设置menu的布局文件
		// localSlidingMenu.toggle();//动态判断自动关闭或开启SlidingMenu
		localSlidingMenu.setSecondaryMenu(R.layout.sliding_right);
		localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
		localSlidingMenu
				.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
					public void onOpened() {

					}
				});
		localSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {

			}
		});
		initView();
		return localSlidingMenu;
	}

	private void initView() {
		mAppListFragment = new AppListFragment();
		mGridViewFragment = new GridViewFragment();
		
		rightImage = (ImageView) localSlidingMenu
				.findViewById(R.id.right_image);
		rightImage.setOnClickListener(this);
		
		message = (TextView) localSlidingMenu.findViewById(R.id.menu1);
		setting = (TextView) localSlidingMenu.findViewById(R.id.menu2);
		message.setOnClickListener(this);
		setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_image:
			ToastUtils.show(activity, "右边");
			break;
		case R.id.menu1:
			((BaseActivity)activity).switchFragment(mAppListFragment, R.id.menu1);
			localSlidingMenu.showContent();
			((SlidingActivity)activity).setSlidingTitle("app");
			break;
		case R.id.menu2:
			((BaseActivity)activity).switchFragment(mGridViewFragment, R.id.menu2);
			localSlidingMenu.showContent();
			((SlidingActivity)activity).setSlidingTitle("gridview");
			break;
		}
	}
}
