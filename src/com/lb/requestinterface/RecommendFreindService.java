package com.lb.requestinterface;

import com.lb.entities.FriendPassStore;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

public interface RecommendFreindService {
	@POST("{token}/add")
	Call<String> recommendFriend(@Path("token") String token, @Body FriendPassStore friend);
}
