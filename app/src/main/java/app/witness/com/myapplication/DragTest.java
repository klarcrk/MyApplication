package app.witness.com.myapplication;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextClock;

import app.witness.com.myapplication.main.BaseActivity;

/**
 * Created on 2016/7/18 17:57.
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
public class DragTest extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drag_test);
        TextClock textClock = (TextClock) findViewById(R.id.textClock);
        textClock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("dot", "Dot : " + v.toString());
                v.startDrag(data, new View.DragShadowBuilder(v), v, 0);
                return false;
            }
        });
        View dragView = findViewById(R.id.drag_view);
        dragView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("dot", "Dot : " + v.toString());
                v.startDrag(data, new View.DragShadowBuilder(v), v, 0);
                return false;
            }
        });
    }
}
