package com.lb.request;

import retrofit.Retrofit;

import com.lb.tools.ReadProperties;

public class GetAreaDomainFiltersClient {
	private static GetAreaDomainFiltersService areaDomainFiltersService;

	public static GetAreaDomainFiltersService getAreaDomainFiltersService() {
		if (areaDomainFiltersService == null) {
			Retrofit retrofit = new Retrofit.Builder()
					.client(GetOkHttpClient.getOkHttpClient())
					.baseUrl(
							ReadProperties.read("url",
									"jackson_job_getjobsbyfilter"))
					.addConverterFactory(new ToStringConverterFactory())
					.build();
			areaDomainFiltersService = retrofit
					.create(GetAreaDomainFiltersService.class);
		}
		return areaDomainFiltersService;
	}
}
