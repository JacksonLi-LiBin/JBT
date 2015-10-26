package com.lb.request;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class CommonMethodsClient {
	private static CommonMethodsService commonMethodsService;

	public static CommonMethodsService getCommonMethodsService() {
		if (commonMethodsService == null) {
			Retrofit retrofit = new Retrofit.Builder().client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(ReadProperties.read("url", "jackson_ad_login_cp"))
					.addConverterFactory(GsonConverterFactory.create()).build();
			commonMethodsService = retrofit.create(CommonMethodsService.class);
		}
		return commonMethodsService;
	}
}
