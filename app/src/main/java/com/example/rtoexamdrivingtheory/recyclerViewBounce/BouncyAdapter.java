
package com.example.rtoexamdrivingtheory.recyclerViewBounce;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

class BouncyAdapter extends RecyclerView.Adapter implements SpringScroller.SpringScrollerListener {
    private static final int GAP_SIZE = 1000; // dp

    private static final int VIEW_TYPE_HEADER = 1111;
    private static final int VIEW_TYPE_FOOTER = 2222;

    private final BouncyConfig mConfig;
    private final int mGapLimitPx;

    private Context mContext;
    private final RecyclerView mRecyclerView;
    private final RecyclerView.Adapter mAdapter;
    private final LinearLayoutManager mLayoutManager;

    private final View mFooterView;
    private final View mHeaderView;

    private final DecelerateSmoothScroller mScroller;
    private final SpringScroller mSpringScroller;

    private final Handler mHandlerUI = new Handler(Looper.getMainLooper());

    private long mPrevTime = SystemClock.elapsedRealtime();
    private double mScrollSpeed = 0;
    private int mPrevFooterVisible = 0;

    private boolean mIsScrollBack = false;

    private int minDistanceToScrollBack = 1;

    private boolean mShouldUseSpring = false;

    private boolean mFirstScrollBy = false;

    private boolean mGestureOnIntercept = true;

    private boolean mFlingOverScrollBack = false;  // fling back while over scrolled.

    private boolean isSpringFirstValue = true;

