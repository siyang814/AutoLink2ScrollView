package com.example.testandroid;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

public class MyScroll extends ScrollView{
	
	private SmoothScrollRunnable mSmoothScrollRunnable;
	
	private ScrollChanged sChanged;

	public void setScrollChanged (ScrollChanged _sChanged )
	{
		sChanged = _sChanged;
	}
	
	public MyScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		if ( sChanged != null ) sChanged.onScrollChanged(l, t, oldl, oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	
	public interface ScrollChanged
	{
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	
	private void setScrollTo ( int x, int y )
	{
		scrollTo(x, y);
	}
	
	private void setScrollBy ( int x, int y )
	{
		scrollBy(x, y);
	}
	
	private int getScrollYValue ()
	{
		return getScrollY();
	}
	/**
	 * 		
	 * @param fromY			移动目的位置
	 * @param actionTime	动画时间
	 */
	public void setScroll (int fromY, int actionTime )
	{
		if (null != mSmoothScrollRunnable) {
			mSmoothScrollRunnable.stop();
		}

		int oldScrollValue = this.getScrollYValue();
		boolean post = (oldScrollValue != fromY);
		if (post) {
			mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue,
					fromY, actionTime);
		}

		if (post) {
				post(mSmoothScrollRunnable);
		}
	}
	
	
	@Override
	public boolean arrowScroll(int direction) {
		// TODO Auto-generated method stub
		Log.w("MYUTIL", "direction="+direction);
		return super.arrowScroll(direction);
	}

	/**
	 * 实现了平滑滚动的Runnable
	 * 
	 * @author Li Hong
	 * @since 2013-8-22
	 */
	final class SmoothScrollRunnable implements Runnable {
		/** 动画效果 */
		private final Interpolator mInterpolator;
		/** 结束Y */
		private final int mScrollToY;
		/** 开始Y */
		private final int mScrollFromY;
		/** 滑动时间 */
		private final long mDuration;
		/** 是否继续运行 */
		private boolean mContinueRunning = true;
		/** 开始时刻 */
		private long mStartTime = -1;
		/** 当前Y */
		private int mCurrentY = -1;

		/**
		 * 构造方法
		 * 
		 * @param fromY
		 *            开始Y
		 * @param toY
		 *            结束Y
		 * @param duration
		 *            动画时间
		 */
		public SmoothScrollRunnable(int fromY, int toY, long duration) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mDuration = duration;
			mInterpolator = new DecelerateInterpolator();
		}

		@Override
		public void run() {
			/**
			 * If the duration is 0, we scroll the view to target y directly.
			 */
			if (mDuration <= 0) {
				setScrollTo(0, mScrollToY);
				return;
			}

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				final long oneSecond = 1000; // SUPPRESS CHECKSTYLE
				long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime))
						/ mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, oneSecond),
						0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator.getInterpolation(normalizedTime
								/ (float) oneSecond));
				mCurrentY = mScrollFromY - deltaY;

				setScrollTo(0, mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				postDelayed(this, 16);// SUPPRESS
																// CHECKSTYLE
			}
		}

		/**
		 * 停止滑动
		 */
		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}
	
	
}
