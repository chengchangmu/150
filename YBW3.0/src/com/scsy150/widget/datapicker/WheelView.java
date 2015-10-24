package com.scsy150.widget.datapicker;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scsy150.R;

/**
 * Numeric wheel view.
 * 
 * @author Yuri Kanivets
 */
public class WheelView extends View {

	/** Top and bottom shadows colors */
	private static final int[] SHADOWS_COLORS = new int[] { 0xFF111111,
			0x00AAAAAA, 0x00AAAAAA };

	/** Top and bottom items offset (to hide that) */
	private static final int ITEM_OFFSET_PERCENT = 10;

	/** Left and right padding value */
	private static final int PADDING = 10;

	/** Default count of visible items */
	private static final int DEF_VISIBLE_ITEMS = 5;

	// Wheel Values
	private int currentItem = 0;

	// Count of visible items
	private int visibleItems = DEF_VISIBLE_ITEMS;

	// Item height
	private int itemHeight = 0;

	// Center Line
	private Drawable centerDrawable;

	// Shadows drawables
	private GradientDrawable topShadow;
	private GradientDrawable bottomShadow;

	// 缓存滚轮整体的非3d时的图像
	private Bitmap mCacheBitmap;
	// 用于临时存放滚轮的上面或下面半截图像
	private Bitmap mTmpBitmap;
	// 存放3d倒影后的结果
	private Bitmap m3DShadowBitmap;
	// 用于刷新滚轮背景
	private Paint mBkP = new Paint();
	private int mBkColor = 0xffffffff;

	// 在相应bitmap上进行绘制
	private Canvas mCacheCanvas;
	private Canvas mTmpCanvas;
	private Canvas m3DShadowCanvas;

	// 对图像做3d倒影变换
	private Camera mCamera = new Camera();;

	private Matrix mMatrix = new Matrix();
	private Paint mPaint = new Paint();

	// 3d变换的中心点
	private int mCenterX;
	private int mCenterY;

	// 被变换的图片的宽高
	private int mBWidth;
	private int mBHeight;

	public static final int SHADOW_LEFT = 0;
	public static final int SHADOW_MIDDLE = 1;
	public static final int SHADOW_RIGHT = 2;

	// 滚轮是偏右还是左
	private int mShadowGravity = SHADOW_RIGHT;

	private int mLineColor;
	private int mCurrentTextColor;
	private int mSecondTextColor;
	private int mThreeTextColor;

	private int mCurrentTextSize;
	private int mSecondTextSize;
	private int mThreeTextSize;

	private int mTextRightPadding;

	private boolean mNeed3DEffect = true;

	// Scrolling
	private WheelScroller scroller;
	private boolean isScrollingPerformed;
	private int scrollingOffset;

	// Cyclic
	boolean isCyclic = false;

	// Items layout
	private LinearLayout itemsLayout;

	// The number of first item in layout
	private int firstItem;

	// View adapter
	private WheelViewAdapter viewAdapter;

	// Recycle
	private WheelRecycle recycle = new WheelRecycle(this);

