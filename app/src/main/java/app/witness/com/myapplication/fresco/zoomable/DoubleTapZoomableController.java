/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.fresco.zoomable;

import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import app.witness.com.myapplication.fresco.gestures.TransformGestureDetector;

/**
 * Created by imac on 4/28/16.
 */
public class DoubleTapZoomableController extends DefaultZoomableController
        implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    private GestureDetector mGestureDetector;

    public DoubleTapZoomableController(TransformGestureDetector gestureDetector, Context context)
    {
        super(gestureDetector);
        mGestureDetector = new GestureDetector(context, this);
        mGestureDetector.setOnDoubleTapListener(this);
    }

    public static DoubleTapZoomableController newInstance(Context context)
    {
        return new DoubleTapZoomableController(TransformGestureDetector.newInstance(), context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event)
    {
//        PointF point = mapViewToImage(new PointF(event.getX(), event.getY()));
        if (getScaleFactor() > mMinScaleFactor)
        {
            System.out.println("DOUblE TAPPII MIN " + getScaleFactor());
            zoomToImagePoint(0.01f, new PointF(event.getX(), event.getY()));
        }
        else
        {
            System.out.println("DOUblE TAPPII MAX" + getScaleFactor());
            zoomToImagePoint(mMaxScaleFactor, new PointF(event.getX(), event.getY()));
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event)
    {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event, MotionEvent event1, float v, float v1)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event)
    {
    }

    @Override
    public boolean onFling(MotionEvent event, MotionEvent event1, float v, float v1)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (isEnabled())
        {
            mGestureDetector.onTouchEvent(event);
            return super.onTouchEvent(event);
        }
        return false;
    }

}
