package com.lb.requestinterface;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

import com.lb.entities.FriendPassStore;

public interface RecommendFreindService {
	@Headers({ "Accept: application/json",
			"Content-Type:application/x-www-form-urlencoded" })
	@POST("/{token}/add")
	Call<String> recommendFriend(@Path("token") String token,
			@Body FriendPassStore friend);
}
