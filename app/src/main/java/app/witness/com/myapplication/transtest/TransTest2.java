/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.transtest;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.addview.DensityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TransTest2 extends AppCompatActivity {

    @BindView(R.id.activity_trans_test2)
    RelativeLayout activityTransTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_test2);
        ButterKnife.bind(this);
        activityTransTest2.setVisibility(View.INVISIBLE);
        ViewTreeObserver viewTreeObserver = activityTransTest2.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    activityTransTest2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    circularRevealActivity(true, null);
                }
            });
        }
    }

    public void startIntent(View view) {
        Intent intent = new Intent(this, TrnsTest3.class);
        startActivity(intent);
    }

    private void circularRevealActivity(boolean showOrHide, Animator.AnimatorListener listener) {
        int bagRadiu = DensityUtil.dip2px(this, 50) / 2;
        int startX = 540 + bagRadiu;
        int startY = 960 + bagRadiu;
        float finalRadius = Math.max(activityTransTest2.getWidth(), activityTransTest2.getHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator circularReveal;
            if (showOrHide) {
                //显示界面
                circularReveal = ViewAnimationUtils.createCircularReveal(activityTransTest2, startX, startY, 0, finalRadius);
            } else {
                //隐藏界面
                circularReveal = ViewAnimationUtils.createCircularReveal(activityTransTest2, startX, startY, finalRadius, 0);
                if (listener != null) {
                    circularReveal.addListener(listener);
                }
            }
            activityTransTest2.setVisibility(View.VISIBLE);
            circularReveal.setDuration(400);
            circularReveal.start();
        }
    }

}
