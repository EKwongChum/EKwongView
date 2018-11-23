package com.ekwong.library.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ekwong.library.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @author erkang
 * <p>
 * 加载中动画
 */
public class EkLoadingView extends FrameLayout {

    private Animatable mAnim;
    private AppCompatImageView mImageView;
    private CircleImageView mCircleImageView;
    private int mCircleRes;

    public EkLoadingView(Context context) {
        this(context, null);
    }

    public EkLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EkLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EkLoadingView, defStyle, 0);
        mCircleRes = a.getResourceId(R.styleable.EkLoadingView_img_src, 0);
        a.recycle();

        mImageView = new AppCompatImageView(context);
        mImageView.setImageResource(R.drawable.anim_vector_loading);
        mCircleImageView = new CircleImageView(context);
        mCircleImageView.setImageDrawable(context.getDrawable(mCircleRes));
        int value = px2dp(context, 24);
        System.out.println("value=" + value);
        mCircleImageView.setBorderWidth(value);
        mCircleImageView.setBorderColor(getResources().getColor(R.color.color_transparent));
        addView(mImageView);
        addView(mCircleImageView);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnim = (Animatable) mImageView.getDrawable();
        if (mAnim != null) {
            mAnim.start();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mAnim != null) {
            if (visibility == VISIBLE) {
                if (mAnim.isRunning()) {
                    mAnim.stop();
                }
                mAnim.start();
            } else {
                if (mAnim.isRunning()) {
                    mAnim.stop();
                }
            }
        }
    }

    private int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
