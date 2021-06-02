package com.example.whackamole.services;

import com.example.whackamole.APIClient;
import com.example.whackamole.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServices {
    String API_ROUTE = "/api/game/users/";

    @GET(API_ROUTE+"nickname/{nickname}")
    Call<User> doGetUserNickname(@Path("nickname") String nickname);

    @GET(API_ROUTE)
    Call<List<User>> getUsers();

    @POST(API_ROUTE)
    Call<User> doPostUser(@Body User user);

}

