package com.example.jing.thread;

import android.os.Handler;
import android.os.Message;

public class TimerHandler extends Handler {
	private onTimerListener onTimerListener;
	private long delay;

	public TimerHandler(onTimerListener onTimerListener, long delay) {
		super();
		this.onTimerListener = onTimerListener;
		this.delay = delay;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (onTimerListener != null) {
			onTimerListener.setOnTimer();
		}
		sendEmptyMessageDelayed(0, delay);
	}

	public interface onTimerListener {
		public void setOnTimer();
	}

	public void setOnTimerListener(onTimerListener onTimerListener) {
		this.onTimerListener = onTimerListener;
	}
}
