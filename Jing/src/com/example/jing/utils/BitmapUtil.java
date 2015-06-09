package com.example.jing.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

public class BitmapUtil {

	public static final String cachesPath = 
			Environment.getExternalStorageDirectory().toString() + File.separator 
			+"Jing" + File.separator +"image" + File.separator;
	public static final int QUALITY = 100;
	public static Bitmap getBitmap(String filePath){
		
		return BitmapFactory.decodeFile(filePath);
	}
	/**
	 * 通过BitmapFactory.Options压缩图片
	 * @param pathName>>图片的绝对路径
	 * @param size >>压缩系数 ，  size=3代表压缩至1/9
	 * @return
	 */
	public static Bitmap getBitmap(String pathName,int size){
		Bitmap bitmap = null;
		BitmapFactory.Options bfOptions;
		bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inTempStorage = new byte[56*1024];
		bfOptions.inSampleSize = size;
		bitmap = BitmapFactory.decodeFile(pathName, bfOptions);
		return bitmap;
	}
	public static Options getOptions(int size){
		BitmapFactory.Options bfOptions;
		bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inTempStorage = new byte[56*1024];
		bfOptions.inSampleSize = size;
		return bfOptions;
	}
	public static Bitmap bitmapCompress(Bitmap bitmap,int newW,int newH){
		int oldW = bitmap.getWidth();
		int oldH = bitmap.getHeight();
		System.out.println(oldW+","+oldH);
		Matrix matrix = new Matrix();
		matrix.postScale(0.001f, 0.001f);
//		matrix.postScale(((float)newW)/oldW, ((float)newH)/oldH);
		return Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true);
	}
	
	public static void saveBitmap(Bitmap bitmap,String imageUrl){
		if (!Helpers.isExistSDCard()) {
			return;
		}
		File file = new File(cachesPath + MD5Util.MD5(imageUrl) + ".jpg");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, QUALITY, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static boolean bitmapExists(String imageUrl){
		if (!Helpers.isExistSDCard()) {
			return false;
		}
		File file = new File(cachesPath + MD5Util.MD5(imageUrl) + ".jpg");
		return file.exists();
		
	}
	
	public static Bitmap getBitmapFromSDCard(String imageUrl){
		if (!Helpers.isExistSDCard()) {
			return null;
		}
		return BitmapFactory.decodeFile(cachesPath + MD5Util.MD5(imageUrl) + ".jpg");
		
	}

	
	/** 
	 * 转换图片成圆形 
	 * @param bitmap 传入Bitmap对象 
	 * @return 
	 */ 
	public static Bitmap toRoundBitmap(Bitmap bitmap) { 
		int width = bitmap.getWidth(); 
		int height = bitmap.getHeight(); 
		float roundPx; 
		float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom; 
		if (width <= height) { 
			roundPx = width / 2; 
			top = 0; 
			bottom = width; 
			left = 0; 
			right = width; 
			height = width; 
			dst_left = 0; 
			dst_top = 0; 
			dst_right = width; 
			dst_bottom = width; 
		} else { 
			roundPx = height / 2; 
			float clip = (width - height) / 2; 
			left = clip; 
			right = width - clip; 
			top = 0; 
			bottom = height; 
			width = height; 
			dst_left = 0; 
			dst_top = 0; 
			dst_right = height; 
			dst_bottom = height; 
		} 
		Bitmap output = Bitmap.createBitmap(width, 
				height, Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 
		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom); 
		final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom); 
		final RectF rectF = new RectF(dst); 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, src, dst, paint); 
		return output; 
	} 
}
