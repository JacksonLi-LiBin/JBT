package com.lb.request;

import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class GetJobsClient {
	private static GetJobsService getJobsService;

	public static GetJobsService getGetJobsService() {
		if (getJobsService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(ReadProperties.read("url", "jackson_job_getjobs"))
					.addConverterFactory(new ToStringConverterFactory())
					.build();
			getJobsService = retrofit.create(GetJobsService.class);
		}
		return getJobsService;
	}
}
