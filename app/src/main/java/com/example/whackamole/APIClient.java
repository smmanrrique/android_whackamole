package com.example.whackamole;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String BASE_URL = "http://192.168.1.81:8888/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}