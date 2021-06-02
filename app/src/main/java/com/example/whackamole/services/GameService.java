package com.example.whackamole.services;

import com.example.whackamole.models.Game;
import com.example.whackamole.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GameService {
    String API_ROUTE = "/api/game/match/";

    @GET(API_ROUTE+"scores")
    Call<Game> doGetScores();

    @GET(API_ROUTE)
    Call<List<Game>> getGames();

    @POST(API_ROUTE)
    Call<Game> doPostGame(@Body Game game);

}
