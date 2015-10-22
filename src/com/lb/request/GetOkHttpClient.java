package com.lb.request;

import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.OkHttpClient;

public class GetOkHttpClient {
	private static OkHttpClient okHttpClient;

	public static OkHttpClient getOkHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient();
			okHttpClient.setConnectTimeout(5, TimeUnit.MINUTES);
			okHttpClient.setReadTimeout(5, TimeUnit.MINUTES);
		}
		return okHttpClient;
	}
}
