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
 * �Ź������
 * 
 * @author
 *
 */
public class LockPatternView extends View {
	private Bitmap lockNormalBitmap, lockPressedBitmap, lockErrorBitmap;
	private Bitmap linePressedBitmap, lineErrorBitmap;
	private Point[][] points = new Point[3][3];
	private boolean isInit; // �Ƿ��ʼ��
	private boolean isSelect; // �Ƿ�ѡ��
	private boolean isFinish; // �Ƿ����
	private boolean movingNoPoint; // �Ƿ��ǾŹ�����ĵ�
	private float width, height, offsetX, offsetY, bitmapRadius, movingX,
			movingY;

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Point> pointList = new ArrayList<Point>(); // ���µ�ļ���
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
			// ���ƾŹ�����ĵ�
			if (movingNoPoint) {
				drawLine(canvas, a, new Point(movingX, movingY));
			}
		}
	}

	/**
	 * ��ʼ����
	 */
	private void initPoints() {
		width = getWidth();
		height = getHeight();

		// ����
		if (width > height) {
			offsetX = (width - height) / 2;
			width = height;
		} else {// ����
			offsetY = (height - width) / 2;
			height = width;
		}

		// ��ʼ��ͼƬ
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

		// �������
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
	 * ����
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
		// ѡ�����ü��
		if (!isFinish && isSelect && point != null) {
			// �����
			if (crossPoint(point)) {
				movingNoPoint = true;
			} else { // �µ�
				point.state = Point.STATE_PRESSED;
				pointList.add(point);
			}
		}

		// ���ƽ���
		if (isFinish) {
			// ���Ʋ�����
			if (pointList.size() == 1) {
				resetPoint();
			} else if (pointList.size() < 5 && pointList.size() > 1) { // ���ƴ���
				errorPoint();
			}
		}
		// ˢ�½���
		postInvalidate();

		return true;
	}

	/**
	 * �����Ƿ�ѡ��
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
	 * ���õ�ļ���
	 */
	private void resetPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_NORMAL;
		}
		pointList.clear();
	}

	/**
	 * �����
	 */
	private void errorPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_ERROR;
		}
	}

	/**
	 * �ж�pointList�������Ƿ���������
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
	 * �������ĽǶ�
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
	 * �Զ����
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
		 * ��������֮��ľ���
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
		 * �жϵ��Ƿ��غ�
		 * 
		 * @param pointX
		 *            �ο����x
		 * @param pointY
		 *            �ο����y
		 * @param r
		 *            Բ�İ뾶
		 * @param movingX
		 *            �ƶ����x
		 * @param movingY
		 *            �ƶ����x
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
