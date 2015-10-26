package com.lb.request;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GetMyGradesService {
	@GET("{token}/getGradesByUser")
	@Headers("Accept-Encoding:application/json")
	Call<String> getMyGrades(@Path("token") String token,
			@Query("userId") String userId,
			@Query("courseNumber") String courseNumber,
			@Query("cycleNumber") String cycleNumber);
}
