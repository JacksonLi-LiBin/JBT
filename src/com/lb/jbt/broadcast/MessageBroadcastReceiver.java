package com.lb.jbt.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageBroadcastReceiver extends BroadcastReceiver {
	private NotificationManager nm = null;
	private Notification notification = null;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if ("com.lb.jbt.broadcast.MSG_Broadcast".equals(arg1.getAction())) {
			String msg = arg1.getStringExtra("msg");
			System.out.println("MessageBroadcastReceiver=====================================>" + msg);
		}

	}

}
