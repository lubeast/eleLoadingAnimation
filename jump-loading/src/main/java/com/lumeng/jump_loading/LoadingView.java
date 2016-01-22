package com.lumeng.jump_loading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author lumeng on 1/21/16.
 */
public class LoadingView extends FrameLayout implements JumpingView.LoadingListener {
    /**
     * Size of this view
     */
    private static final int DEFAULT_SIZE = 100;
    /**
     * store the former state of this view
     */
    private int lastVisibleStatus = INVISIBLE;

    JumpingView jumpingView;

    private ObjectAnimator animator;

    private View view;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initShadow(context);
        initJumpView(context, attrs);
    }

    /**
     * add Shadow view
     * @param context
     */
    private void initShadow(Context context) {
        view = new View(context);
        view.setBackgroundResource(R.drawable.loading_shadow);
        addView(view);
    }

    /**
     * add jump view
     * @param context context
     * @param attrs attrs
     */
    private void initJumpView(Context context, AttributeSet attrs){
        jumpingView = new JumpingView(context, attrs);
        addView(jumpingView);
        jumpingView.addListener(this);
    }

    /**
     * shadow start animation with jump view
     */
    private void startShadowAnimation() {
        animator = ObjectAnimator.ofFloat(view, "scaleX", 1, 1.5f, 1);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(jumpingView.getmDuration() * 2).start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        view.layout(dp2px(35), dp2px(80), dp2px(65), dp2px(95));
        jumpingView.layout(0, 0, dp2px(100), dp2px(100));
    }

    public void setResource(@NonNull SparseArray<Integer> items) {
        jumpingView.setResource(items);
    }

    public void stopAnimation() {
        jumpingView.stopAnimation();
    }

    public void startAnimation() {
        jumpingView.startAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

    @Override
    public void start() {
        startShadowAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (lastVisibleStatus == INVISIBLE && visibility == VISIBLE) {
            invalidate();
            startShadowAnimation();
            startAnimation();
        }

        if (lastVisibleStatus == VISIBLE && visibility == INVISIBLE) {
            if (animator.isRunning()) {
                animator.end();
                stopAnimation();
            }
        }
    }
}