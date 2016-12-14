/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package app.witness.com.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


/**
 * Created by coco on 2016/6/30.
 */
public class UpdateRecevier extends BroadcastReceiver {
	// private Intent mService;
    public IService mBinder;
    private MyServiceConnection conn;
    @SuppressWarnings("static-access")
	@Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("cmd_update").toString().equals("true")){
        	conn = new MyServiceConnection();
            context.bindService(intent, conn, context.BIND_AUTO_CREATE);
        }

    }
    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // sTODO Auto-generated method stub
            // 3,�󶨳ɹ�,�ڴ˴��Ϳ����÷����еķ���
           mBinder = (IService) service;
            mBinder.callMethodInService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

    }

}
