package com.lb.request;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface CommonMethodsService {
	@GET("CommonMethods/sendEmail")
	Call<String> forgetPassword(@Query("userId") String userId, @Query("email") String email);
}
