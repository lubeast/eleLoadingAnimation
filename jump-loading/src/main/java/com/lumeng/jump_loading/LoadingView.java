package com.lumeng.jump_loading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author lumeng on 1/21/16.
 */
public class LoadingView extends FrameLayout implements JumpingView.LoadingListener {

    private static final int DEFAULT_SIZE = 100;

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

        view = new View(context);
        view.setBackgroundResource(R.drawable.loading_shadow);

        addView(view);

        jumpingView = new JumpingView(context, attrs);
        addView(jumpingView);
        jumpingView.addListener(this);

    }

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

    public void startAnimation(){
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

}