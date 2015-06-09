package com.example.jing.utils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityUtils {
	public static final String EXTRA_BUNDLE = "bundle";
	
	public static void jumpActivity(Context context, Class<?> cls, Bundle bundle){
		Intent intent = new Intent(context, cls);
		if (bundle != null) {
			intent.putExtra(EXTRA_BUNDLE, bundle);
		}
		context.startActivity(intent);
	}
	
	public static void jumpActivity(Context context, Class<?> cls){
		jumpActivity(context, cls, null);
	}
}
