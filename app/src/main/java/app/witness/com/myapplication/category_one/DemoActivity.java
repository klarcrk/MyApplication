package app.witness.com.myapplication.category_one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import app.witness.com.myapplication.R;
import app.witness.com.myapplication.dagger.Poetry;
import app.witness.com.myapplication.main.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 2016/7/15 17:35.
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
public class DemoActivity extends BaseActivity {
    @BindView(R.id.btnMakeCoffee)
    Button btnMakeCoffee;
    @BindView(R.id.tvCoffee)
    TextView tvCoffee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.sample_coffee_make);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnMakeCoffee)
    public void onClick() {
        Poetry poetry = new Poetry("life is so dili");
        Poetry poetry2 = new Poetry("what is life");
        Poetry poetry3 = new Poetry("just look");
        Poetry poetry4 = new Poetry("that's good");
        Map<String, Poetry> datas = new HashMap<>();
        datas.put("poetry", poetry);
        datas.put("poetry2", poetry2);
        datas.put("poetry3", poetry3);
        datas.put("poetry4", poetry4);
        Gson gson = new Gson();
        String result = gson.toJson(datas);
        tvCoffee.append(result);
        tvCoffee.append("\n");
        Map<String, Poetry> reverse = gson.fromJson(result, new TypeToken<Map<String, Poetry>>() {
        }.getType());
        tvCoffee.append(reverse.toString());
        tvCoffee.append("\n");
    }
}
