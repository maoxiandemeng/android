package com.example.jing.view;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jing.R;

public class MyListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	
	private final static int RELEASE_TO_MORE = 5;//����ˢ��
	private final static int PULL_UP_MORE = 6;//����ˢ��

	// ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	//ͷ��
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean isRecored;
	private int startY;
	private int firstItemIndex;
	private int headContentWidth;
	private int headContentHeight;
	private int refreshState;
	private boolean isBack;
	private boolean isRefreshable;
	
	//β��
	private TextView footTips;
	private ImageView footArrow;
	private ProgressBar footPb;
	private int moreDownY;
	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean isMoreRecored;
	private int footContentHeight;
	private int footContentWidth;
	private int remainItems;
	private boolean isMoreBack;
	private int moreState;
	private boolean isLoadMore;
	private boolean moreEnable = true;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private OnRefreshListener refreshListener;
	private OnLoadMoreListener loadMoreListener;
	private OnEmptyListener onEmptyListener;
	
	public MyListView(Context context) {
		super(context);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);

		// headView = (LinearLayout) inflater
		// .inflate(R.layout.listview_head, null);
		//
		// arrowImageView = (ImageView) headView
		// .findViewById(R.id.head_arrowImageView);
		// arrowImageView.setMinimumWidth(70);
		// arrowImageView.setMinimumHeight(50);
		// progressBar = (ProgressBar) headView
		// .findViewById(R.id.head_progressBar);
		// tipsTextview = (TextView)
		// headView.findViewById(R.id.head_tipsTextView);
		// lastUpdatedTextView = (TextView) headView
		// .findViewById(R.id.head_lastUpdatedTextView);
		//
		// measureView(headView);
		// headContentHeight = headView.getMeasuredHeight();
		// headContentWidth = headView.getMeasuredWidth();
		//
		// headView.setPadding(0, -1 * headContentHeight, 0, 0);
		// headView.invalidate();
		//
		// Log.v("size", "width:" + headContentWidth + " height:"
		// + headContentHeight);
		//
		// addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		refreshState = DONE;
		moreState = DONE;
		isRefreshable = false;
		isLoadMore = false;

	}

	private void addHeadView() {
		headView = (LinearLayout) inflater
				.inflate(R.layout.listview_head, null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);

		this.addHeaderView(headView, null, false);
	}

	public void addFootView() {
		footView = LayoutInflater.from(getContext()).inflate(
				R.layout.listview_foot, null);
		footArrow = (ImageView) footView
				.findViewById(R.id.head_arrowImageView);
		footArrow.setMinimumWidth(70);
		footArrow.setMinimumHeight(50);
		footPb = (ProgressBar) footView
				.findViewById(R.id.head_progressBar);
		footTips = (TextView) footView.findViewById(R.id.head_tipsTextView);
		measureView(footView);
		footContentHeight = footView.getMeasuredHeight();
		footContentWidth = footView.getMeasuredWidth();

		footView.setPadding(0, -1 * footContentHeight, 0, 0);
		footView.invalidate();
		Log.d(TAG, "footView:" + footView);
		this.addFooterView(footView);
	}

	public void removeFootView() {
		if (footView != null) {
			this.removeFooterView(footView);
		}
	}

	private View footView;

	public View getFootView() {
		return footView;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisiableItem;
		remainItems = totalItemCount - firstVisiableItem - visibleItemCount;
		// lastItem = firstVisiableItem + visibleItemCount - 1;
		// //��1����Ϊ������˸�addFooterView
		// this.totalItemCount = totalItemCount;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isLoadMore && refreshState == DONE && getMoreEnable()) {
			moreTouch(event);
		}

		if (isRefreshable && moreState == DONE) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v(TAG, "��downʱ���¼��ǰλ�á�");
				}
				break;

			case MotionEvent.ACTION_UP:

				if (refreshState != REFRESHING && refreshState != LOADING) {
					if (refreshState == DONE) {
						// ʲô������
					}
					if (refreshState == PULL_To_REFRESH) {
						refreshState = DONE;
						changeHeaderViewByState();

						Log.v(TAG, "������ˢ��״̬����done״̬");
					}
					if (refreshState == RELEASE_To_REFRESH) {
						refreshState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

						Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					Log.v(TAG, "��moveʱ���¼��λ��");
					isRecored = true;
					startY = tempY;
				}

				if (refreshState != REFRESHING && isRecored
						&& refreshState != LOADING) {

					// ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���

					// ��������ȥˢ����
					if (refreshState == RELEASE_To_REFRESH) {

						setSelection(0);

						// �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							refreshState = PULL_To_REFRESH;
							changeHeaderViewByState();

							Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
						}
						// һ�����Ƶ�����
						else if (tempY - startY <= 0) {
							refreshState = DONE;
							changeHeaderViewByState();

							Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
						}
						// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
						else {
							// ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
						}
					}
					// ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
					if (refreshState == PULL_To_REFRESH) {

						setSelection(0);

						// ���������Խ���RELEASE_TO_REFRESH��״̬
						if ((tempY - startY) / RATIO >= headContentHeight) {
							refreshState = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

							Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
						}
						// ���Ƶ�����
						else if (tempY - startY <= 0) {
							refreshState = DONE;
							changeHeaderViewByState();

							Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");
						}
					}

					// done״̬��
					if (refreshState == DONE) {
						if (tempY - startY > 0) {
							refreshState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// ����headView��size
					if (refreshState == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// ����headView��paddingTop
					if (refreshState == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}
	
	private void moreTouch(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isMoreRecored && remainItems <= 0) {
				isMoreRecored = true;
				moreDownY = (int) event.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();
			if (!isMoreRecored && remainItems <= 0) {
				isMoreRecored = true;
				moreDownY = tempY;
			}
			if (moreState != REFRESHING && isMoreRecored && moreState != LOADING) {
				if (moreState == RELEASE_TO_MORE) {
					setSelection(getAdapter().getCount() - 1);
					if (tempY - moreDownY < 0 &&
							(moreDownY - tempY) / RATIO < footContentHeight) {
						moreState = PULL_UP_MORE;
						changeFootViewByState();
					}else if (tempY - moreDownY >= 0) {
						moreState = DONE;
						changeFootViewByState();
					}
				}

				if (moreState == PULL_UP_MORE) {
					setSelection(getAdapter().getCount() - 1);
					if ((moreDownY - tempY) / RATIO >= footContentHeight) {
						moreState = RELEASE_TO_MORE;
						isMoreBack = true;
						changeFootViewByState();
					}else if (tempY - moreDownY >= 0) {
						moreState = DONE;
						changeFootViewByState();
					}
				}

				if (moreState == DONE) {
					if (tempY - moreDownY < 0) {
						moreState = PULL_UP_MORE;
						changeFootViewByState();
					}
				}

				if (moreState == PULL_UP_MORE || moreState == RELEASE_TO_MORE) {
					footView.setPadding(0, (moreDownY - tempY) / RATIO
							- footContentHeight, 0, 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (moreState == PULL_UP_MORE) {
				moreState = DONE;
				changeFootViewByState();
			}

			if (moreState == RELEASE_TO_MORE) {
				moreState = REFRESHING;
				changeFootViewByState();
				onLoadMore();
			}
			isMoreRecored = false;
			isMoreBack = false;
			break;
		default:
			break;
		}
	}

	// ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	private void changeHeaderViewByState() {
		switch (refreshState) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("�ɿ�ˢ��");

			Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("����ˢ��");
			} else {
				tipsTextview.setText("����ˢ��");
			}
			Log.v(TAG, "��ǰ״̬������ˢ��");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("����ˢ��...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "��ǰ״̬,����ˢ��...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.icon_arrow);
			tipsTextview.setText("����ˢ��");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "��ǰ״̬��done");
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		addHeadView();
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		refreshState = DONE;
		lastUpdatedTextView.setText("�������:" + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	private void changeFootViewByState() {
		switch (moreState) {
		case RELEASE_TO_MORE:
			footArrow.setVisibility(View.VISIBLE);
			footPb.setVisibility(View.GONE);
			footTips.setVisibility(View.VISIBLE);

			footArrow.clearAnimation();
			footArrow.startAnimation(animation);

			footTips.setText("�ɿ����ظ���");
			break;
		case PULL_UP_MORE:
			footPb.setVisibility(View.GONE);
			footTips.setVisibility(View.VISIBLE);
			footArrow.clearAnimation();
			footArrow.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isMoreBack) {
				isMoreBack = false;
				footArrow.clearAnimation();
				footArrow.startAnimation(reverseAnimation);
				footTips.setText("�������ظ���");
			} else {
				footTips.setText("�������ظ���");
			}
			break;

		case REFRESHING:
			footView.setPadding(0, 0, 0, 0);
			footPb.setVisibility(View.VISIBLE);
			footArrow.clearAnimation();
			footArrow.setVisibility(View.GONE);
			footTips.setText("����ˢ��...");
			break;
		case DONE:
			footView.setPadding(0, -1 * footContentHeight, 0, 0);
			footPb.setVisibility(View.GONE);
			footArrow.clearAnimation();
			footTips.setText("�������ظ���");
			break;
		}
	}

	public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
		if (loadMoreListener != null) {
			addFootView();
			isLoadMore = true;
		}
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	private void onLoadMore() {
		if (loadMoreListener != null) {
			loadMoreListener.onLoadMore();
		}
	}
	
	public void onLoadMoreComplete() {
		moreState = DONE;
		changeFootViewByState();
	}
	
	public void setMoreEnable(boolean moreEnable){
		this.moreEnable = moreEnable;
	}
	
	public boolean getMoreEnable(){
		return moreEnable;
	}
	
	public void setOnEmptyListener(OnEmptyListener onEmptyListener){
		this.onEmptyListener = onEmptyListener;
	}
	
	public interface OnEmptyListener{
		public void onAddEmpty();
		public void onRemoveEmpty();
	}
	
	// �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

//	public void setAdapter(BaseAdapter adapter) {
//		if (onEmptyListener != null) {
//			onEmptyListener.onRemoveEmpty();
//		}
//		if (adapter != null && adapter.getCount() == 0) {
//			if (onEmptyListener != null) {
//				onEmptyListener.onAddEmpty();
//			}
//		}
//		super.setAdapter(adapter);
//	}
	
	public void notifyDataSetChanged(BaseAdapter adapter) {
		if (onEmptyListener != null) {
			onEmptyListener.onRemoveEmpty();
		}
		if (adapter != null && adapter.getCount() == 0) {
			if (onEmptyListener != null) {
				onEmptyListener.onAddEmpty();
			}
		}
		adapter.notifyDataSetChanged();
	}

}
