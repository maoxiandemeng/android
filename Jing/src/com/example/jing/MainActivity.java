package com.example.jing;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.example.jing.base.BaseActivity;
import com.example.jing.fragment.BoardFragment;
import com.example.jing.fragment.ContactFragment;
import com.example.jing.fragment.MessageFragment;
import com.example.jing.fragment.NearbyFragment;
import com.example.jing.fragment.SettingFragment;
import com.example.jing.prefs.ThemePreference;
import com.example.jing.view.NavigationBar;
import com.example.jing.view.NavigationBar.NavigationItem;
import com.example.jing.view.NavigationBar.onTabSelectListern;

public class MainActivity extends BaseActivity implements onTabSelectListern {
	private NavigationBar mNavigationBar;

	private NearbyFragment mNearbyFragment;
	private BoardFragment mBoardFragment;
	private MessageFragment mMessageFragment;
	private ContactFragment mContactFragment;
	private SettingFragment mSettingFragment;

	private int mTheme = 1;
	private ThemePreference mPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPreference = ThemePreference.getThemePreference(this);
		if (savedInstanceState == null) {
			mTheme = getAppTheme(this);
		} else {
			mTheme = savedInstanceState.getInt("theme");
		}
		setTheme(mTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		initEvents();
		initBars();
		switchFragment(mNearbyFragment, R.id.tab1);
	}

	private void initBars() {
		ArrayList<NavigationItem> items = new ArrayList<NavigationItem>();
		int[] ids = { R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5 };
		int[] texts = { R.string.nearby, R.string.board, R.string.message,
				R.string.contact, R.string.setting, };
		int[] drawables = { R.drawable.ic_tab_nearby, R.drawable.ic_tab_site,
				R.drawable.ic_tab_chat, R.drawable.ic_tab_friend,
				R.drawable.ic_tab_profile };
		for (int i = 0; i < drawables.length; i++) {
			NavigationItem item = new NavigationItem(ids[i], texts[i],
					drawables[i]);
			items.add(item);
		}
		mNavigationBar.loadItems(items);
	}

	@Override
	public void setOnTabSelectListern(int id) {
		switch (id) {
		case R.id.tab1:
			switchFragment(mNearbyFragment, R.id.tab1);
			break;
		case R.id.tab2:
			switchFragment(mBoardFragment, R.id.tab2);
			break;
		case R.id.tab3:
			switchFragment(mMessageFragment, R.id.tab3);
			break;
		case R.id.tab4:
			switchFragment(mContactFragment, R.id.tab4);
			break;
		case R.id.tab5:
			switchFragment(mSettingFragment, R.id.tab5);
			break;
		}
	}

	@Override
	protected void initViews() {
		mNavigationBar = (NavigationBar) findViewById(R.id.mNavigationBar);
		mNearbyFragment = new NearbyFragment();
		mBoardFragment = new BoardFragment();
		mMessageFragment = new MessageFragment();
		mContactFragment = new ContactFragment();
		mSettingFragment = new SettingFragment();
	}

	@Override
	protected void initEvents() {
		mNavigationBar.setOnTabSelectListern(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mTheme == getAppTheme(this)) {
		} else {
			reload();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("theme", mTheme);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(Menu.NONE, R.id.item, Menu.NONE, "毛线");
		item.setIcon(R.drawable.cyberplayer_listbtn_pressed);
		MenuItemCompat.setShowAsAction(item,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
//		SubMenu subMenu = menu.addSubMenu("more");
//		subMenu.add(Menu.NONE, R.id.item, Menu.NONE, "连播").setIcon(
//				R.drawable.cyberplayer_listbtn_pressed);
//
//		MenuItem subMenu1Item = subMenu.getItem();
//		subMenu1Item.setIcon(R.drawable.cyberplayer_listbtn_pressed);// 右上角MenuItem缩略图
//		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
//				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.item) {
			switchAppTheme(this);
			reload();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void reload() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);// 不设置进入退出动画
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public int getAppTheme(Context context) {
		int value = mPreference.getThemeMode();
		switch (value) {
		case 1:
			return R.style.AppLight;
		case 2:
			return R.style.AppDark;
		}
		return R.style.AppLight;
	}

	public void switchAppTheme(Context context) {
		int value = mPreference.getThemeMode();
		switch (Integer.valueOf(value)) {
		case 1:
			mPreference.setThemeMode(2);
			;
			break;
		case 2:
			mPreference.setThemeMode(1);
			;
			break;
		}
	}

}
