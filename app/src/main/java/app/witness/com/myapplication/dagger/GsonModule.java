package app.witness.com.myapplication.dagger;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 2016/7/19 13:49.
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
@Module
public class GsonModule {

    @Provides
    public Gson provideGson() {
        return new Gson();
    }
}
