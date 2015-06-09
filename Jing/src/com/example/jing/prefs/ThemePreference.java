package com.example.jing.prefs;

import android.content.Context;

import com.example.jing.base.BaseSharePreference;

public class ThemePreference extends BaseSharePreference {
	public static final String THEME = "theme";
	public static final String THEME_MODE = "theme_mode";
	public static final int THEME_LIGHT = 1;//°×Ìì
	public static final int THEME_DARK = 2;//ÍíÉÏ
	
	private static ThemePreference mThemePreference;

	protected ThemePreference(Context context) {
		super(context, THEME);
	}
	
	public static ThemePreference getThemePreference(Context context){
		if (mThemePreference == null) {
			mThemePreference = new ThemePreference(context);
		}
		return mThemePreference;
	}
	
	public void setThemeMode(int mode){
		putInt(THEME_MODE, mode);
	}
	
	public int getThemeMode(){
		return getInt(THEME_MODE, 1);
	}

}
