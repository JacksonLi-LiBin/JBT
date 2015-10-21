package com.lb.requestinterface;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class RecommendFriendInterface {
	private static RecommendFreindService recommendFreindService;

	public static RecommendFreindService getRecommendFriendClient() {
		if (recommendFreindService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(
							ReadProperties.read("url",
									"jackson_recommend_local"))
					.addConverterFactory(GsonConverterFactory.create()).build();
			recommendFreindService = retrofit
					.create(RecommendFreindService.class);
		}
		return recommendFreindService;
	}
}