    public BouncyAdapter(Context context, RecyclerView recyclerView,
                         RecyclerView.Adapter adapter, BouncyConfig config) {
        if (recyclerView == null)
            throw new RuntimeException("null RecyclerView");

        if (adapter == null)
            throw new RuntimeException(("null adapter"));

        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager))
            throw new RuntimeException("RecyclerView must use LinearLayoutManager");

        mContext = context;
        mAdapter = adapter;
        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mConfig = config;
        mGapLimitPx = (int) dpToPx(mConfig.gapLimit);

        mFooterView = createGapView();
        mHeaderView = createGapView();

        mScroller = new DecelerateSmoothScroller(context);
        mSpringScroller = new SpringScroller(config.tension, config.friction, this);

        initRecyclerView();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER)
            return new HeaderHolder(mHeaderView);

        if (viewType == VIEW_TYPE_FOOTER)
            return new FooterHolder(mFooterView);

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0 && position != getItemCount() - 1) {
            mAdapter.onBindViewHolder(holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_HEADER;

        if (position == getItemCount() - 1)
            return VIEW_TYPE_FOOTER;

        return mAdapter.getItemViewType(position - 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onSpringUpdate(int currX, int currY) {
        if (!mShouldUseSpring)
            return;

        synchronized (lockSpring) {
            final int visibleHeader = getHeaderVisibleLength();
            final int visibleFooter = getFooterVisibleLength();

            int diff = directionVertical() ? currY : currX;

            if (visibleHeader > 0) {
                diff -= visibleHeader;
            } else {
                diff -= visibleFooter;
            }

            if (diff < 0) {
                // discard the first value
                if (isSpringFirstValue) {
                    isSpringFirstValue = false;
                    return;
                }

                if (!mFlingOverScrollBack) {
                    mRecyclerView.stopScroll();
                }

                if (visibleHeader > 0) {
                    diff *= -1;
                }

                if (mLayoutManager.getReverseLayout()) {
                    diff *= -1;
                }

                scrollBy(diff);
            }
        }
    }

    @Override
    public void onSpringAtRest() {
        mIsScrollBack = false;
    }

    private void initRecyclerView() {
        scrollToPosition(0);
        initOnScrollListener();
        initTouchListener();
    }

    private void initOnScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean gapAlreadyVisible = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final int deltaDist = (directionVertical() ? dy : dx) * (mLayoutManager.getReverseLayout() ? -1 : 1);

                if (footerOccupiesWholeView()) {
                    mPrevFooterVisible = Math.max(0, mPrevFooterVisible + deltaDist);
                }

                final int state = recyclerView.getScrollState();
                final boolean usingScrollBy = (state == RecyclerView.SCROLL_STATE_IDLE && deltaDist != 0);
                final boolean isDragging = (state == RecyclerView.SCROLL_STATE_DRAGGING);

                computeScrollSpeed(dx, dy);

                if (!isDragging && !usingScrollBy) {
                    final int footerVisible = getFooterVisibleLength();
                    final int headerVisible = getHeaderVisibleLength();

                    final boolean scrolledBackToOtherSide = mIsScrollBack && ((deltaDist > 0 && footerVisible > 0)
                            || (deltaDist < 0 && headerVisible > 0));

                    if (scrolledBackToOtherSide) {
                        gapAlreadyVisible = true;
                        mIsScrollBack = false;
                        mSpringScroller.stopScroll();
                        minDistanceToScrollBack = getMinDistanceToScrollBack(mScrollSpeed, headerVisible, footerVisible);
                    }

                    if (footerVisible == 0 && headerVisible == 0) {
                        gapAlreadyVisible = false;
                        mIsScrollBack = false;

                        mSpringScroller.stopScroll();
                        minDistanceToScrollBack = 1;

                    } else if (!mIsScrollBack) {
                        if (!gapAlreadyVisible) {
                            // check if it's already exceeded the distance to scroll back
                            minDistanceToScrollBack = getMinDistanceToScrollBack(mScrollSpeed, headerVisible, footerVisible);
                            gapAlreadyVisible = true;

                            // scroll back
                            if (headerVisible >= minDistanceToScrollBack || footerVisible >= minDistanceToScrollBack) {
                                scrollBack(headerVisible, footerVisible);
                            } else {
                                reduceScrollSpeed(mScrollSpeed, headerVisible);
                            }
                        } else if (headerVisible >= minDistanceToScrollBack || footerVisible >= minDistanceToScrollBack) {
                            scrollBack(headerVisible, footerVisible);
                        }
                    }
                }
            }
        });

    }

    private void initTouchListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPrevTime = SystemClock.elapsedRealtime();
                        mShouldUseSpring = false;

                        mSpringScroller.stopScroll();
                        mIsScrollBack = false;
                        rv.stopScroll();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        onActionUp();
                        break;
                }

                mGestureOnIntercept = true;
                mGestureDetector.onTouchEvent(e);

                // return true so that RecyclerView won't scroll when the user scroll.
                // We scroll it using scrollBy().
                return shouldInterceptTouch();
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                mGestureOnIntercept = false;
                mGestureDetector.onTouchEvent(e);

                switch (e.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mGestureOnIntercept = true;
                        onActionUp();
                        break;
                }
            }
        });
    }

    private void onActionUp() {
        final int footerVisible = getFooterVisibleLength();
        final int headerVisible = getHeaderVisibleLength();
        final boolean overScrolled = (footerVisible > 0 || headerVisible > 0);

        if (overScrolled) {
            minDistanceToScrollBack = getMinDistanceToScrollBack(mScrollSpeed, headerVisible, footerVisible);
            boolean reduceHeaderSpeed = (headerVisible > 0) && (headerVisible < minDistanceToScrollBack);
            boolean reduceFooterSpeed = (footerVisible > 0) && (footerVisible < minDistanceToScrollBack);

            if (reduceHeaderSpeed || reduceFooterSpeed) {
                reduceScrollSpeed(mScrollSpeed, headerVisible);
            } else {
                scrollBack(headerVisible, footerVisible);
            }
        }

        mShouldUseSpring = true;
    }

    private boolean shouldInterceptTouch() {
        final int headerVisible = getHeaderVisibleLength();
        final int footerVisible = getFooterVisibleLength();

        return headerVisible > 0 || footerVisible > 0;
    }

    private void computeScrollSpeed(int dx, int dy) {
        long currTime = SystemClock.elapsedRealtime();
        int deltaDist = (directionVertical() ? dy : dx);

        if (mFirstScrollBy) {
            mFirstScrollBy = false;
            int correctedDeltaDist = deltaDist * (mLayoutManager.getReverseLayout() ? -1 : 1);

            if (correctedDeltaDist > 0)
                deltaDist = getFooterVisibleLength();
            else if (correctedDeltaDist < 0)
                deltaDist = getHeaderVisibleLength();

            if (mLayoutManager.getReverseLayout()) {
                deltaDist *= -1;
            }
        }

        mScrollSpeed = (double) deltaDist / (currTime - mPrevTime);
        mPrevTime = currTime;
    }

    private final Object lockSpring = new Object();

    private void scrollBack(int headerVisible, int footerVisible) {
        synchronized (lockSpring) {
            mIsScrollBack = true;
            isSpringFirstValue = true;

            mRecyclerView.stopScroll();

            if (headerVisible > 0) {
                startSpringScroll(headerVisible);
            } else {
                startSpringScroll(footerVisible);
            }
        }
    }

    private int getMinDistanceToScrollBack(double speed, int headerVisible, int footerVisible) {
        if (mLayoutManager.getReverseLayout())
            speed *= -1.0;

        if (headerVisible > 0) {
            if (speed >= 0)
                return 0;

            return (int) Math.min((mGapLimitPx / mConfig.speedFactor * pxToDp(-speed)), mGapLimitPx);
        }

        if (footerVisible == 0 || speed <= 0)
            return 0;

        return (int) Math.min((mGapLimitPx / mConfig.speedFactor * pxToDp(speed)), mGapLimitPx);
    }

    private void reduceScrollSpeed(double speed, int headerVisible) {
        mRecyclerView.stopScroll();
        int distToStop = minDistanceToScrollBack;

        mScroller.setScrollVector(getDecelVector(headerVisible));
        mScroller.setTargetPosition(getDecelTargetPos(headerVisible));

        mScroller.setDistanceToStop(distToStop);
        mScroller.setInitialSpeed((float) Math.abs(speed));

        mLayoutManager.startSmoothScroll(mScroller);
    }

    private PointF getDecelVector(int headerVisible) {
        if (headerVisible > 0) {
            if (directionVertical())
                return new PointF(0, (!mLayoutManager.getReverseLayout() ? -1 : 1));
            else
                return new PointF((!mLayoutManager.getReverseLayout() ? -1 : 1), 0);
        }

        if (directionVertical())
            return new PointF(0, (!mLayoutManager.getReverseLayout() ? 1 : -1));

        return new PointF((!mLayoutManager.getReverseLayout() ? 1 : -1), 0);
    }

    private int getDecelTargetPos(int headerVisible) {
        if (headerVisible > 0)
            return 0;

        return getItemCount() - 1;
    }

    private final GestureDetectorCompat mGestureDetector = new GestureDetectorCompat(mContext,
            new GestureDetector.SimpleOnGestureListener() {
                int scrollByCount = 0;

                @Override
                public boolean onDown(MotionEvent e) {
                    scrollByCount = 0;
                    mFlingOverScrollBack = false;
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    final int headerVisible = getHeaderVisibleLength();
                    final int footerVisible = getFooterVisibleLength();

                    int visible = (headerVisible > 0) ? headerVisible : footerVisible;

                    if (visible > 0) {
                        scrollByCount++;
                        mFirstScrollBy = (scrollByCount == 1);

                        double ratioVisible = (double) visible / mGapLimitPx;
                        float distance = directionVertical() ? distanceY : distanceX;
                        double scrollDist = Math.abs(distance - distance * ratioVisible);

                        if (distance < 0) {
                            scrollDist *= -1;
                        }

                        scrollBy((int) scrollDist);
                    }

                    // still in onTouchEvent, manually scroll the recycler view.
                    else if (visible == 0 && !mGestureOnIntercept) {
                        mRecyclerView.scrollBy((int) distanceX, (int) distanceY);
                    }

                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, final float velocityY) {
                    final int headerVisible = getHeaderVisibleLength();
                    final int footerVisible = getFooterVisibleLength();

                    float deltaVel = directionVertical() ? velocityY : velocityX;
                    if (mLayoutManager.getReverseLayout())
                        deltaVel *= -1.0;

                    final boolean gapVisible = headerVisible > 0 || footerVisible > 0;
                    final boolean isFlingOverBack = gapVisible && !mGestureOnIntercept &&
                            ((headerVisible > 0 && deltaVel < 0) || (footerVisible > 0 && deltaVel > 0));

                    // gaps are not visible, use regular fling.
                    if (!gapVisible && !mGestureOnIntercept) {
                        mHandlerUI.post(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.fling((int) -velocityX, (int) -velocityY);
                            }
                        });
                    }

                    // gap is visible, only fling if it's fling back.
                    else if (isFlingOverBack) {
                        mHandlerUI.post(new Runnable() {
                            @Override
                            public void run() {
                                mFlingOverScrollBack = true;
                                mRecyclerView.fling((int) -velocityX, (int) -velocityY);
                            }
                        });
                    }

                    return true;
                }
            });

    private void scrollBy(int dist) {
        if (directionVertical()) {
            mRecyclerView.scrollBy(0, dist);
        } else {
            mRecyclerView.scrollBy(dist, 0);
        }
    }

    private void startSpringScroll(int dist) {
        if (directionVertical()) {
            mSpringScroller.startScroll(0, dist);
        } else {
            mSpringScroller.startScroll(dist, 0);
        }
    }

    private int contentSizeLessThanView() {
        final int recyclerSize = directionVertical() ? mRecyclerView.getHeight() : mRecyclerView.getWidth();
        return Math.max(recyclerSize - estimateContentSize(), 0);
    }

    private int estimateContentSize() {
        int total = 0;
        int count = 0;

        for (int i = mAdapter.getItemCount() - 1; i >= 0 && count < mConfig.viewCountEstimateSize; i--) {
            View view = mLayoutManager.findViewByPosition(i + 1);

            if (view != null) {
                Rect rect = new Rect();
                mLayoutManager.getDecoratedBoundsWithMargins(view, rect);
                int itemHeight = Math.abs(directionVertical() ? rect.height() : rect.width());

                count++;
                total += itemHeight;
            }
        }

        if (count > 0) {
            double average = (double) total / count;
            return (int) (average * mAdapter.getItemCount());
        }

        return 0;
    }

    private int getFooterVisibleLength() {
        if (footerOccupiesWholeView()) {
            return mPrevFooterVisible;
        }

        // footer is not visible
        if (mLayoutManager.findLastVisibleItemPosition() != getItemCount() - 1) {
            mPrevFooterVisible = 0;
            return 0;
        }

        int result;
        if (directionVertical()) {
            if (!mLayoutManager.getReverseLayout()) {
                result = getBottomVisible(mFooterView);
            } else {
                result = getTopVisible(mFooterView);
            }
        } else {
            if (!mLayoutManager.getReverseLayout()) {
                result = getRightVisible(mFooterView);
            } else {
                result = getLeftVisible(mFooterView);
            }
        }

        if (mAdapter.getItemCount() <= mConfig.maxAdapterSizeToEstimate) {
            result -= contentSizeLessThanView();
        }

        result = Math.max(0, result);
        mPrevFooterVisible = result;
        return result;
    }

    private int getHeaderVisibleLength() {
        // header is not visible
        if (mLayoutManager.findFirstVisibleItemPosition() != 0)
            return 0;

        if (directionVertical()) {
            if (!mLayoutManager.getReverseLayout()) {
                return getTopVisible(mHeaderView);
            } else {
                return getBottomVisible(mHeaderView);
            }
        }

        if (!mLayoutManager.getReverseLayout()) {
            return getLeftVisible(mHeaderView);
        }

        return getRightVisible(mHeaderView);
    }

    private int getTopVisible(View view) {
        return Math.max(0, view.getBottom() - mRecyclerView.getPaddingTop());
    }

    private int getBottomVisible(View view) {
        return Math.max(0, mRecyclerView.getHeight() - view.getTop() - mRecyclerView.getPaddingBottom());
    }

    private int getLeftVisible(View view) {
        return Math.max(0, view.getRight() - mRecyclerView.getPaddingLeft());
    }

    private int getRightVisible(View view) {
        return Math.max(0, mRecyclerView.getWidth() - view.getLeft() - mRecyclerView.getPaddingRight());
    }

    private boolean footerOccupiesWholeView() {
        if (getItemCount() == 0)
            return false;

        final int firstX = mRecyclerView.getPaddingLeft();
        final int firstY = mRecyclerView.getPaddingTop();
        final int lastX = mRecyclerView.getWidth() - 1 - mRecyclerView.getPaddingRight();
        final int lastY = mRecyclerView.getHeight() - 1 - mRecyclerView.getPaddingBottom();

        if (!mLayoutManager.getReverseLayout()) {
            if (directionVertical()) {
                return (mRecyclerView.findChildViewUnder(firstX, firstY) == mFooterView) ||
                        (mRecyclerView.findChildViewUnder(lastX, firstY) == mFooterView);
            } else {
                return (mRecyclerView.findChildViewUnder(firstX, firstY) == mFooterView) ||
                        (mRecyclerView.findChildViewUnder(firstX, lastY) == mFooterView);
            }
        }

        if (directionVertical()) {
            return (mRecyclerView.findChildViewUnder(firstX, lastY) == mFooterView) ||
                    (mRecyclerView.findChildViewUnder(lastX, lastY) == mFooterView);
        } else {
            return (mRecyclerView.findChildViewUnder(firstX, lastY) == mFooterView) ||
                    (mRecyclerView.findChildViewUnder(lastX, firstY) == mFooterView);
        }
    }

    private void scrollToPosition(int position) {
        if (mRecyclerView instanceof RecyclerViewBouncy) {
            mRecyclerView.scrollToPosition(position);
        } else {
            mRecyclerView.scrollToPosition(position + 1);
        }
    }

    private View createGapView() {
        final View view = new View(mContext);
        final int width = directionVertical() ? ViewGroup.LayoutParams.MATCH_PARENT : (int) dpToPx(GAP_SIZE);
        final int height = directionVertical() ? (int) dpToPx(GAP_SIZE) : ViewGroup.LayoutParams.MATCH_PARENT;

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);

        if (directionVertical()) {
            params.width = 1;
        } else {
            params.height = 1;
        }

        view.setLayoutParams(params);

        return view;
    }

    private boolean directionVertical() {
        return mLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL;
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View v) {
            super(v);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View v) {
            super(v);
        }
    }

    private double dpToPx(double dp) {
        Resources resources = mContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((double) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private double pxToDp(double px) {
        Resources resources = mContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((double) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private String format(double value) {
        return String.format(Locale.US, "%1$,.2f", value);
    }

    private String toStringScrollState(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                return "dragging";
            case RecyclerView.SCROLL_STATE_IDLE:
                return "idle";
            case RecyclerView.SCROLL_STATE_SETTLING:
                return "settling";
        }
        return "";
    }
}



