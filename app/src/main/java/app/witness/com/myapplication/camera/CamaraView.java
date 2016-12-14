package app.witness.com.myapplication.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2016/7/19 17:49.
 * Project MyApplication
 * Copyright (c) 2016 zzkko Inc. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CamaraView extends View implements View.OnClickListener {
    public CamaraView(Context context) {
        super(context);
    }

    public CamaraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CamaraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CamaraView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            init();
        }
    }

    private Paint paint;
    private Rect rect;
    private Camera mCamera;
    private Matrix mMatrix;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        int width = getWidth();
        int height = getHeight();
        rect = new Rect(width / 5, height / 5, width / 5 * 3, height / 5 * 3);
        mCamera = new Camera();
        mMatrix = new Matrix();
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(mMatrix);
        canvas.drawRect(rect, paint);
        canvas.restore();
    }

    @Override
    public void onClick(View v) {
        /*mCamera.save();
        mCamera.restore();*/
        mCamera.rotateX(5);
        mCamera.getMatrix(mMatrix);
        invalidate();
    }
}
