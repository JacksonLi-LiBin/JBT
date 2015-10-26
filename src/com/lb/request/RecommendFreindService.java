package com.lb.request;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

import com.lb.entities.FriendPassStore;

public interface RecommendFreindService {
	@POST("{token}/add")
	Call<String> recommendFriend(@Path("token") String token, @Body FriendPassStore friend);
}
