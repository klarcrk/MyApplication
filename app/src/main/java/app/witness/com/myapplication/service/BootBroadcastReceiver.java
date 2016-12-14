/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.witness.com.myapplication.main.MainActivity;

/**
 * Created by apple on 15/12/11.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";
   
    @SuppressWarnings("static-access")
	@Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
        	Intent StartIntent = new Intent(context, MainActivity.class); //接收到广播后，跳转到MainActivity
            StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(StartIntent);
        }

    }
   
}
