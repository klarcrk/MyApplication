package app.witness.com.myapplication.main;


import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import app.witness.com.myapplication.addview.ShopBagFloatView;

/**
 * Created on 2016/7/15 17:36.
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
public class BaseActivity extends AppCompatActivity {


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        afterViewSet();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        afterViewSet();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        afterViewSet();
    }

    private void afterViewSet() {
        ShopBagFloatView.showShopBagFloatView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

