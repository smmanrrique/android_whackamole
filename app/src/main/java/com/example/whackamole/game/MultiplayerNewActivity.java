package com.example.whackamole.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.models.Game;
import com.example.whackamole.models.Score;
import com.example.whackamole.models.User;
import com.example.whackamole.services.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MultiplayerNewActivity extends AppCompatActivity implements ReciclerViewActivity.ItemClickListener {

    private final static Logger LOGGER = Logger.getLogger(MultiplayerNewActivity.class.getName());

    User user;
    Intent intent;
    List<Score> scores;
    Game game;
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    GameService gameService = retrofit.create(GameService .class);
    ReciclerViewActivity adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        LOGGER.info("ESTOY AQUI ---------------------------------??? ");
        if(getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("User");
            LOGGER.info(user.toString());

            /// Get match
            Call<List<Score>> call = gameService.doGetScores();

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    Response<List<Score>> response = call.execute();
                    if(response.isSuccessful()){
                        scores = response.body();

                        if(scores.size() < 1){
                            Intent intent = new Intent();
                            intent.putExtra("User", user);
                            this.finish();

                        }

                        // data to populate the RecyclerView with
                        ArrayList<String> games_show = new ArrayList<>();
                        for (Score s : scores) {
                            if (s.getNickname() != user.getNickname()){
                                games_show.add("Send Request:"+s.getNickname());
                            }
                        }

                        // set up the RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.multiplayer_match);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        adapter = new ReciclerViewActivity(this, games_show);
                        adapter.setClickListener(this);
                        recyclerView.setAdapter(adapter);

                    }else{
                        System.out.println(response.errorBody());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast toast = Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position,
                Toast.LENGTH_SHORT);
        toast.show();
        // Get name user guest
        String guest = scores.get(position).getNickname();
        game = new Game(guest, user.getId());
        game.setStatus(1);
        Call<Game> call = gameService.doPostGame(game);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Response<Game> response = call.execute();
                if(response.isSuccessful()){
                    game = response.body();
                    LOGGER.info(game.toString());
                }
            } catch (Exception ex) {
                Intent intent = new Intent();
                intent.putExtra("User", user);
                this.finish();
                ex.printStackTrace();
            }
        }

        toast.cancel();
        intent = new Intent(getApplicationContext(), StartGameActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("Game", game);
        startActivity(intent);
        this.finish();

    }



}