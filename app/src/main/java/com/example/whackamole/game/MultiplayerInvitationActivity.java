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
import com.example.whackamole.models.User;
import com.example.whackamole.services.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MultiplayerInvitationActivity extends AppCompatActivity implements ReciclerViewActivity.ItemClickListener {

    private final static Logger LOGGER = Logger.getLogger(MultiplayerNewActivity.class.getName());

    User user;
    Intent intent;
    List<Game> games;
    Game game;
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    GameService gameService = retrofit.create(GameService .class);
    ReciclerViewActivity adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        if(getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("User");
            LOGGER.info(user.toString());

            // Get match
            Call<List<Game>> call = gameService.getMultiplayerGames(user.getNickname());

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    Response<List<Game>> response = call.execute();
                    if(response.isSuccessful()){
                        games = response.body();

                        // data to populate the RecyclerView with
                        ArrayList<String> games_show = new ArrayList<>();
                        for (Game g : games) {
                            games_show.add("Invitacion:"+g.getWinner()+" score: "+g.getScore());
                        }

                        // set up the RecyclerView
                        RecyclerView recyclerView = findViewById(R.id.multiplayer_match);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        adapter = new ReciclerViewActivity(this, games_show);
                        adapter.setClickListener(this);
                        recyclerView.setAdapter(adapter);

                    }
                } catch (Exception ex) {
                    Intent intent = new Intent();
                    intent.putExtra("User", user);
                    this.finish();
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

        game = games.get(position);
        game.setStatus(0);
        LOGGER.info(game.toString());
        intent = new Intent(getApplicationContext(), StartGameActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("Game", game);
        toast.cancel();
        startActivity(intent);
        this.finish();
    }



}