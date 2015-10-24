package com.scsy150.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scsy150.R;

/**
 * 下拉刷新
 * 
 * @author K
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {

	private static final int STATE_PULL_TO_REFRESH = 1;// 下拉刷新
	private static final int STATE_RELEASE_TO_REFRESH = 2;// 松开刷新
	private static final int STATE_REFRESHING = 3;// 正在刷新

	private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前状态

	private View mHeaderView;
	private int mHeaderHeight;

	private View mFooterView;
	private int mFooterHeight;

	private int startY = -1;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private RotateAnimation mAnimUp;
	private RotateAnimation mAnimDown;
	private ProgressBar pbLoading;

	private boolean isLoadMore;// 表示是否正在加载更多数据

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
		mGestureDetector = new GestureDetector(new YScrollDetector());
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_refresh_header,
				null);
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

		this.addHeaderView(mHeaderView);

		mHeaderView.measure(0, 0);// 手动测量布局宽高
		mHeaderHeight = mHeaderView.getMeasuredHeight();// 获取头布局高度

		mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局

		initAnim();

		tvTime.setText("最后刷新:" + getCurrentTime());
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.pull_refresh_footer,
				null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);// 手动测量布局宽高
		mFooterHeight = mFooterView.getMeasuredHeight();// 获取脚布局高度

		mFooterView.setPadding(0, -mFooterHeight, 0, 0);// 隐藏脚布局

		setOnScrollListener(this);// 设置滑动监听
	}

	/**
	 * 初始化箭头动画
	 */
	private void initAnim() {
		// 向上移动动画
		mAnimUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mAnimUp.setDuration(200);
		mAnimUp.setFillAfter(true);

		// 向下移动动画
		mAnimDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mAnimDown.setDuration(200);
		mAnimDown.setFillAfter(true);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 如果没有初始化起点坐标,需要重新获取一次
				startY = (int) ev.getY();
			}

			if (mCurrentState == STATE_REFRESHING) {// 如果当前正在刷新,什么都不做
				break;
			}

			int endY = (int) ev.getY();
			int endX = (int) ev.getX();
			int dy = endY - startY;// y方向偏移

			int firstVisiblePosition = getFirstVisiblePosition();// 当前显示的第一个item的位置

			if (dy > 0 && firstVisiblePosition == 0) {// 向下拉, 并且当前显示的是第一个item
				int paddingTop = dy - mHeaderHeight;// 计算当前padding值

				if (paddingTop > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {// 切换为松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop <= 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {// 切换为下拉刷新
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);// 更新当前头布局位置
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;// 初始化起点坐标
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				// 正在刷新
				mCurrentState = STATE_REFRESHING;
				mHeaderView.setPadding(0, 0, 0, 0);// 完全显示头布局
				refreshState();
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态刷新界面
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.startAnimation(mAnimDown);
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.startAnimation(mAnimUp);
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新...");
			ivArrow.clearAnimation();// 清除动画,才能正常隐藏布局
			ivArrow.setVisibility(View.INVISIBLE);
			pbLoading.setVisibility(View.VISIBLE);

			if (mListener != null) {
				mListener.onRefresh();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 刷新完成
	 */
	public void onRefreshComplete(boolean success) {
		if (!isLoadMore) {
			mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);// 隐藏头布局

			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbLoading.setVisibility(View.INVISIBLE);

			if (success) {
				tvTime.setText("最后刷新:" + getCurrentTime());
			}
		} else {
			isLoadMore = false;
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);// 隐藏脚布局
		}
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	private OnRefreshListener mListener;

	// 设置下拉刷新监听
	public void setRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	// 下拉刷新的回调接口
	public interface OnRefreshListener {
		// 下拉刷新
		public void onRefresh();

		// 加载更多
		public void loadMore();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			int lastVisiblePosition = getLastVisiblePosition();// 当前页面最后一个item的位置
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {// 到底了
				isLoadMore = true;

				System.out.println("到底了");
				mFooterView.setPadding(0, 0, 0, 0);// 显示脚布局

				setSelection(getCount() - 1);// 设置listview显示位置,显示在最后一个item上

				if (mListener != null) {
					mListener.loadMore();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	private OnItemClickListener mClickListener;
	private int startX;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		mClickListener = listener;
		super.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mClickListener != null) {
			mClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);// 回调方法,位置要减掉头布局的个数
		}
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (distanceY != 0 && distanceX != 0) {

			}
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

}
