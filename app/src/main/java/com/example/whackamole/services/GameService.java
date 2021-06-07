package com.example.whackamole.services;

import com.example.whackamole.models.Game;
import com.example.whackamole.models.Score;
import com.example.whackamole.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GameService {
    String API_ROUTE = "/api/game/match/";

    @GET(API_ROUTE+"scores")
    Call<List<Score>> doGetScores();

    @GET(API_ROUTE)
    Call<List<Game>> getGames();

    @GET(API_ROUTE+"multiplayer/{nickname}")
    Call<List<Game>> getMultiplayerGames(@Path("nickname") String nickname);

    @POST(API_ROUTE)
    Call<Game> doPostGame(@Body Game game);

    @PUT(API_ROUTE+"{id}")
    Call<Game> doPutGame(@Path("id") Integer id, @Body Game game);

}
