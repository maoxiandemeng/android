package com.example.jing.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import com.example.jing.base.BaseActivity;
import com.example.jing.entity.Common;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class ApiRequest<T> extends AsyncTask<String, String, Integer> {
	private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	public static final int FORCE_UPDATE = -4;
	public static final int SERVICE_MAINTAINCE = -4004;
	public static final int SERVICE_MESSAGE = -6;
	public static final int NOT_LOGIN = -3;

	private static final int HTTP_NETWORK_SUCCESS = 0;
	private static final int HTTP_NETWORK_FAIL = -2;
	private static final int PARSE_EXCEPTION_ERROR = -3;
	private static final int IO_EXCEPTION_ERROR = -4;
	private static final int UNKNOWN_ERROR = -1;

	private WeakReference<Context> mContext;
	private WeakReference<BaseActivity> mActivity;
	private WeakReference<Fragment> mFragment;

	private DefaultHttpClient mHttpClient;
	private HttpRequestBase mHttpRequest;
	
	private T mResponseData;

	public ApiRequest(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("null context");
		}
		init(context);
	}

	public ApiRequest(Fragment fragment) {
		if (fragment == null) {
			throw new IllegalArgumentException("null fragment");
		}
		mFragment = new WeakReference<Fragment>(fragment);
		init(fragment.getActivity());
	}

	private void init(Context context) {
		mContext = new WeakReference<Context>(context);
		
		HttpParams params = new BasicHttpParams();

        ConnManagerParams.setTimeout(params, DEFAULT_SOCKET_TIMEOUT);

        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSoTimeout(params, DEFAULT_SOCKET_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(params, DEFAULT_SOCKET_TIMEOUT);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		mHttpClient = new DefaultHttpClient();
		mHttpClient.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest httpRequest, HttpContext httpContext)
					throws HttpException, IOException {
				if (!httpRequest.containsHeader(HEADER_ACCEPT_ENCODING)) {
					httpRequest
							.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
				}
			}
		});

		mHttpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse httpResponse,
					HttpContext httpContext) {
				final HttpEntity entity = httpResponse.getEntity();
				if (entity == null) {
					return;
				}
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
							httpResponse.setEntity(new InflatingEntity(entity));
							break;
						}
					}
				}
			}
		});
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null && mActivity.get() != null) {
			mActivity.get().showLoadingDialog();
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		try {
			HttpResponse response = mHttpClient.execute(mHttpRequest);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				//Long contentLength = response.getEntity().getContentLength();
				InputStream is = response.getEntity().getContent();
				InputStreamReader reader = new InputStreamReader(is, "utf-8");
				JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
				ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
				Gson gson = new Gson();
				mResponseData = gson.fromJson(jsonObject, (Class<T>)type.getActualTypeArguments()[0]);
				return HTTP_NETWORK_SUCCESS;
//				GsonHelper.getAsJsonObject(jsonObject.get("records"), null);
			}else {
				return HTTP_NETWORK_FAIL;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return IO_EXCEPTION_ERROR;
		}
		return UNKNOWN_ERROR;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (mActivity != null && mActivity.get() != null) {
			mActivity.get().hideLoadingDialog();
		}
		if (mContext != null) {
			Context context = mContext.get();
			if (context == null) {
				return;
			}
			if (context instanceof BaseActivity) {
				if (((BaseActivity) context).isFinishing()) {
					return;
				}
			}
		}
		if (mFragment != null) {
			Fragment fragment = mFragment.get();
			if (fragment == null || !fragment.isAdded() || fragment.getView() == null) {
				return;
			}
			if (fragment.getActivity() == null || fragment.getActivity().isFinishing()) {
				return;
			}
		}
		if (result.intValue() == HTTP_NETWORK_SUCCESS) {
			onSuccess(mResponseData);
		}else {
			if (mContext != null && mContext.get() != null) {
				if (!Helpers.isNetworkConnected(mContext.get())) {
					ToastUtils.show(mContext.get(), "当前网络不可访问，请重新尝试！");
				}
			}
			onFail("网络访问异常，请稍后重试！");
		}
	}

	public void doRequest(BaseActivity base, String api, Common.HttpEntityBuilder inputBuilder) {
		mActivity = new WeakReference<BaseActivity>(base);

		try {
			HttpPost httpPost = new HttpPost(api);
			HttpEntity httpEntity = inputBuilder.buildEntity();
			httpPost.setEntity(httpEntity);
			mHttpRequest = httpPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		this.execute("");
	}
	
	protected void onSuccess(T responseData){
		
	}
	
	protected void onFail(String message){
		
	}
	
	private static class InflatingEntity extends HttpEntityWrapper {

        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }
    }

}
