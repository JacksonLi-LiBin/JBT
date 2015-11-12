package com.lb.request;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GetJobDetailsService {
	@GET("{token}/getJobDetail")
	Call<String> getJobDetails(@Path("token") String token,
			@Query("jobId") String jobId);
}
