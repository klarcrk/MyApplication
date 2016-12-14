/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.progressbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import app.witness.com.myapplication.R;

public class FlikerProgressbar extends View implements Runnable {

    private int DEFAULT_HEIGHT_DP = 35;

    private float MAX_PROGRESS = 100f;

    private Paint textPaint;

    private Paint bgPaint;

    private String progressText;

    private Rect textBouds;

    /**
     * 左右来回移动的滑块
     */
    private Bitmap flikerBitmap;

    /**
     * 滑块移动最左边位置，作用是控制移动
     */
    private float flickerLeft;

    /**
     * 进度条 bitmap ，包含滑块
     */
    private Bitmap pgBitmap;

    private Canvas pgCanvas;

    /**
     * 当前进度
     */
    private float progress;

    private boolean isFinish;

    private boolean isStop;

    /**
     * 下载中颜色
     */
    private int loadingColor;

    /**
     * 暂停时颜色
     */
    private int stopColor;

    /**
     * 进度文本、边框、进度条颜色
     */
    private int progressColor;

    private int textSize;

    private Thread thread;
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    public FlikerProgressbar(Context context) {
        super(context);
        initAttrs(null);
    }

    public FlikerProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public FlikerProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlikerProgressbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlikerProgressbar);
            textSize = (int) typedArray.getDimension(R.styleable.FlikerProgressbar_textSize, dip2px(getContext(), 12));
            loadingColor = typedArray.getColor(R.styleable.FlikerProgressbar_loadingColor, Color.parseColor("#40c4ff"));
            stopColor = typedArray.getColor(R.styleable.FlikerProgressbar_stopColor, Color.parseColor("#ff9800"));
        } else {
            textSize = dip2px(getContext(), 12);
            loadingColor = Color.parseColor("#40c4ff");
            stopColor = Color.parseColor("#ff9800");
        }
    }

    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textBouds = new Rect();

        progressColor = loadingColor;
        flikerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flicker);
        flickerLeft = -flikerBitmap.getWidth();
        pgBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        pgCanvas = new Canvas(pgBitmap);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST:
                height = dip2px(getContext(), DEFAULT_HEIGHT_DP);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = heightSpecSize;
                break;
        }
        setMeasuredDimension(widthSpecSize, height);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1 边框
        drawBorder(canvas);
        //2 进度
        drawProgress();
        canvas.drawBitmap(pgBitmap, 0, 0, bgPaint);
        // 3 进度text
        drawText(canvas);
        // 4.变色
        drawColorProgressText(canvas);
    }

    private void drawColorProgressText(Canvas canvas) {
        textPaint.setColor(Color.WHITE);
        int tWidth = textBouds.width();
        int tHeight = textBouds.height();
        int xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        int yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        float progressWidth = (progress / MAX_PROGRESS) * getMeasuredWidth();
        if (progressWidth > xCoordinate) {
            canvas.save();
            float right = Math.min(progressWidth, xCoordinate + tWidth);
            canvas.clipRect(0, 0, right, getMeasuredHeight());
            canvas.drawText(getProgressText(), xCoordinate, yCoordinate, textPaint);
            canvas.restore();
        }
    }

    private void drawText(Canvas canvas) {
        textPaint.setColor(progressColor);
        progressText = getProgressText();
        textPaint.getTextBounds(progressText, 0, progressText.length(), textBouds);
        int tWidth = textBouds.width();
        int tHeight = textBouds.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        canvas.drawText(progressText, xCoordinate, yCoordinate, textPaint);
    }

    private void drawProgress() {
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(progressColor);

        float right = progress / MAX_PROGRESS * getWidth();

        pgCanvas.save();
        pgCanvas.clipRect(0, 0, right, getMeasuredHeight());
        pgCanvas.drawColor(progressColor);
        pgCanvas.restore();

        if (!isStop) {
            bgPaint.setXfermode(xfermode);
            pgCanvas.drawBitmap(flikerBitmap, flickerLeft, (getMeasuredHeight()-flikerBitmap.getHeight())/2, bgPaint);
            bgPaint.setXfermode(null);
        }
    }

    private void drawBorder(Canvas canvas) {
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(progressColor);
        bgPaint.setStrokeWidth(dip2px(getContext(), 1));
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
    }

    @Override
    public void run() {
        int width = flikerBitmap.getWidth();
        while (!isStop) {
            flickerLeft += dip2px(getContext(), 5);
            float progressWidth = (progress / MAX_PROGRESS) * getMeasuredWidth();
            if (flickerLeft >= progressWidth) {
                flickerLeft = -width;
            }
            postInvalidate();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }


    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void finishLoad() {

    }

    public String getProgressText() {
        String text = "";
        if (!isFinish) {
            if (!isStop) {
                text = "下载中" + progress + "%";
            } else {
                text = "继续";
            }
        } else {
            text = "下载完成";
        }
        return text;
    }
}
