package com.lb.request;

import com.lb.tools.ReadProperties;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

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
