/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.fresco;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.fresco.zoomable.DoubleTapZoomableController;
import app.witness.com.myapplication.fresco.zoomable.ZoomableController;
import app.witness.com.myapplication.fresco.zoomable.ZoomableDraweeView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FrescoActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ZoomableDraweeView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);
        ButterKnife.bind(this);
        ZoomableController zoomController = DoubleTapZoomableController.newInstance(this);
        img.setZoomableController(zoomController);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setPlaceholderImage(new ColorDrawable(Color.GREEN))
                .setFadeDuration(300)
                .build();
        img.setHierarchy(hierarchy);

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("http://map.ourivy.com/m/a/cn.jpg"))
                .setTapToRetryEnabled(true)
                .build();
        img.setController(controller);

    }
}
