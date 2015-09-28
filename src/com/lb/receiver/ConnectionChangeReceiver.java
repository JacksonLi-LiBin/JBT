package com.lb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lb.constants.MobileNetStatus;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetworkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetworkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!mobileNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
			MobileNetStatus.isNetUsable = false;
		} else {
			MobileNetStatus.isNetUsable = true;
		}
	}

}
