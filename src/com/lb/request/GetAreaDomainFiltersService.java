package com.lb.request;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface GetAreaDomainFiltersService {
	@GET("{token}/getDomainAndArea")
	@Headers("Accept-Encoding:application/json")
	Call<String> getDomainAndArea(@Path("token") String token);
}
