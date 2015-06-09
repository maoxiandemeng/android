package com.example.jing.utils;

import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.TypedValue;

public class Helpers {

	/**
	 * �ж�SD���Ƿ����
	 * 
	 * @return
	 */
	public static boolean isExistSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * �ж��������ӵ�״̬
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * �����ֽڴ�С�õ���Ӧ��MB��GB
	 * 
	 * @param size
	 * @return
	 */
	public static String getSizeText(Context context, long size) {
		String sizeText = "";
		if (size >= 0) {
			sizeText = Formatter.formatFileSize(context, size);
		}
		return sizeText;
	}

	public static int dpToPx(float dp, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	public static int pxToDp(float dp, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp / scale + 0.5f);
	}

	/**
	 * SD��ʣ��ռ�
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ���е����ݿ������
		long freeBlocks = sf.getAvailableBlocks();
		// ����SD�����д�С
		// return freeBlocks * blockSize; //��λByte
		// return (freeBlocks * blockSize)/1024; //��λKB
		return (freeBlocks * blockSize) / 1024 / 1024; // ��λMB
	}

	/**
	 * SD��������
	 * 
	 * @return
	 */
	public static long getSDAllSize() {
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ��ȡ�������ݿ���
		long allBlocks = sf.getBlockCount();
		// ����SD����С
		// return allBlocks * blockSize; //��λByte
		// return (allBlocks * blockSize)/1024; //��λKB
		return (allBlocks * blockSize) / 1024 / 1024; // ��λMB
	}
}
