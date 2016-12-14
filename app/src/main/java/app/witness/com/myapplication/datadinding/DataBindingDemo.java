/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.datadinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.databinding.ActivityDataBindingDemoBinding;
import app.witness.com.myapplication.datadinding.domain.User;
import app.witness.com.myapplication.main.BaseActivity;

public class DataBindingDemo extends BaseActivity {
    User user;
    ActivityDataBindingDemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.
        ActivityDataBindingDemoBinding demoBinding = ActivityDataBindingDemoBinding.inflate(getLayoutInflater());
        setContentView(demoBinding.getRoot());
        //2.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_demo);
        user = new User();
        user.setUserAge("123");
        user.setUserName("Tom");
        binding.setUser(user);
        binding.setMActivity(this);
        Map<String, String> data = new HashMap<>();
        data.put("new", "no");
        binding.setDatas(data);
    }

    public void onButtonClick(View view) {
        user.setUserName("Joo");
    }

    public void onSaveClick() {
        user.setUserName("Saveoo");
    }

    public void onThirdClick(View view) {
        user.setUserName("Third");
    }

    public void onFourthClick(View view, User user) {
        user.setUserName("Fourth");
    }
}
