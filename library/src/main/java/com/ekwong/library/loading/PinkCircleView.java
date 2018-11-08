package com.ekwong.library.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ekwong.library.R;


/**
 * @author erkang
 * <p>
 * 粉色圆圈
 */
public class PinkCircleView extends View {

    private Paint mPaint;

    public PinkCircleView(Context context) {
        this(context, null);
    }

    public PinkCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBase(context);
    }

    private void initBase(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.common_pink_color));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        canvas.drawCircle(centerX, centerY, 16, mPaint);
    }
}
