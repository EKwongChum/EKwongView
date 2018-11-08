package com.ekwong.library.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * @author erkang
 * <p>
 *
 * 加载更多动画
 */
public class EkLoadMoreView extends FrameLayout {
    private PinkCircleView mCircle1;
    private PinkCircleView mCircle2;
    private int mRadius = 18;
    private AnimatorSet mAnimatorSet;
    private float[] mLastValues = {0, 0};

    public EkLoadMoreView(Context context) {
        this(context, null);
    }

    public EkLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCircle1 = new PinkCircleView(context);
        mCircle2 = new PinkCircleView(context);
        addView(mCircle1);
        addView(mCircle2);
        initAnim();
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

    private AnimatorListenerAdapter mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mAnimatorSet.start();
        }
    };

    private void initAnim() {

        ValueAnimator rotate = new ValueAnimator();
        rotate.setDuration(400);
        rotate.setObjectValues(new PointF(0, 0));
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                float radian = (float) (fraction * Math.PI);
                // 设置曲线函数  https://en.wikipedia.org/wiki/Fermat%27s_spiral
                float x = (float) (mRadius * Math.sqrt(radian) * Math.cos(radian));
                float y = (float) (mRadius * Math.sqrt(radian) * Math.sin(radian));
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
        move.setDuration(100);
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
        ObjectAnimator scaleXBig1 = ObjectAnimator.ofFloat(mCircle1, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleYBig1 = ObjectAnimator.ofFloat(mCircle1, "scaleY", 0.5f, 1f);
        ObjectAnimator scaleXSmall1 = ObjectAnimator.ofFloat(mCircle1, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleYSmall1 = ObjectAnimator.ofFloat(mCircle1, "scaleY", 1f, 0.5f);
        // 回归中间时的放大
        ObjectAnimator scaleXBig2 = ObjectAnimator.ofFloat(mCircle2, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleYBig2 = ObjectAnimator.ofFloat(mCircle2, "scaleY", 0.5f, 1f);
        ObjectAnimator scaleXSmall2 = ObjectAnimator.ofFloat(mCircle2, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleYSmall2 = ObjectAnimator.ofFloat(mCircle2, "scaleY", 1f, 0.5f);

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

        mAnimatorSet.setDuration(500);

    }

}
