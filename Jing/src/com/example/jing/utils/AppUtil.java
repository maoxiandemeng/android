package com.example.jing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppUtil {
	/**
	 * Stringת��Ϊʱ��
	 * @param str
	 * @return
	 */
	public static Date ParseDate(String str){
		SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		Date addTime = null;
		try {
			addTime = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return addTime;
	}
	/**
	 * ������ת��Ϊ�ַ���
	 * @param date
	 * @return
	 */
	public static String ParseDateToString(Date date){
		
		return ParseDateToString(date,"yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * ������ת��Ϊ�ַ��������أ�
	 * @param date
	 * @param format:ʱ���ʽ���������yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String ParseDateToString(Date date,String format){
		SimpleDateFormat dateFormat =new SimpleDateFormat(format);
 		return dateFormat.format(date);
	}
	/**
	 * ��UMTʱ��ת��Ϊ����ʱ��
	 * @param str
	 * @return
	 * @throws ParseException 
	 */
	public static Date ParseUTCDate(String str){
		//��ʽ��2012-03-04T23:42:00+08:00
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",Locale.CHINA);
		try {
			Date date = formatter.parse(str);
			return date;
		} catch (ParseException e) {
			//��ʽ��Sat, 17 Mar 2012 11:37:13 +0000
			//Sat, 17 Mar 2012 22:13:41 +0800
			try{
				SimpleDateFormat formatter2=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.CHINA);
				Date date2 = formatter2.parse(str);

				return date2;
			}catch(ParseException ex){
				return null;
			}
		}		
	}
	/**
	 * ��ȡ����ͼƬȡDrawable
	 * @param url
	 * @return
	 */
	public static Drawable GetUrlDrawable(String url){
		try{			
			URL aryURI=new URL(url);
			URLConnection conn=aryURI.openConnection();
			InputStream is=conn.getInputStream();
			Bitmap bmp=BitmapFactory.decodeStream(is);
			return new BitmapDrawable(bmp);
		}catch(Exception e){
			Log.e("ERROR", "urlImage2Drawable���������쳣��imageUrl��" + url, e);
			return null;
		}
	}
	/**
	 * �������ַ����Bitmap
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap GetBitmap(String imageUrl){   
		Bitmap mBitmap = null;   
		try {   
			URL url = new URL(imageUrl);   
			URLConnection conn=url.openConnection();
			InputStream is = conn.getInputStream();   
			mBitmap = BitmapFactory.decodeStream(is);   
		} catch (MalformedURLException e) {   
			e.printStackTrace();   
		} catch (IOException e) {   
			e.printStackTrace();   
		}   
		return mBitmap;   
	}
	/**
	 * Drawableת��ΪBitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap DrawableToBitmap(Drawable drawable) {  
		try {  
			Bitmap bitmap = Bitmap  
					.createBitmap(  
							drawable.getIntrinsicWidth(),  
							drawable.getIntrinsicHeight(),  
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
									: Bitmap.Config.RGB_565);  
			Canvas canvas = new Canvas(bitmap);  
			// canvas.setBitmap(bitmap);  
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable  
					.getIntrinsicHeight());  
			drawable.draw(canvas);  

			return bitmap;  
		} catch (OutOfMemoryError e) {  
			e.printStackTrace();  
			return null;  
		}  
	} 
	//	/**
	//	 * �˳�����
	//	 * @param context
	//	 */
	//    public static void QuitHintDialog(final Context context){
	//    	new AlertDialog.Builder(context)
	//    	.setMessage(R.string.sys_ask_quit_app)
	//    	.setTitle(R.string.com_dialog_title_quit)
	//    	.setIcon(R.drawable.icon)
	//    	.setPositiveButton(R.string.com_btn_ok,new DialogInterface.OnClickListener() {
	//			
	//			@Override
	//			public void onClick(DialogInterface dialog, int which) {
	//				try{
	//					((Activity)context).finish();
	//				}catch(Exception e){
	//					Log.e("close","+++++++++++++����+++++++++");
	//				}
	//			}
	//		})
	//		.setNegativeButton(R.string.com_btn_cancel, new DialogInterface.OnClickListener() {				
	//			@Override
	//			public void onClick(DialogInterface dialog, int which) {
	//			}
	//		}).show();
	//    }
	/** 
	 * �������汾��
	 */
	public static int GetVersionCode(final Context con) {
		int version = 1;
		PackageManager packageManager = con.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
			version = packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
	/**
	 * ����������
	 * @param context
	 * @return
	 */
	public static String GetVersionName(final Context context){
		String versionName = null;
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	/**
	 * ����html�����ַ�
	 * @param str
	 * @return
	 */
	public static String HtmlToText(String str){
		str=str.replace("<br />", "\n");
		str=str.replace("<br/>", "\n");
		str=str.replace("&nbsp;&nbsp;", "\t");
		str=str.replace("&nbsp;", " ");
		str=str.replace("&#39;","\\");
		str=str.replace("&quot;", "\\");
		str=str.replace("&gt;",">");
		str=str.replace("&lt;","<");
		str=str.replace("&amp;", "&");

		return str;
	}
	/**
	 * ��ʱ��ת��Ϊ����
	 * @param datetime
	 * @return
	 */
	public static String DateToChineseString(Date datetime){
		Date today=new Date();
		long   seconds   =   (today.getTime()-   datetime.getTime())/1000; 

		long year=	seconds/(24*60*60*30*12);// �������
		long   month  =   seconds/(24*60*60*30);//�������
		long   date   =   seconds/(24*60*60);     //�������� 
		long   hour   =   (seconds-date*24*60*60)/(60*60);//����Сʱ�� 
		long   minute   =   (seconds-date*24*60*60-hour*60*60)/(60);//���ķ����� 
		long   second   =   (seconds-date*24*60*60-hour*60*60-minute*60);//�������� 

		if(year>0){
			return year + "��ǰ";
		}
		if(month>0){
			return month + "��ǰ";
		}
		if(date>0){
			return date + "��ǰ";
		}
		if(hour>0){
			return hour + "Сʱǰ";
		}
		if(minute>0){
			return minute + "����ǰ";
		}
		if(second>0){
			return second + "��ǰ";
		}
		return "δ֪ʱ��";
	}
	/**
	 * �ж��Ƿ�cnblogs�ڲ����ӣ�����0�������
	 * 	��ʽ��http://www.cnblogs.com/walkingp/archive/2011/05/27/2059420.html
	 * @param url
	 * @return
	 */
	public static int GetCnblogsBlogLinkId(String url){
		Pattern pattern=Pattern.compile("http://www.cnblogs.com/(.+?)/archive/(\\d+?)/(\\d+?)/(\\d+?)/(\\d+?).html");
		Matcher m=pattern.matcher(url);
		int id=0;
		while(m.find()){
			id=Integer.parseInt(m.group(5));
		}
		return id;
	}
	/**
	 * ��ʽ�����ݣ����ڲ������ݼ��������ݣ�
	 */	
	public static String FormatContent(String html,boolean isImgMode){
		//�Ƿ�ͼƬģʽ

		if(!isImgMode){
			html=AppUtil.ReplaceImgTag(html);
			html=AppUtil.ReplaceVideoTag(html);
		}

		return html;
	}
	
	/**
	 * ��ʽ����������
	 * @param content
	 * @return
	 */
	static final Pattern patternLine=Pattern.compile("<br/>");
	public static String formatCommentContent(String content){
		content = replaceLineTag(content);
		content = replaceSource(content);
		return content;
	}
	/**
	 * ��<br/>�滻Ϊ���з� "\n"
	 * @param content
	 * @return
	 */
	public static String replaceLineTag(String content){
		Matcher m=patternLine.matcher(content);
		while(m.find()){
			content = m.replaceAll("\n");
		}
		return content;
	}
	static final Pattern patternSource1 =Pattern.compile("��Դ(.+?)\\)");
	static final Pattern patternSource2 =Pattern.compile("<a(.+?)��Դ(.+?)>");
	private static final String TAG = "AppUtil";
	
	public static String replaceSource(String content){
		Matcher m1 = patternSource1.matcher(content);
		String sourceTag = "";
		if(m1.find()){
			sourceTag = m1.group(0);
		}
		Matcher m2 = patternSource2.matcher(content);
		while(m2.find()){
			content = m2.replaceAll("#��Դ#");
		}
		content = content.replace("#��Դ#",sourceTag);
		Log.d(TAG, "content="+content);
		return content;
	}
	
	static final Pattern patternHtml=Pattern.compile("<.+?>");
	/**
	 * �Ƴ�html���
	 * @param html
	 * @return
	 */
	public static String RemoveHtmlTag(String html){
		Matcher m=patternHtml.matcher(html);
		while(m.find()){
			html= m.replaceAll("");
		}
		return html;
	}
	/**
	 * �ж��Ƿ���ͼƬ����
	 * @param html
	 * @return
	 */
	static final Pattern patternImg=Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)(onload=\"(.+?)\")?([^\"]+?)>");
	public static boolean IsContainImg(String html){
		Matcher m=patternImg.matcher(html);
		while(m.find()){
			return true;
		}
		return false;
	}
	/**
	 * �Ƴ�ͼƬ���
	 * @param html
	 * @return
	 */
	public static String RemoveImgTag(String html){
		Matcher m=patternImg.matcher(html);
		while(m.find()){
			html= m.replaceAll("");
		}
		return html;
	}
	/**
	 * �滻ͼƬ��� 
	 * @param html
	 * @return
	 */
	//<img style="border: 0px currentColor;" src="http://www.umlonline.org/attachments/2009/09/2_2009091514491024WHA.png" alt="" width="848" height="253" />
	static final Pattern patternImgSrc=Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)>");
	public static String ReplaceImgTag(String html){
		Matcher m=patternImgSrc.matcher(html);
		while(m.find()){
			html= m.replaceAll("��<a href=\"$2\">����鿴ͼƬ</a>��");
		}
		return html;
	}
	/**
	 * �Ƴ���Ƶ���
	 */
	static final Pattern patternVideo=Pattern.compile("<object(.+?)>(.*?)<param name=\"src\" value=\"(.+?)\"(.+?)>(.+?)</object>");
	public static String RemoveVideoTag(String html){
		Matcher m=patternVideo.matcher(html);
		while(m.find()){
			html= m.replaceAll("");
		}
		return html;
	}
	/**
	 * �滻��Ƶ���
	 */
	static final Pattern patternVideoSrc=Pattern.compile("<object(.+?)>(.*?)<param name=\"src\" value=\"(.+?)\"(.+?)>(.+?)</object>");
	public static String ReplaceVideoTag(String html){
		Matcher m=patternVideoSrc.matcher(html);
		while(m.find()){
			html= m.replaceAll("��<a href=\"$3\">����鿴��Ƶ</a>��");
		}
		return html;
	}

	/**
	 * ����������ȡString�ַ���������
	 * @param in
	 * @return
	 */
	public static String getStrFromInputStream(InputStream in){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line=br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();

				}
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
