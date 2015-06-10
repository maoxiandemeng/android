package com.example.jing.view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jing.R;
import com.example.jing.base.BaseActivity;
import com.example.jing.fragment.MessageFragment;
import com.example.jing.fragment.SettingFragment;
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
	
	private MessageFragment mMessageFragment;
	private SettingFragment mSettingFragment;

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
		mMessageFragment = new MessageFragment();
		mSettingFragment = new SettingFragment();
		
		rightImage = (ImageView) localSlidingMenu
				.findViewById(R.id.right_image);
		rightImage.setOnClickListener(this);
		
		message = (TextView) localSlidingMenu.findViewById(R.id.message);
		setting = (TextView) localSlidingMenu.findViewById(R.id.setting);
		message.setOnClickListener(this);
		setting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_image:
			ToastUtils.show(activity, "�ұ�");
			break;
		case R.id.message:
			((BaseActivity)activity).switchFragment(mMessageFragment, R.id.tab3);
			localSlidingMenu.showContent();
			break;
		case R.id.setting:
			((BaseActivity)activity).switchFragment(mSettingFragment, R.id.tab5);
			localSlidingMenu.showContent();
			break;
		}
	}
}
