package com.lb.request;

import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class GetJobDetailsClient {
	private static GetJobDetailsService getJobDetailsService;

	public static GetJobDetailsService getGetJobDetailsService() {
		if (getJobDetailsService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(
							ReadProperties.read("url",
									"jackson_job_detail_getjobdetail"))
					.client(GetOkHttpClient.getOkHttpClient())
					.addConverterFactory(new ToStringConverterFactory())
					.build();
			getJobDetailsService = retrofit.create(GetJobDetailsService.class);
		}
		return getJobDetailsService;
	}
}
