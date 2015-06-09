package com.example.jing.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ChartView extends View {
	private float width;
	private float heigh;
	
	private int lineY = 2; //Y方向画线的个数
	private int lineX = 1; //X方向画线的个数
	private ArrayList<Float> list;
	
	private float max;
	private float min;
	private int rectCount;
	private float gap = 1f;
	
	public ChartView(Context context) {
		super(context);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = getWidth();
		heigh = getHeight();
		Log.i("info", "width:" + width + "heigh:" + heigh);
		drawLine(canvas);
		drawRect(canvas);
		drawY(canvas);
	}
	
	/**
	 * 画边框
	 * @param canvas
	 */
	private void drawLine(Canvas canvas){
		Paint mPaint = new Paint();
		mPaint.setColor(Color.GRAY);
		mPaint.setAntiAlias(true);
		
		canvas.drawLine(0, 0, width, 0, mPaint);
		canvas.drawLine(0, 0, 0, heigh, mPaint);
		canvas.drawLine(width, 0, width, heigh, mPaint);
		canvas.drawLine(0, heigh, width, heigh, mPaint);
		
//		float stickX = width / (lineY + 1);
//		for (int i = 0; i < lineY; i++) {
//			stickX = (i+1)*stickX;
//			canvas.drawLine(stickX, 0, stickX, heigh, mPaint);
//		}
//		
//		float stickY = heigh / (lineX + 1);
//		for (int i = 0; i < lineY; i++) {
//			stickY = (i+1)*stickY;
//			canvas.drawLine(0, stickY, width, stickY, mPaint);
//		}
	}
	
	private void drawY(Canvas canvas){
		Paint mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setAntiAlias(true);
		
		Rect rect = new Rect();
		mPaint.getTextBounds(String.valueOf(min), 0, String.valueOf(min).length(), rect);
		int textHeight = rect.height();
		
		canvas.drawText(String.valueOf(min), 2, heigh - textHeight, mPaint);
		canvas.drawText(String.valueOf(getRealityY((max - min)/2)), 2, getRealityY((max - min)/2) + textHeight/2, mPaint);
		canvas.drawText(String.valueOf(heigh), 2, textHeight * 2, mPaint);
	}
	
	/**
	 * 画柱状图
	 * @param canvas
	 */
	private void drawRect(Canvas canvas) {
		if (list.isEmpty()) {
			return;
		}
		Paint mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setAntiAlias(true);
		float rectWidth = (width - (rectCount - 1) * gap) / rectCount;
		float left = 0;
		for (int i = 0; i < list.size(); i++) {
			float top = getRealityY(list.get(i));
			canvas.drawRect(left, heigh, left + rectWidth, top, mPaint);
			left = left + rectWidth + gap;
		}
	}
	
	/**
	 * 通过一个点Y方向得到对应实际点的坐标
	 * @return
	 */
	private Float getRealityY(float f){
		return heigh - (f * heigh / max);
	}

	public int getLineY() {
		return lineY;
	}

	public void setLineY(int lineY) {
		this.lineY = lineY;
	}

	public int getLineX() {
		return lineX;
	}

	public void setLineX(int lineX) {
		this.lineX = lineX;
	}

	public ArrayList<Float> getList() {
		return list;
	}

	public void setList(ArrayList<Float> list) {
		this.list = list;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public int getRectCount() {
		return rectCount;
	}

	public void setRectCount(int rectCount) {
		this.rectCount = rectCount;
	}

}
