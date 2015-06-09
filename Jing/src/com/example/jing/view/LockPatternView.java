package com.example.jing.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jing.R;

/**
 * 九宫格解锁
 * 
 * @author
 *
 */
public class LockPatternView extends View {
	private Bitmap lockNormalBitmap, lockPressedBitmap, lockErrorBitmap;
	private Bitmap linePressedBitmap, lineErrorBitmap;
	private Point[][] points = new Point[3][3];
	private boolean isInit; // 是否初始化
	private boolean isSelect; // 是否选中
	private boolean isFinish; // 是否完成
	private boolean movingNoPoint; // 是否是九宫格里的点
	private float width, height, offsetX, offsetY, bitmapRadius, movingX,
			movingY;

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Point> pointList = new ArrayList<Point>(); // 按下点的集合
	private Matrix matrix = new Matrix();

	public LockPatternView(Context context) {
		super(context);
	}

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!isInit) {
			initPoints();
		}
		drawPoints(canvas);
		if (pointList.size() > 0) {
			Point a = pointList.get(0);
			for (int i = 0; i < pointList.size(); i++) {
				Point b = pointList.get(i);
				drawLine(canvas, a, b);
				a = b;
			}
			// 绘制九宫格外的点
			if (movingNoPoint) {
				drawLine(canvas, a, new Point(movingX, movingY));
			}
		}
	}

	/**
	 * 初始化点
	 */
	private void initPoints() {
		width = getWidth();
		height = getHeight();

		// 横屏
		if (width > height) {
			offsetX = (width - height) / 2;
			width = height;
		} else {// 竖屏
			offsetY = (height - width) / 2;
			height = width;
		}

		// 初始化图片
		lockNormalBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lock_normal);
		lockPressedBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lock_pressed);
		lockErrorBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lock_error);
		linePressedBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.line_pressed);
		lineErrorBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.line_error);

		// 点的坐标
		points[0][0] = new Point(offsetX + width / 4, offsetY + width / 4);
		points[0][1] = new Point(offsetX + width / 2, offsetY + width / 4);
		points[0][2] = new Point(offsetX + width - width / 4, offsetY + width
				/ 4);

		points[1][0] = new Point(offsetX + width / 4, offsetY + width / 2);
		points[1][1] = new Point(offsetX + width / 2, offsetY + width / 2);
		points[1][2] = new Point(offsetX + width - width / 4, offsetY + width
				/ 2);

		points[2][0] = new Point(offsetX + width / 4, offsetY + width - width
				/ 4);
		points[2][1] = new Point(offsetX + width / 2, offsetY + width - width
				/ 4);
		points[2][2] = new Point(offsetX + width - width / 4, offsetY + width
				- width / 4);

		bitmapRadius = lockNormalBitmap.getWidth() / 2;
		
		int index = 1;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				point.index = index;
				index++;
			}
		}
		
		isInit = true;
	}

	/**
	 * 画点
	 * 
	 * @param canvas
	 */
	private void drawPoints(Canvas canvas) {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (point.state == Point.STATE_NORMAL) {
					canvas.drawBitmap(lockNormalBitmap, point.x - bitmapRadius,
							point.y - bitmapRadius, paint);
				} else if (point.state == Point.STATE_PRESSED) {
					canvas.drawBitmap(lockPressedBitmap,
							point.x - bitmapRadius, point.y - bitmapRadius,
							paint);
				} else if (point.state == Point.STATE_ERROR) {
					canvas.drawBitmap(lockErrorBitmap, point.x - bitmapRadius,
							point.y - bitmapRadius, paint);
				}
			}
		}
	}

	private void drawLine(Canvas canvas, Point a, Point b) {
		float lineLength = (float) Point.distance(a, b);
		float degrees = getDegree(a, b);
		canvas.rotate(degrees, a.x, a.y);
		if (a.state == Point.STATE_PRESSED) {
			matrix.setScale(lineLength / linePressedBitmap.getWidth(), 1);
			matrix.postTranslate(a.x, a.y
					- linePressedBitmap.getHeight() / 2);
			canvas.drawBitmap(linePressedBitmap, matrix, paint);
		} else {
			matrix.setScale(lineLength / lineErrorBitmap.getWidth(), 1);
			matrix.postTranslate(a.x, a.y
					- lineErrorBitmap.getHeight() / 2);
			canvas.drawBitmap(lineErrorBitmap, matrix, paint);
		}
		canvas.rotate(-degrees, a.x, a.y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		movingNoPoint = false;
		isFinish = false;
		movingX = event.getX();
		movingY = event.getY();

		Point point = null;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			resetPoint();

			point = checkSelectPoint();
			if (point != null) {
				isSelect = true;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			movingNoPoint = true;
			if (isSelect) {
				point = checkSelectPoint();
			}
			break;

		case MotionEvent.ACTION_UP:
			isFinish = true;
			isSelect = false;
			break;
		}
		// 选中重置检查
		if (!isFinish && isSelect && point != null) {
			// 交叉点
			if (crossPoint(point)) {
				movingNoPoint = true;
			} else { // 新点
				point.state = Point.STATE_PRESSED;
				pointList.add(point);
			}
		}

		// 绘制结束
		if (isFinish) {
			// 绘制不成立
			if (pointList.size() == 1) {
				resetPoint();
			} else if (pointList.size() < 5 && pointList.size() > 1) { // 绘制错误
				errorPoint();
			}
		}
		// 刷新界面
		postInvalidate();

		return true;
	}

	/**
	 * 检查点是否选中
	 * 
	 * @return
	 */
	private Point checkSelectPoint() {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (Point
						.with(point.x, point.y, bitmapRadius, movingX, movingY)) {
					return point;
				}
			}
		}
		return null;
	}

	/**
	 * 重置点的集合
	 */
	private void resetPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_NORMAL;
		}
		pointList.clear();
	}

	/**
	 * 错误点
	 */
	private void errorPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_ERROR;
		}
	}

	/**
	 * 判断pointList集合中是否包含这个点
	 * 
	 * @param point
	 * @return
	 */
	private boolean crossPoint(Point point) {
		if (pointList.contains(point)) {
			return true;
		}
		return false;
	}

	/**
	 * 获得两点的角度
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private float getDegree(Point a, Point b) {
		float beforeX = a.x;
		float beforeY = a.y;
		float afterX = b.x;
		float afterY = b.y;
		float x = beforeX - afterX;
		float y = beforeY - afterY;
		float de1 = (float) Math.toDegrees(Math.atan2(y, x));
		float degree = 180 + de1;
		return degree;
	}

	/**
	 * 
	 * 自定义点
	 * 
	 * 
	 */
	public static class Point {
		public static final int STATE_NORMAL = 0;
		public static final int STATE_PRESSED = 1;
		public static final int STATE_ERROR = 2;
		public int state = STATE_NORMAL;
		public int index;
		public float x, y;

		public Point() {

		}

		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * 计算两点之间的距离
		 * 
		 * @param a
		 * @param b
		 * @return
		 */
		public static double distance(Point a, Point b) {
			return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x)
					+ Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
		}

		/**
		 * 判断点是否重合
		 * 
		 * @param pointX
		 *            参考点的x
		 * @param pointY
		 *            参考点的y
		 * @param r
		 *            圆的半径
		 * @param movingX
		 *            移动点的x
		 * @param movingY
		 *            移动点的x
		 * @return
		 */
		public static boolean with(float pointX, float pointY, float r,
				float movingX, float movingY) {
			return Math.sqrt(Math.abs(movingX - pointX)
					* Math.abs(movingX - pointX) + Math.abs(movingY - pointY)
					* Math.abs(movingY - pointY)) < r;
		}
	}

}
