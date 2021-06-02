package com.example.whackamole.services;

import com.example.whackamole.models.Loging;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LogingService {
    String API_ROUTE = "/api/game/users/loging";

    @POST(API_ROUTE)
    Call<Loging> doGetLoging(@Body Loging loging);
}
