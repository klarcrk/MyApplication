/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import app.witness.com.myapplication.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressbarDemo extends AppCompatActivity {

    @BindView(R.id.progressbar)
    FlikerProgressbar progressbar;
    @BindView(R.id.button7)
    Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar_demo);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button7)
    public void onClick() {
        downLoad();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressbar.setProgress(msg.arg1);
            if (msg.arg1 == 100) {
                progressbar.finishLoad();
            }
        }
    };

    private void downLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(200);
                        Message message = handler.obtainMessage();
                        message.arg1 = i + 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