	// Listeners
	private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
	private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
	private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context);
	}

	/**
	 * Constructor
	 */
	public WheelView(Context context) {
		super(context);
		initData(context);
	}

	/**
	 * Initializes class data
	 * 
	 * @param context
	 *            the context
	 */
	private void initData(Context context) {
		scroller = new WheelScroller(getContext(), scrollingListener);
	}

	// Scrolling listener
	WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
		@Override
		public void onStarted() {
			isScrollingPerformed = true;
			notifyScrollingListenersAboutStart();
		}

		@Override
		public void onScroll(int distance) {
			doScroll(distance);

			int height = getHeight();
			if (scrollingOffset > height) {
				scrollingOffset = height;
				scroller.stopScrolling();
			} else if (scrollingOffset < -height) {
				scrollingOffset = -height;
				scroller.stopScrolling();
			}
		}

		@Override
		public void onFinished() {
			if (isScrollingPerformed) {
				notifyScrollingListenersAboutEnd();
				isScrollingPerformed = false;
			}

			scrollingOffset = 0;
			invalidate();
		}

		@Override
		public void onJustify() {
			if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
				scroller.scroll(scrollingOffset, 0);
			}
		}
	};

	/**
	 * Set the the specified scrolling interpolator
	 * 
	 * @param interpolator
	 *            the interpolator
	 */
	public void setInterpolator(Interpolator interpolator) {
		scroller.setInterpolator(interpolator);
	}

	/**
	 * Gets count of visible items
	 * 
	 * @return the count of visible items
	 */
	public int getVisibleItems() {
		return visibleItems;
	}

	/**
	 * Sets the desired count of visible items. Actual amount of visible items
	 * depends on wheel layout parameters. To apply changes and rebuild view
	 * call measure().
	 * 
	 * @param count
	 *            the desired count for visible items
	 */
	public void setVisibleItems(int count) {
		visibleItems = count;
	}

	/**
	 * Gets view adapter
	 * 
	 * @return the view adapter
	 */
	public WheelViewAdapter getViewAdapter() {
		return viewAdapter;
	}

	// Adapter listener
	private DataSetObserver dataObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			invalidateWheel(false);
		}

		@Override
		public void onInvalidated() {
			invalidateWheel(true);
		}
	};

	/**
	 * Sets view adapter. Usually new adapters contain different views, so it
	 * needs to rebuild view by calling measure().
	 * 
	 * @param viewAdapter
	 *            the view adapter
	 */
	public void setViewAdapter(WheelViewAdapter viewAdapter) {
		if (this.viewAdapter != null) {
			this.viewAdapter.unregisterDataSetObserver(dataObserver);
		}
		this.viewAdapter = viewAdapter;
		if (this.viewAdapter != null) {
			this.viewAdapter.registerDataSetObserver(dataObserver);
		}

		invalidateWheel(true);
	}

	/**
	 * Adds wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addChangingListener(OnWheelChangedListener listener) {
		changingListeners.add(listener);
	}

	/**
	 * Removes wheel changing listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeChangingListener(OnWheelChangedListener listener) {
		changingListeners.remove(listener);
	}

	/**
	 * Notifies changing listeners
	 * 
	 * @param oldValue
	 *            the old wheel value
	 * @param newValue
	 *            the new wheel value
	 */
	protected void notifyChangingListeners(int oldValue, int newValue) {
		for (OnWheelChangedListener listener : changingListeners) {
			listener.onChanged(this, oldValue, newValue);
		}
	}

	/**
	 * Adds wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.add(listener);
	}

	/**
	 * Removes wheel scrolling listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeScrollingListener(OnWheelScrollListener listener) {
		scrollingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about starting scrolling
	 */
	protected void notifyScrollingListenersAboutStart() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingStarted(this);
		}
	}

	/**
	 * Notifies listeners about ending scrolling
	 */
	protected void notifyScrollingListenersAboutEnd() {
		for (OnWheelScrollListener listener : scrollingListeners) {
			listener.onScrollingFinished(this);
		}
	}

	/**
	 * Adds wheel clicking listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addClickingListener(OnWheelClickedListener listener) {
		clickingListeners.add(listener);
	}

	/**
	 * Removes wheel clicking listener
	 * 
	 * @param listener
	 *            the listener
	 */
	public void removeClickingListener(OnWheelClickedListener listener) {
		clickingListeners.remove(listener);
	}

	/**
	 * Notifies listeners about clicking
	 */
	protected void notifyClickListenersAboutClick(int item) {
		for (OnWheelClickedListener listener : clickingListeners) {
			listener.onItemClicked(this, item);
		}
	}

	/**
	 * Gets current value
	 * 
	 * @return the current value
	 */
	public int getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 * @param animated
	 *            the animation flag
	 */
	public void setCurrentItem(int index, boolean animated) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return; // throw?
		}

		int itemCount = viewAdapter.getItemsCount();
		if (index < 0 || index >= itemCount) {
			if (isCyclic) {
				while (index < 0) {
					index += itemCount;
				}
				index %= itemCount;
			} else {
				return; // throw?
			}
		}
		if (index != currentItem) {
			if (animated) {
				int itemsToScroll = index - currentItem;
				if (isCyclic) {
					int scroll = itemCount + Math.min(index, currentItem)
							- Math.max(index, currentItem);
					if (scroll < Math.abs(itemsToScroll)) {
						itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
					}
				}
				scroll(itemsToScroll, 0);
			} else {
				scrollingOffset = 0;

				int old = currentItem;
				currentItem = index;

				notifyChangingListeners(old, currentItem);

				invalidate();
			}
		}
	}

	/**
	 * Sets the current item w/o animation. Does nothing when index is wrong.
	 * 
	 * @param index
	 *            the item index
	 */
	public void setCurrentItem(int index) {
		setCurrentItem(index, false);
	}

	/**
	 * Tests if wheel is cyclic. That means before the 1st item there is shown
	 * the last one
	 * 
	 * @return true if wheel is cyclic
	 */
	public boolean isCyclic() {
		return isCyclic;
	}

	/**
	 * Set wheel cyclic flag
	 * 
	 * @param isCyclic
	 *            the flag to set
	 */
	public void setCyclic(boolean isCyclic) {
		this.isCyclic = isCyclic;
		invalidateWheel(false);
	}

	/**
	 * Invalidates wheel
	 * 
	 * @param clearCaches
	 *            if true then cached views will be clear
	 */
	public void invalidateWheel(boolean clearCaches) {
		if (clearCaches) {
			recycle.clearAll();
			if (itemsLayout != null) {
				itemsLayout.removeAllViews();
			}
			scrollingOffset = 0;
		} else if (itemsLayout != null) {
			// cache all items
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		}

		invalidate();
	}

	/**
	 * Initializes resources
	 */
	private void initResourcesIfNecessary() {
		// if (centerDrawable == null) {
		// centerDrawable = getContext().getResources().getDrawable(
		// R.drawable.wheel_val);
		// }

		if (topShadow == null) {
			topShadow = new GradientDrawable(Orientation.TOP_BOTTOM,
					SHADOWS_COLORS);
		}

		if (bottomShadow == null) {
			bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP,
					SHADOWS_COLORS);
		}
		Resources rs = getContext().getResources();
		mLineColor = rs.getColor(R.color.common_line);
		mCurrentTextColor = rs.getColor(R.color.common_big_title_text);
		mSecondTextColor = rs.getColor(R.color.common_small_des_text);
		mThreeTextColor = rs.getColor(R.color.common_tiny_des_text);
		mCurrentTextSize = rs.getDimensionPixelSize(R.dimen.font_16px);
		mSecondTextSize = rs
				.getDimensionPixelSize(R.dimen.font_14px);
		mThreeTextSize = rs
				.getDimensionPixelSize(R.dimen.font_12px);

		mTextRightPadding = rs
				.getDimensionPixelSize(R.dimen.px40);

		// setBackgroundResource(R.drawable.picker_hover_bg);
	}

	/**
	 * Calculates desired height for layout
	 * 
	 * @param layout
	 *            the source layout
	 * @return the desired layout height
	 */
	private int getDesiredHeight(LinearLayout layout) {
		if (layout != null && layout.getChildAt(0) != null) {
			itemHeight = layout.getChildAt(0).getMeasuredHeight();
		}

		int desired = itemHeight * visibleItems - itemHeight
				* ITEM_OFFSET_PERCENT / 50;

		return Math.max(desired, getSuggestedMinimumHeight());
	}

	/**
	 * Returns height of wheel item
	 * 
	 * @return the item height
	 */
	private int getItemHeight() {
		if (itemHeight != 0) {
			return itemHeight;
		}

		if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
			itemHeight = itemsLayout.getChildAt(0).getHeight();
			return itemHeight;
		}

		return getHeight() / visibleItems;
	}

	/**
	 * Calculates control width and creates text layouts
	 * 
	 * @param widthSize
	 *            the input layout width
	 * @param mode
	 *            the layout mode
	 * @return the calculated control width
	 */
	private int calculateLayoutWidth(int widthSize, int mode) {
		initResourcesIfNecessary();

		// TODO: make it static
		itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		itemsLayout
				.measure(MeasureSpec.makeMeasureSpec(widthSize,
						MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
						0, MeasureSpec.UNSPECIFIED));
		int width = itemsLayout.getMeasuredWidth();

		if (mode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width += 2 * PADDING;

			// Check against our minimum width
			width = Math.max(width, getSuggestedMinimumWidth());

			if (mode == MeasureSpec.AT_MOST && widthSize < width) {
				width = widthSize;
			}
		}

		itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED));

		return width;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		buildViewForMeasuring();

		int width = calculateLayoutWidth(widthSize, widthMode);

		int height;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = getDesiredHeight(itemsLayout);

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layout(r - l, b - t);
	}

	/**
	 * Sets layouts width and height
	 * 
	 * @param width
	 *            the layout width
	 * @param height
	 *            the layout height
	 */
	private void layout(int width, int height) {
		int itemsWidth = width - 2 * PADDING;

		itemsLayout.layout(0, 0, itemsWidth, height);
	}

	public void setShadowGravity(int gravity) {
		mShadowGravity = gravity;
	}

	/**
	 * 获取图片的倒影，让滚轮产生3d的效果
	 * 
	 * @param b
	 * @param isTop
	 *            是滚轮上半部分与否
	 * @return
	 */
	private Bitmap get3DShadow(Bitmap b, boolean isTop) {
		if (m3DShadowBitmap == null) {
			m3DShadowBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Bitmap.Config.RGB_565);
		}
		mBWidth = b.getWidth();
		mBHeight = b.getHeight();
		if (m3DShadowCanvas == null) {
			m3DShadowCanvas = new Canvas(m3DShadowBitmap);
		}
		m3DShadowCanvas.drawColor(mBkColor);
		mPaint.setAntiAlias(true);

		if (isTop) {
			mCenterX = 0;
			mCenterY = mBHeight;
			rotate(mNeed3DEffect ? -6 : -10);
		} else {
			mCenterX = 0;
			mCenterY = 0;
			rotate(mNeed3DEffect ? 6 : 10);
		}
		m3DShadowCanvas.drawBitmap(b, mMatrix, mPaint);
		return m3DShadowBitmap;

	}

	/**
	 * 绕X轴做旋转
	 * 
	 * @param degreeY
	 */
	private void rotate(int degreeY) {
		mCamera.save();
		if (mShadowGravity == SHADOW_RIGHT) {
			mCamera.translate(-mBWidth, 0, 0);
		} else if (mShadowGravity == SHADOW_MIDDLE) {
			mCamera.translate(-mBWidth / 2, 0, 0);
		}

		mCamera.rotateX(-degreeY);
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-mCenterX, -mCenterY);
		mMatrix.postTranslate(mCenterX, mCenterY);
		if (mShadowGravity == SHADOW_RIGHT) {
			mMatrix.postTranslate(mBWidth, 0);
		} else if (mShadowGravity == SHADOW_MIDDLE) {
			mMatrix.postTranslate(mBWidth / 2, 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
			updateView();

			if (mCacheBitmap == null) {
				mCacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
						Bitmap.Config.RGB_565);
			}

			if (mCacheCanvas == null) {
				mCacheCanvas = new Canvas(mCacheBitmap);
			}
			mCacheCanvas.drawColor(mBkColor);
			drawItems(mCacheCanvas);

			final int halfNum = (Math.round((float) getHeight()
					/ getItemHeight()) - 1) / 2;
			if (mTmpBitmap == null) {
				mTmpBitmap = Bitmap.createBitmap(this.getWidth(),
						getItemHeight() * halfNum, Bitmap.Config.RGB_565);
			}

			// draw top
			if (mTmpCanvas == null) {
				mTmpCanvas = new Canvas(mTmpBitmap);
			}
			mTmpCanvas.drawColor(mBkColor);
			mTmpCanvas.drawBitmap(mCacheBitmap, 0, 0, null);
			mBkP.setColor(mBkColor);
			final int halfHeight = getItemHeight() * halfNum;
			mCacheCanvas.drawRect(0, 0, getWidth(), halfHeight, mBkP);
			mMatrix.reset();
			Bitmap shadow = get3DShadow(mTmpBitmap, true);
			if (shadow == null) {
				shadow = mTmpBitmap;
			}

			mCacheCanvas.drawBitmap(shadow, 0, 0, null);

			// draw bottom
			mTmpCanvas.drawColor(mBkColor);
			final int bottomY = getHeight() - halfHeight;
			mTmpCanvas.drawBitmap(mCacheBitmap, 0, -bottomY, null);
			mCacheCanvas.drawRect(0, bottomY, getWidth(), getHeight(), mBkP);
			mMatrix.reset();
			shadow = get3DShadow(mTmpBitmap, false);
			if (shadow == null) {
				shadow = mTmpBitmap;
			}
			mCacheCanvas.drawBitmap(shadow, 0, bottomY, null);

			canvas.drawBitmap(mCacheBitmap, 0, 0, mPaint);

			mPaint.setColor(mLineColor);
			int center = getHeight() / 2;
			int offset = (int) (getItemHeight() / 2 * 1.2);
			canvas.drawLine(0, center - offset, getWidth(), center - offset,
					mPaint);
			canvas.drawLine(0, center + offset, getWidth(), center + offset,
					mPaint);
		}
		if (!mNeed3DEffect) {
			drawShadows(canvas);
		}
	}

	/**
	 * Draws shadows on top and bottom of control
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawShadows(Canvas canvas) {
		int height = (int) (1.5 * getItemHeight());
		topShadow.setBounds(0, 0, getWidth(), height);
		topShadow.draw(canvas);

		bottomShadow
				.setBounds(0, getHeight() - height, getWidth(), getHeight());
		bottomShadow.draw(canvas);
	}

	/**
	 * Draws items
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawItems(Canvas canvas) {
		canvas.save();

		int top = (currentItem - firstItem) * getItemHeight()
				+ (getItemHeight() - getHeight()) / 2;

		canvas.translate(PADDING, -top + scrollingOffset);

		if (mNeed3DEffect) {
			final int count = itemsLayout.getChildCount();
			final int index = currentItem - firstItem;
			int offset = 0;
			for (int i = 0; i < count; i++) {
				View v = itemsLayout.getChildAt(i);
				if (v instanceof TextView) {
					TextView tv = (TextView) v;
					offset = Math.abs(i - index);
					if (offset == 0) {
						tv.setTextColor(mCurrentTextColor);
						tv.setTextSize(mCurrentTextSize);
					} else if (offset == 1) {
						tv.setTextColor(mSecondTextColor);
						tv.setTextSize(mSecondTextSize);
					} else {
						tv.setTextColor(mThreeTextColor);
						tv.setTextSize(mThreeTextSize);
					}
				}
			}
		}

		itemsLayout.draw(canvas);

		canvas.restore();
	}

	/**
	 * Draws rect for current value
	 * 
	 * @param canvas
	 *            the canvas for drawing
	 */
	private void drawCenterRect(Canvas canvas) {
		int center = getHeight() / 2;
		int offset = (int) (getItemHeight() / 2 * 1.2);
		centerDrawable.setBounds(0, center - offset, getWidth(), center
				+ offset);
		centerDrawable.draw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled() || getViewAdapter() == null) {
			return true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!isScrollingPerformed) {
				int distance = (int) event.getY() - getHeight() / 2;
				if (distance > 0) {
					distance += getItemHeight() / 2;
				} else {
					distance -= getItemHeight() / 2;
				}
				int items = distance / getItemHeight();
				if (items != 0 && isValidItemIndex(currentItem + items)) {
					notifyClickListenersAboutClick(currentItem + items);
				}
			}
			break;
		}

		return scroller.onTouchEvent(event);
	}

	/**
	 * Scrolls the wheel
	 * 
	 * @param delta
	 *            the scrolling value
	 */
	private void doScroll(int delta) {
		scrollingOffset += delta;

		int itemHeight = getItemHeight();
		int count = scrollingOffset / itemHeight;

		int pos = currentItem - count;
		int itemCount = viewAdapter.getItemsCount();

		int fixPos = scrollingOffset % itemHeight;
		if (Math.abs(fixPos) <= itemHeight / 2) {
			fixPos = 0;
		}
		if (isCyclic && itemCount > 0) {
			if (fixPos > 0) {
				pos--;
				count++;
			} else if (fixPos < 0) {
				pos++;
				count--;
			}
			// fix position by rotating
			while (pos < 0) {
				pos += itemCount;
			}
			pos %= itemCount;
		} else {
			//
			if (pos < 0) {
				count = currentItem;
				pos = 0;
			} else if (pos >= itemCount) {
				count = currentItem - itemCount + 1;
				pos = itemCount - 1;
			} else if (pos > 0 && fixPos > 0) {
				pos--;
				count++;
			} else if (pos < itemCount - 1 && fixPos < 0) {
				pos++;
				count--;
			}
		}

		int offset = scrollingOffset;
		if (pos != currentItem) {
			setCurrentItem(pos, false);
		} else {
			invalidate();
		}

		// update offset
		scrollingOffset = offset - count * itemHeight;
		if (scrollingOffset > getHeight()) {
			scrollingOffset = scrollingOffset % getHeight() + getHeight();
		}
	}

	/**
	 * Scroll the wheel
	 * 
	 * @param itemsToSkip
	 *            items to scroll
	 * @param time
	 *            scrolling duration
	 */
	public void scroll(int itemsToScroll, int time) {
		int distance = itemsToScroll * getItemHeight() - scrollingOffset;
		scroller.scroll(distance, time);
	}

	/**
	 * Calculates range for wheel items
	 * 
	 * @return the items range
	 */
	private ItemsRange getItemsRange() {
		if (getItemHeight() == 0) {
			return null;
		}

		int first = currentItem;
		int count = 1;

		while (count * getItemHeight() < getHeight()) {
			first--;
			count += 2; // top + bottom items
		}

		if (scrollingOffset != 0) {
			if (scrollingOffset > 0) {
				first--;
			}
			count++;

			// process empty items above the first or below the second
			int emptyItems = scrollingOffset / getItemHeight();
			first -= emptyItems;
			count += Math.asin(emptyItems);
		}
		return new ItemsRange(first, count);
	}

	/**
	 * Rebuilds wheel items if necessary. Caches all unused items.
	 * 
	 * @return true if items are rebuilt
	 */
	private boolean rebuildItems() {
		boolean updated = false;
		ItemsRange range = getItemsRange();
		if (itemsLayout != null) {
			int first = recycle.recycleItems(itemsLayout, firstItem, range);
			updated = firstItem != first;
			firstItem = first;
		} else {
			createItemsLayout();
			updated = true;
		}

		if (!updated) {
			updated = firstItem != range.getFirst()
					|| itemsLayout.getChildCount() != range.getCount();
		}

		if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
			for (int i = firstItem - 1; i >= range.getFirst(); i--) {
				if (!addViewItem(i, true)) {
					break;
				}
				firstItem = i;
			}
		} else {
			firstItem = range.getFirst();
		}

		int first = firstItem;
		for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
			if (!addViewItem(firstItem + i, false)
					&& itemsLayout.getChildCount() == 0) {
				first++;
			}
		}
		firstItem = first;

		return updated;
	}

	/**
	 * Updates view. Rebuilds items and label if necessary, recalculate items
	 * sizes.
	 */
	private void updateView() {
		if (rebuildItems()) {
			calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
			layout(getWidth(), getHeight());
		}
	}

	/**
	 * Creates item layouts if necessary
	 */
	private void createItemsLayout() {
		if (itemsLayout == null) {
			itemsLayout = new LinearLayout(getContext());
			itemsLayout.setOrientation(LinearLayout.VERTICAL);
		}
	}

	/**
	 * Builds view for measuring
	 */
	private void buildViewForMeasuring() {
		// clear all items
		if (itemsLayout != null) {
			recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
		} else {
			createItemsLayout();
		}

		// add views
		int addItems = visibleItems / 2;
		for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
			if (addViewItem(i, true)) {
				firstItem = i;
			}
		}
	}

	/**
	 * Adds view for item to items layout
	 * 
	 * @param index
	 *            the item index
	 * @param first
	 *            the flag indicates if view should be first
	 * @return true if corresponding item exists and is added
	 */
	private boolean addViewItem(int index, boolean first) {
		View view = getItemView(index);
		if (view != null) {
			if (mNeed3DEffect) {
				view.setMinimumHeight(getItemHeight());
				if (view instanceof TextView) {
					TextView tv = (TextView) view;
					if (mShadowGravity == SHADOW_RIGHT) {
						tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
						tv.setPadding(0, 0, mTextRightPadding, 0);
					} else if (mShadowGravity == SHADOW_MIDDLE) {
						tv.setGravity(Gravity.CENTER);
					} else {
						tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
						tv.setPadding(mTextRightPadding, 0, 0, 0);
					}
				}
			}
			if (first) {
				itemsLayout.addView(view, 0);
			} else {
				itemsLayout.addView(view);
			}

			return true;
		}

		return false;
	}

	/**
	 * Checks whether intem index is valid
	 * 
	 * @param index
	 *            the item index
	 * @return true if item index is not out of bounds or the wheel is cyclic
	 */
	private boolean isValidItemIndex(int index) {
		return viewAdapter != null
				&& viewAdapter.getItemsCount() > 0
				&& (isCyclic || index >= 0
						&& index < viewAdapter.getItemsCount());
	}

	/**
	 * Returns view for specified item
	 * 
	 * @param index
	 *            the item index
	 * @return item view or empty view if index is out of bounds
	 */
	private View getItemView(int index) {
		if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
			return null;
		}
		int count = viewAdapter.getItemsCount();
		if (!isValidItemIndex(index)) {
			return viewAdapter
					.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
		} else {
			while (index < 0) {
				index = count + index;
			}
		}

		index %= count;
		return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
	}

	/**
	 * Stops scrolling
	 */
	public void stopScrolling() {
		scroller.stopScrolling();
	}
}
