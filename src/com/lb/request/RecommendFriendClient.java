package com.lb.request;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class RecommendFriendClient {
	private static RecommendFreindService recommendFreindService;

	public static RecommendFreindService getRecommendFriendClient() {
		if (recommendFreindService == null) {
			Retrofit retrofit = new Retrofit.Builder().client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(ReadProperties.read("url", "jackson_recommend_local"))
					.addConverterFactory(GsonConverterFactory.create()).build();
			recommendFreindService = retrofit.create(RecommendFreindService.class);
		}
		return recommendFreindService;
	}
}
