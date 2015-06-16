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
 * �Զ���SlidingMenu �����˵���
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
		localSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// �������һ��˵�
		localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ����Ҫʹ�˵�������������Ļ�ķ�Χ
		// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//������������ȡ�����˵�����Ľ��㣬������ע�͵�
		localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// ������ӰͼƬ�Ŀ��
		localSlidingMenu.setShadowDrawable(R.drawable.shadow);// ������ӰͼƬ
		localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu����ʱ��ҳ����ʾ��ʣ����
		localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu����ʱ�Ľ���̶�
		localSlidingMenu.attachToActivity(activity, SlidingMenu.RIGHT);// ʹSlidingMenu������Activity�ұ�
		// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//����SlidingMenu�˵��Ŀ��
		localSlidingMenu.setMenu(R.layout.sliding_left);// ����menu�Ĳ����ļ�
		// localSlidingMenu.toggle();//��̬�ж��Զ��رջ���SlidingMenu
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
			ToastUtils.show(activity, "�ұ�");
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
