package com.lumeng.jump_loading;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author lumeng on 1/20/16.
 */
public class JumpingView extends View {

    public static final int FRAME_SIZE = 100;

    SparseArray<Integer> mItems = new SparseArray<>();

    ObjectAnimator animator;

    // set by the xml
    private int mDuration = 0;
    // current position
    private int currentPosition = 0;

    private boolean mHasAnimation;

    private LoadingListener listener;

    private View shadow;

    public JumpingView(Context context) {
        this(context, null);
    }

    public JumpingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumpingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.JumpingView, 0, 0);
        if (attr != null) {
            try {
                mDuration = attr.getInt(R.styleable.JumpingView_jl_duration, 400);
            } finally {
                attr.recycle();
            }
        }
        animator = ObjectAnimator.ofFloat(this, "translationY", 0, -100, 0);
        animator.addListener(animatorListener);

        shadow = new View(context);
        shadow.setBackgroundResource(R.drawable.loading_shadow);
        shadow.layout(dp2px(55 / 2), dp2px(70), dp2px(72), dp2px(90));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mItems.size() != 0) {
            Drawable drawable = getContext().getResources().getDrawable(mItems.get(currentPosition), null);
            if (drawable != null) {
                drawable.setBounds(new Rect(dp2px(55 / 2), dp2px(45), dp2px(72), dp2px(90)));
                drawable.draw(canvas);
                canvas.restore();
            }
        }

        if (!mHasAnimation) {
            mHasAnimation = true;
            initAnimator();
        }

    }

    /**
     * It will call this method if the animation is end, to update data and do animation again.
     */
    private void initAnimator() {
        animator.setDuration(mDuration * 2);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (listener != null) {
                listener.start();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            updateResource();
            postInvalidate();
        }
    };

    /**
     * update position whenever an animation is end
     */
    private void updateResource() {
        currentPosition = (currentPosition + 1) % mItems.size();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(FRAME_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(FRAME_SIZE), heightMeasureSpec);
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

    /**
     * dp2px
     * @param dpValue dp
     * @return px
     */
    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

    public void stopAnimation() {
        if (animator.isRunning()) {
            animator.end();
        }
    }

    public void startAnimation() {
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setResource(@NonNull SparseArray<Integer> items) {
        this.mItems = items;
    }

    public void addListener(@NonNull LoadingListener listener) {
        this.listener = listener;
    }

    public interface LoadingListener {
        void start();
    }
}
