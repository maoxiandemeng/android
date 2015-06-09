package com.example.jing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {

	private static final int CONNECT_TIMEOUT = 5*1000;
	private static final int READ_TIMEOUT = 5*1000;

	
	public static InputStream getInputStreamFromUrl(String urlStr){
		HttpURLConnection connection = null;
		InputStream is = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
//			connection.setConnectTimeout(CONNECT_TIMEOUT);
//			connection.setReadTimeout(READ_TIMEOUT);
			is = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	/**
	 * 根据urlStr从网络获取数据
	 * @param urlStr
	 * @return
	 */
	public static String getContentFromUrl(String urlStr){
		HttpURLConnection connection = null;
		BufferedReader br = null;
		try {
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(CONNECT_TIMEOUT);
			connection.setReadTimeout(READ_TIMEOUT);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(connection != null){
				connection.disconnect();
			}
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	/**
	 * 读取网络数据
	 * 
	 * @param _url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getContentFromUrl2(String url) {
		String result = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpUriRequest req = new HttpGet(url);
			HttpResponse resp = client.execute(req);
			HttpEntity ent = resp.getEntity();
			int status = resp.getStatusLine().getStatusCode();
			// If the status is equal to 200 ，that is OK
			if (status == HttpStatus.SC_OK) {
				result = EntityUtils.toString(ent);
				// Encode utf-8 to iso-8859-1
				// result = new String(result.getBytes("ISO-8859-1"), "UTF-8");
			}
			client.getConnectionManager().shutdown();
			return result;
		} catch (Exception e) {
			Log.e("NetHelper", "______________读取数据失败" + e.toString()
					+ "_____________");
			return "";
		}
	}
}
