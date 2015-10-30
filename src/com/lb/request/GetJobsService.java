package com.lb.request;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface GetJobsService {
	@GET("{token}/getJobList")
	@Headers("Accept-Encoding:application/json")
	Call<String> getJobs(@Path("token") String token);
}
