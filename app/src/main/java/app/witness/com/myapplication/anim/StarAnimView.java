/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import app.witness.com.myapplication.R;

public class StarAnimView extends View {

    public StarAnimView(Context context) {
        super(context);
        init();
    }

    public StarAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Bitmap starImg;
    Paint paint = new Paint();
    Matrix matrix = new Matrix();
    int translateValue = 0;
    private int orginScale = 1;
    private float starScaleValue;
    private int maxDotHeight = 20;
    private float dotHeight = 0;
    private float dotRadiu = 0;
    private float maxStrokeWidth = 5;
    private float strokeWidth = 0;
    private final int dotCount = 15;
    private final float maxScaleValue = 1f;

    private float getMaxScale() {
        return maxScaleValue + orginScale;
    }

    private float getMaxScaleWidth() {
        return starImg.getWidth() * getMaxScale();
    }

    private float getCurrentScaledStarWidth() {
        return starImg.getWidth() * starScaleValue;
    }

    private boolean centerAnimed = false;

    private boolean isAnimating = false;

    public boolean isAnimating() {
        return isAnimating;
    }

    private float getMaxDotRadiu() {
        return getMaxScaleWidth() / 2;
    }

    private float getExtraDotRadiu() {
        return dip2px(getContext(), 3);
    }

    private float getDotRadiu() {
        //初始状态
        float maxRadiu = getMaxDotRadiu();
        float radiuValue;
        if (centerAnimed) {
            radiuValue = maxRadiu;
        } else {
            radiuValue = //动态增大半径值
                    maxRadiu * (0.6f + 0.4f * preScaleValue);
        }
        return radiuValue + getExtraDotRadiu();
    }

    private void init() {
        starImg = BitmapFactory.decodeResource(getResources(), R.drawable.ico_start_cheked_black);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(strokeWidth);
    }

    private float getViewMaxSize() {
        float maxDotRadiu = getMaxDotRadiu();
        return (maxDotRadiu + getExtraDotRadiu() + maxDotHeight)*2;
    }

    private float preScaleValue;

    public void doAnim() {
        centerAnimed = false;
        isAnimating = true;
        ValueAnimator animator = new ValueAnimator();
        animator.setFloatValues(0, maxScaleValue, 0);
        preScaleValue = 0;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animateValue = (float) animation.getAnimatedValue();
                if (animateValue > preScaleValue) {
                    preScaleValue = animateValue;
                } else if (animateValue < preScaleValue) {
                    centerAnimed = true;
                    preScaleValue = animateValue;
                }
                starScaleValue = orginScale + animateValue / 2;
//                matrix.setTranslate(translateValue,translateValue);
                matrix.setScale(starScaleValue, starScaleValue, starImg.getWidth() / 2, starImg.getHeight() / 2);
                //dot
                dotHeight = maxDotHeight * animateValue;
                dotRadiu = getDotRadiu();
                strokeWidth = maxStrokeWidth * animateValue;
                paint.setStrokeWidth(strokeWidth);
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isAnimating = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimating = true;
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int starWidth = starImg.getWidth();
        translateValue = (w - starWidth) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeValue;
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            sizeValue = (int) getViewMaxSize();
        } else {
            sizeValue = MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension(sizeValue, sizeValue);
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(translateValue, translateValue);
        canvas.drawBitmap(starImg, matrix, paint);
        canvas.restore();
        //360度点点
        float dotRadiu = getDotRadiu();
        System.out.println("半径:" + dotRadiu);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        for (int i = 0; i < dotCount; i++) {
            //
            //            dotRadiu
            canvas.save();
            int rotateAngle = i * (360 / dotCount);
            canvas.rotate(rotateAngle, centerX, centerY);
            canvas.drawLine(centerX, centerY - dotRadiu - dotHeight, centerX, centerY - dotRadiu, paint);
            canvas.restore();
        }
    }
}
