package com.ekwong.library.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


/**
 * @author erkang
 * <p>
 * 粉色圆圈
 */
public class PinkCircleView extends View {

    private Paint mPaint;

    /**
     * 圆点的半径
     */
    private float mPointRadius = 16;

    public PinkCircleView(Context context) {
        super(context);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        setPaint(paint);
    }

    public PinkCircleView(Context context, Paint paint) {
        super(context);
        setPaint(paint);
    }

    private void setPaint(Paint paint) {
        mPaint = paint;
    }

    public void setPointRadius(float pointRadius) {
        mPointRadius = pointRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        canvas.drawCircle(centerX, centerY, mPointRadius, mPaint);
    }
}
