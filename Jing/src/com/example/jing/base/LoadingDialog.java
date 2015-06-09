package com.example.jing.base;

import com.example.jing.R;
import com.example.jing.view.FlippingImageView;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingDialog extends BaseDialog {
	private FlippingImageView mImageView;
	private ImageView mLoadingImage;

	public LoadingDialog(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		setContentView(R.layout.dialog_loading);
//		mImageView = (FlippingImageView) findViewById(R.id.flip_image);
//		mImageView.startAnimation();
		mLoadingImage = (ImageView) findViewById(R.id.loading_image);
		Animation animation = AnimationUtils.loadAnimation(context, R.drawable.loading_rotate);
		mLoadingImage.setAnimation(animation);
	}
	
	public void clear(){
		mLoadingImage.clearAnimation();
	}

}
