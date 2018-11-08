package com.ekwong.library.loading;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ekwong.library.R;


/**
 * @author erkang
 * <p>
 * 加载中动画
 */
public class EkLoadingView extends FrameLayout {

    private Animatable mAnim;
    private AppCompatImageView mImageView;

    public EkLoadingView(Context context) {
        this(context, null);
    }

    public EkLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageView = new AppCompatImageView(context);
        mImageView.setImageResource(R.drawable.anim_vector_loading);
        addView(mImageView);
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

}
