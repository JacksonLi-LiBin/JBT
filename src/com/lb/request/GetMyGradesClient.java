package com.lb.request;

import com.lb.tools.ReadProperties;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class GetMyGradesClient {
	private static GetMyGradesService getMyGradesService;

	public static GetMyGradesService getGetMyGradesService() {
		if (getMyGradesService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(
							ReadProperties.read("url",
									"jackson_grades_getgrades"))
					.addConverterFactory(GsonConverterFactory.create()).build();
			getMyGradesService = retrofit.create(GetMyGradesService.class);
		}
		return getMyGradesService;
	}
}
