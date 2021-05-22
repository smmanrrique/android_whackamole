package com.example.whackamole.services;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserServices {

    String API_ROUTE = "/users";

    @GET(API_ROUTE)
    Call<List> getPosts();
}

