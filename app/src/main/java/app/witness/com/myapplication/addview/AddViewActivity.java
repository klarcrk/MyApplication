/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.addview;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.main.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddViewActivity extends BaseActivity {

    @BindView(R.id.button6)
    Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button6)
    public void onClick() {
        ViewGroup viewById = (ViewGroup) findViewById(android.R.id.content);
        Button button = new Button(this);
        button.setText("測試");
        Random random = new Random();
        int height = random.nextInt(1920);
        int width = random.nextInt(1080);
        button.setX(width);
        button.setY(height);
        viewById.addView(button,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
