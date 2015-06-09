package com.example.jing.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baidu.cyberplayer.core.BMediaController;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.example.jing.MyApplication;
import com.example.jing.R;
import com.example.jing.base.BaseActivity;
import com.example.jing.constant.MyConstant;
import com.example.jing.entity.VideoInfo;
import com.example.jing.utils.ActivityUtils;

public class PlayVideoActivity extends BaseActivity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPlayingBufferCacheListener {
	public static final String TAG = "info";
	private RelativeLayout mViewHolder;
	private LinearLayout mControllerHolder;

	private BVideoView mVV = null;
	private BMediaController mVVCtl = null;
	private String mVideoSource = null;
	private ArrayList<VideoInfo> mInfos;

	// ����״̬
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	// ������Ƶ��id
	private int mId;
	// ��¼����λ��
	private int mLastPos = 0;
	private int mPosition = 0;

	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "PlayVideoActivity";

	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();
	private final int EVENT_PLAY = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_playvideo);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
		if (getIntent() == null) {
			return;
		}
		mInfos = MyApplication.getInstance().getmInfos();
		mId = getIntent().getBundleExtra(ActivityUtils.EXTRA_BUNDLE).getInt(
				MyConstant.VIDEO_INFO_ID);
		mVideoSource = getDataById(mId);
		mPosition = getPositionById(mId);
		Log.i("info", "mVideoSource--" + mVideoSource);
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		mViewHolder = (RelativeLayout) findViewById(R.id.view_holder);
		mControllerHolder = (LinearLayout) findViewById(R.id.controller_holder);

		// ����ak��sk��ǰ16λ
		BVideoView.setAKSK(MyConstant.AK, MyConstant.SK);

		// ����BVideoView��BMediaController
		mVV = new BVideoView(this);
		mVVCtl = new BMediaController(this);
		mViewHolder.addView(mVV);
		mControllerHolder.addView(mVVCtl);

	}

	@Override
	protected void initEvents() {
		// ע��listener
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);
		mVVCtl.setPreNextListener(mPreListener, mNextListener);

		// ����BMediaController
		mVV.setMediaController(mVVCtl);
		// ���ý���ģʽ
		mVV.setDecodeMode(BVideoView.DECODE_SW);
		// initSubtitleSetting();

		// ������̨�¼������߳�
		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());
	}

	private String getDataById(int id) {
		String data = null;
		for (int i = 0; i < mInfos.size(); i++) {
			if (id == mInfos.get(i).getId()) {
				data = mInfos.get(i).getData();
			}
		}
		return data;
	}

	private int getPositionById(int id) {
		int position = 0;
		for (int i = 0; i < mInfos.size(); i++) {
			if (id == mInfos.get(i).getId()) {
				position = i;
			}
		}
		return position;
	}

	/**
	 * ʵ���л�ʾ��
	 */
	private View.OnClickListener mPreListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(TAG, "pre btn clicked");
			if (mPosition <= 0) {
				mPosition = 0;
			}else {
				mPosition--;
			}
			mVideoSource = mInfos.get(mPosition).getData();
			// ����Ѿ��������ţ���ֹͣ����
			if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
				mVV.stopPlayback();
			}

			// ����һ���µĲ�������
			if (mEventHandler.hasMessages(EVENT_PLAY))
				mEventHandler.removeMessages(EVENT_PLAY);
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}
	};

	private View.OnClickListener mNextListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(TAG, "next btn clicked");
			if (mPosition >= mInfos.size() - 1) {
				mPosition = 0;
			}else {
				mPosition++;
			}
			mVideoSource = mInfos.get(mPosition).getData();
			// ����Ѿ��������ţ���ֹͣ����
			if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
				mVV.stopPlayback();
			}

			// ����һ���µĲ�������
			if (mEventHandler.hasMessages(EVENT_PLAY))
				mEventHandler.removeMessages(EVENT_PLAY);
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * ��ֹͣ����ǰ ������ȼ�¼��ǰ���ŵ�λ��,�Ա��Ժ��������
		 */
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@SuppressLint("Wakelock")
	@Override
	protected void onResume() {
		super.onResume();
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		// ����һ�β�������,��Ȼ����һ��Ҫ���ⷢ��
		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ������̨�¼������߳�
		mHandlerThread.quit();
	}

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				// ����Ѿ������ˣ��ȴ���һ�β��Ž���
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				// ���ò���url
				mVV.setVideoPath(mVideoSource);

				// �����������Ҫ���
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}

				// ��ʾ�������ػ�����ʾ
				mVV.showCacheInfo(true);

				// ��ʼ����
				mVV.start();

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * ����׼������
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
	}

	/**
	 * �������
	 */
	@Override
	public void onCompletion() {
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	}

	/**
	 * ���ų���
	 */
	@Override
	public boolean onError(int arg0, int arg1) {
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	@Override
	public boolean onInfo(int what, int extra) {
		switch (what) {
		// ��ʼ����
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		// ��������
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			break;
		}
		return false;
	}

	/**
	 * ��ǰ����İٷֱȣ� �������onInfo�еĿ�ʼ����ͽ�����������ʾ�ٷֱȵ�����
	 */
	@Override
	public void onPlayingBufferCache(int percent) {

	}

}
