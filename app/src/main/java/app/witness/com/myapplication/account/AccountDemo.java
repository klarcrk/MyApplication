package app.witness.com.myapplication.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import app.witness.com.myapplication.main.BaseActivity;

/**
 * Created on 2016/7/18 15:11.
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
public class AccountDemo extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (Account account : accounts) {
                am.removeAccount(account, AccountDemo.this, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {

                    }
                },null);
            }
        }
        TextView texti = new TextView(this);
        texti.setText(accounts.length);
    }
}
