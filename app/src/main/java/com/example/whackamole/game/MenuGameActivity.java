package com.example.whackamole.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.models.Game;
import com.example.whackamole.models.User;
import com.example.whackamole.services.UserServices;

import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuGameActivity extends AppCompatActivity {

    private final static Logger LOGGER = Logger.getLogger(MenuGameActivity.class.getName());

    private static final int REQUEST_CODE = 0;

    String nickname;
    Intent intent;
    User user= new User();
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    UserServices userServices = retrofit.create(UserServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game);

        if(getIntent().getExtras() != null) {
            nickname = getIntent().getStringExtra("nickname");

            Call<User> call = userServices.doGetUserNickname(nickname);

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    System.out.println(call.request().url().toString());
                    Response<User> response = call.execute();
                    if(response.isSuccessful()){
                        user = response.body();
                        LOGGER.info(user.toString());
                    }else{
                        LOGGER.info(response.errorBody().string());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                LOGGER.info(" case R.id.button_start:");
                intent = new Intent(getApplicationContext(), StartGameActivity.class);
                intent.putExtra("GameType", "individual");
                intent.putExtra("User", user);
                startActivity(intent);
                break;
            case R.id.button_multiplayer:
                LOGGER.info(" R.id.multiplayer_match");
                intent = new Intent(getApplicationContext(), StartGameActivity.class);
                intent.putExtra("GameType", "multiplayer");
                intent.putExtra("User", user);
                startActivity(intent);
                break;
            case R.id.button_multiplayer_inv:
                LOGGER.info(" R.id.button_multiplayer_inv");
                intent = new Intent(getApplicationContext(), MultiplayerActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
                break;

            case R.id.button_scores:
                LOGGER.info("case R.id.button_scores:");
                intent = new Intent(getApplicationContext(), ScoresActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
                break;
            case R.id.button_options:
                LOGGER.info("case R.id.button_options:");
                intent = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent);
                break;
        }
    }






}