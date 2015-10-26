package com.lb.request;

import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class GetMyGradesClient {
	private static GetMyGradesService getMyGradesService;

	public static GetMyGradesService getGetMyGradesService() {
		if (getMyGradesService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(
							ReadProperties.read("url",
									"jackson_grades_getgrades"))
					.addConverterFactory(new ToStringConverterFactory())
					.build();
			getMyGradesService = retrofit.create(GetMyGradesService.class);
		}
		return getMyGradesService;
	}
}
