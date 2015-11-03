package com.lb.request;

import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class GetJobsTitleClient {
	private static GetJobsTitleService getJobsService;

	public static GetJobsTitleService getGetJobsService() {
		if (getJobsService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(ReadProperties.read("url", "jackson_job_getjobs"))
					.addConverterFactory(new ToStringConverterFactory())
					.build();
			getJobsService = retrofit.create(GetJobsTitleService.class);
		}
		return getJobsService;
	}
}
