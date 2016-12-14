/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.addview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import app.witness.com.myapplication.R;


/**
 * Created by yh on 2016/1/20.
 * 系统没有android.permission.SYSTEM_ALERT_WINDOW权限且系统版本小于4.4不能使用ShopBagFloatView
 * 在必要的地方加了固定的按钮，这时应该显示固定的按钮
 */
public class ShopBagFloatView extends FrameLayout {

    //用于判断是否在onstart的时候显示shopBag；在MainTabsActivity中设置值为true；在退出登录后设置为false；
    public static boolean isShowedBefore = false;

    private static float preX = 0;
    private static float preY = 0;
    private static boolean bagInitiatedBefore = false;

    private static boolean rtlFlag;


    public static void showShopBagFloatView(Activity activity) {
        rtlFlag = PhoneUtil.shouldReversLayout();
        Context appContext = activity.getApplicationContext();
        final ShopBagFloatView floatView = new ShopBagFloatView(appContext);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        floatView.setHeightAndWidth(heightPixels, widthPixels);
        final ShopBagFloatView shopBagFloatView = floatView;
        shopBagFloatView.findViewById(R.id.bag_image).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止瞬间点击多次
                long lastTime = 0;
                if (v.getTag() != null) {
                    lastTime = (long) v.getTag();
                }
                long currTime = System.currentTimeMillis();
                v.setTag(currTime);
                if (currTime - lastTime < 600) {
                    return;
                }
                Context context = v.getContext();
            }
        });
        Window window = activity.getWindow();
        //获取WindowManager
        //设置LayoutParams(全局变量）相关参数
        //设置悬浮窗口长宽数据
        //以屏幕左上角为原点，设置x、y初始值
        //actionBarSize
        //设置y轴最大值
        floatView.setMaxY(heightPixels);
        //显示myFloatView图像
        floatView.insureWindowParams();
        floatView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (floatView != null) {
                    floatView.scaleIn();
                    floatView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        final int icoSize = DensityUtil.dip2px(activity, 55);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(icoSize, icoSize);
        final ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (rootView != null) {
            boolean notAdd = true;
            int childCount = rootView.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    View rootChild = rootView.getChildAt(i);
                    if (rootChild == floatView) {
                        notAdd = false;
                        break;
                    }
                }
            }
            if (notAdd) {
                floatView.setX(preX);
                floatView.setY(preY);
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        floatView.setMaxY(rootView.getMeasuredHeight() - icoSize);
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                rootView.addView(floatView, layoutParams);
            }
        }
        bagInitiatedBefore = true;
    }


    private void scaleIn() {
        setScaleX(0.2f);
        setScaleY(0.2f);
        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("scaleX", 0.2f, 1);
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("scaleY", 0.2f, 1);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(this, xHolder, yHolder);
        scaleAnim.setDuration(200);
        scaleAnim.start();
    }

    private void scaleOut() {
        setScaleX(1f);
        setScaleY(1f);
        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("scaleX", 1, 0.2f);
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofFloat("scaleY", 1, 0.2f);
        ObjectAnimator scaleAnim = ObjectAnimator.ofPropertyValuesHolder(this, xHolder, yHolder);
        scaleAnim.setDuration(200);
        scaleAnim.start();
        scaleAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }


    public ShopBagFloatView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_shop_bag, this, true);
        countTv = (TextView) findViewById(R.id.shoppingbag_count_text);
        icoImage = (ImageView) findViewById(R.id.bag_image);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration) / 2;
    }

    TextView countTv;
    ImageView icoImage;

    public void setText(String content) {
        countTv.setText(content);
        if (MODE == MODE_SHOPBAG) {
            countTv.setVisibility(View.VISIBLE);
        }
    }

    public TextView getCountTv() {
        return countTv;
    }


    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setHeightAndWidth(int heightPixels, int widthPixels) {
        this.screenHeight = heightPixels;
        this.screenWidth = widthPixels;
        maxY = widthPixels;
    }

    private int maxY = 100;
    private float mTouchStartX;
    private float mTouchStartY;
    private float mTouchMoveX;
    private float mTouchMoveY;
    private float x;
    private float y;
    int screenHeight = 0;
    int screenWidth = 0;


    private int mTouchSlop = 0;
    float mXDown;
    float mYDown;
    float mXMove;
    float mYMove;
    float mXLastTouch;
    float mYLastTouch;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimate) {
            return true;
        }
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = ev.getRawX();
        y = ev.getRawY() - 25;   //25是系统状态栏的高度
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mYDown = ev.getRawY();
                mXLastTouch = mXDown;
                mYLastTouch = mYDown;
                mTouchStartX = ev.getX();
                mTouchStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                mYMove = ev.getRawY();
                float diff = Math.abs((mXMove - mXLastTouch) + (mYMove - mYLastTouch));
                mXLastTouch = mXMove;
                // slop
                System.out.println("diff" + diff);
                System.out.println("slop" + mTouchSlop);
                //判断是否拦截触摸事件，拦截则开始控件的拖动，否则会将事件传递给子控件
                if (diff > mTouchSlop) {
                    //拖动
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    private long pressTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimate) {
            return false;
        }
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - 25;   //25是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                pressTime = System.currentTimeMillis();
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
               /* long currTime = System.currentTimeMillis();
                if (currTime - pressTime > 180) {
                    updateViewPosition();
                }*/
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                onTouchEnd();
                /*long endTime = System.currentTimeMillis();
                if (endTime - pressTime > 180) {
                    updateViewPosition();
                } else {
                    performClick();
                }*/
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return true;
    }


    private boolean isAnimate = false;

    private void onTouchEnd() {
        //int xMargin = wmParams.width / 4;
        //控制距离屏幕边框的距离
        int xMargin = 0;
        int halfWidth = screenWidth / 2;
        int targetX = xMargin;
        if (getX() > halfWidth) {
            targetX = screenWidth - getWidth() - xMargin;
        } else {
            preX = getWidth();
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(getX(), targetX);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float xValue = (float) animation.getAnimatedValue();
                setX(xValue);
                preX = xValue;
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
                preX = getX();
            }
        });
        valueAnimator.start();
    }

    //限制浮动窗口位置参数值
    private void insureWindowParams() {
        int xMargin = 0;
        //int xMargin = wmParams.width / 4;
        if (getX() < 0) {
            setX(xMargin);
        } else if (getX() + getWidth() + xMargin > screenWidth) {
            setX(screenWidth - getWidth() - xMargin);
        }
        if (getY() < 0) {
            setY(0);
        } else if (getY() > maxY) {
            setY(maxY);
        }
        preX = getX();
        preY = getY();
    }

    private void updateViewPosition() {
        setX(x - getWidth() / 2 - mTouchStartX);
        setY(y - getHeight() / 2 - mTouchStartY);
        //x,y;
        insureWindowParams();
    }

    public static final int MODE_SHOPBAG = 0;
    public static final int MODE_CUSTOM_SERVICE = 1;
    public static final int MODE_CUSTOM_SERVICE_WITH_ORDER = 2;
    public int MODE = 0;

    private void reverserMode() {
        if (MODE == MODE_CUSTOM_SERVICE) {
            showShopbagMode();
        } else {
            showCustomServiceMode();
        }
    }

    public void showCustomServiceMode() {
        if (MODE != MODE_CUSTOM_SERVICE) {
            //MODE_CUSTOM_SERVICE_WITH_ORDER与MODE_CUSTOM_SERVICE使用的是同一个角度
            if (MODE != MODE_CUSTOM_SERVICE_WITH_ORDER) {
                setRotationY(0);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 180);
                valueAnimator.setDuration(300);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    private float preValue = 0;

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float animValue = (Float) animation.getAnimatedValue();
                        setRotationY(animValue);
                    }
                });
                valueAnimator.start();
            }
            MODE = MODE_CUSTOM_SERVICE;
        }
    }


    public void showShopbagMode() {
        if (MODE != MODE_SHOPBAG) {
            MODE = MODE_SHOPBAG;
            setRotationY(180);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(180, 360);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private float preValue = 0;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float animValue = (Float) animation.getAnimatedValue();
                    setRotationY(animValue);
                    if (preValue < 270 && animValue >= 270) {
                        //转到中间的地方
                        countTv.setVisibility(VISIBLE);
                        icoImage.setImageResource(R.drawable.ico_float_shopbag);
                    } else {
                        preValue = animValue;
                    }
                }
            });
            valueAnimator.start();
        }
    }

}


