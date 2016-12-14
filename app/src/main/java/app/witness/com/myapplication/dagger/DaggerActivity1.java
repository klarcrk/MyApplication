package app.witness.com.myapplication.dagger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import javax.inject.Inject;

import app.witness.com.myapplication.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2016/7/19 11:09.
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
public class DaggerActivity1 extends Activity implements View.OnClickListener {

    @BindView(R.id.btnMakeCoffee)
    Button btnMakeCoffee;
    @BindView(R.id.tvCoffee)
    TextView tvCoffee;

    @Inject
    Poetry poetry;
    @Inject
    Gson mJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
        btnMakeCoffee.setOnClickListener(this);
        PoeComponent.getInstance().inject(this);
    }

    @Override
    public void onClick(View v) {
        String json = mJson.toJson(poetry);
        String text = json + ",mPoetry:" + poetry;
        tvCoffee.setText(text);
    }
}
