package com.ekwong.library.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.ekwong.library.R;

/**
 * @author erkang
 * <p>
 * <p>
 * 加载更多动画
 */
public class EkLoadMoreView extends FrameLayout {
    private PinkCircleView mCircle1;
    private PinkCircleView mCircle2;

    private Paint mPaint;

    private static final float DEFAULT_POINT_RADIUS = 16;
    private static final float DEFAULT_SPIRAL_RADIUS = 18;
    private static final int DEFAULT_ROTATION_DURATION = 400;
    private static final int DEFAULT_REGRESSION_DURATION = 100;
    private static final float DEFAULT_POINT_SCALE = 0.5f;
    /**
     * 两个圆点的半径
     */
    private float mPointRadius;

    /**
     * 圆点颜色
     */
    private int mColorRes;

    /**
     * 螺线半径?
     */
    private float mSpiralRadius;

    /**
     * 旋转周期
     */
    private int mRotationDuration;

    /**
     * 回归周期
     */
    private int mRegressionDuration;

    private float mPointScale;

    /**
     * 动画组合
     */
    private AnimatorSet mAnimatorSet;
    /**
     * 上一次的值
     */
    private float[] mLastValues = {0, 0};

    public EkLoadMoreView(Context context) {
        this(context, null);
    }

    public EkLoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EkLoadMoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EkLoadMoreView, defStyle, 0);
        mColorRes = a.getColor(R.styleable.EkLoadMoreView_point_color, Color.BLACK);
        mPointRadius = a.getDimension(R.styleable.EkLoadMoreView_point_radius, DEFAULT_POINT_RADIUS);
        mSpiralRadius = a.getDimension(R.styleable.EkLoadMoreView_spiral_radius, DEFAULT_SPIRAL_RADIUS);
        mRotationDuration = a.getInt(R.styleable.EkLoadMoreView_rotation_duration, DEFAULT_ROTATION_DURATION);
        mRegressionDuration = a.getInt(R.styleable.EkLoadMoreView_regression_duration, DEFAULT_REGRESSION_DURATION);
        mPointScale = a.getFloat(R.styleable.EkLoadMoreView_point_scale, DEFAULT_POINT_SCALE);
        a.recycle();

        initPoint(context);
        initAnim();
    }

    private void initPoint(Context context) {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorRes);

        mCircle1 = new PinkCircleView(context, mPaint);
        mCircle2 = new PinkCircleView(context, mPaint);
        mCircle1.setPointRadius(mPointRadius);
        mCircle2.setPointRadius(mPointRadius);
        addView(mCircle1);
        addView(mCircle2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimatorSet.addListener(mAnimatorListenerAdapter);
        mAnimatorSet.start();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mAnimatorSet != null) {
            if (visibility == VISIBLE) {
                if (mAnimatorSet.isRunning()) {
                    mAnimatorSet.removeListener(mAnimatorListenerAdapter);
                    mAnimatorSet.end();
                }
                mAnimatorSet.addListener(mAnimatorListenerAdapter);
                mAnimatorSet.start();
            } else {
                if (mAnimatorSet.isRunning()) {
                    mAnimatorSet.removeListener(mAnimatorListenerAdapter);
                    mAnimatorSet.end();
                }
            }
        }
    }

    private AnimatorListenerAdapter mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mAnimatorSet.start();
        }
    };

    private void initAnim() {

        ValueAnimator rotate = new ValueAnimator();
        rotate.setDuration(mRotationDuration);
        rotate.setObjectValues(new PointF(0, 0));
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                float radian = (float) (fraction * Math.PI);
                // 设置曲线函数  https://en.wikipedia.org/wiki/Fermat%27s_spiral
                float x = (float) (mSpiralRadius * Math.sqrt(radian) * Math.cos(radian));
                float y = (float) (mSpiralRadius * Math.sqrt(radian) * Math.sin(radian));
                mLastValues[0] = x;
                mLastValues[1] = y;

                return new PointF(x, y);
            }
        });

        rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                mCircle1.setX(point.x);
                mCircle1.setY(point.y);
                mCircle2.setX(-point.x);
                mCircle2.setY(-point.y);
            }
        });

        ValueAnimator move = new ValueAnimator();
        move.setDuration(mRegressionDuration);
        move.setObjectValues(new PointF(0, 0));
        move.setInterpolator(new LinearInterpolator());
        move.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                float x = (mLastValues[0] - 0) * (1 - fraction);
                float y = (mLastValues[1] - 0) * (1 - fraction);

                return new PointF(x, y);
            }
        });

        move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                mCircle1.setX(point.x);
                mCircle1.setY(point.y);
                mCircle2.setX(-point.x);
                mCircle2.setY(-point.y);
            }
        });

        // 设置转圈时的缩小
        ObjectAnimator scaleXBig1 = ObjectAnimator.ofFloat(mCircle1, "scaleX", mPointScale, 1f);
        ObjectAnimator scaleYBig1 = ObjectAnimator.ofFloat(mCircle1, "scaleY", mPointScale, 1f);
        ObjectAnimator scaleXSmall1 = ObjectAnimator.ofFloat(mCircle1, "scaleX", 1f, mPointScale);
        ObjectAnimator scaleYSmall1 = ObjectAnimator.ofFloat(mCircle1, "scaleY", 1f, mPointScale);
        // 回归中间时的放大
        ObjectAnimator scaleXBig2 = ObjectAnimator.ofFloat(mCircle2, "scaleX", mPointScale, 1f);
        ObjectAnimator scaleYBig2 = ObjectAnimator.ofFloat(mCircle2, "scaleY", mPointScale, 1f);
        ObjectAnimator scaleXSmall2 = ObjectAnimator.ofFloat(mCircle2, "scaleX", 1f, mPointScale);
        ObjectAnimator scaleYSmall2 = ObjectAnimator.ofFloat(mCircle2, "scaleY", 1f, mPointScale);

        mAnimatorSet = new AnimatorSet();

        mAnimatorSet.play(scaleXSmall1).with(rotate);
        mAnimatorSet.play(rotate).with(scaleYSmall1);
        mAnimatorSet.play(scaleYSmall1).with(scaleXSmall2);
        mAnimatorSet.play(scaleXSmall2).with(scaleYSmall2);

        mAnimatorSet.play(move).after(scaleYSmall2);

        mAnimatorSet.play(move).with(scaleXBig1);
        mAnimatorSet.play(scaleXBig1).with(scaleYBig1);
        mAnimatorSet.play(scaleYBig1).with(scaleXBig2);
        mAnimatorSet.play(scaleXBig2).with(scaleYBig2);

        mAnimatorSet.setDuration(mRotationDuration + mRegressionDuration);

    }

}
