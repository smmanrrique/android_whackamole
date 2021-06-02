package com.example.whackamole.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.models.User;
import com.example.whackamole.services.UserServices;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuGameActivity extends AppCompatActivity {

    Intent intent;
    User user;
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    UserServices userServices = retrofit.create(UserServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game);

        Intent recibir = getIntent();
        String nickname = recibir.getStringExtra("nickname");
        System.out.println("RECIBIDO NICKNAME= "+nickname);
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
                }else{
                    System.out.println(response.errorBody());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                System.out.println(" case R.id.button_start:");
                intent = new Intent(getApplicationContext(), StartGameActivity.class);
                startActivity(intent);
                break;
            case R.id.button_scores:
                System.out.println("case R.id.button_scores:");
                intent = new Intent(getApplicationContext(), ScoresActivity.class);
                startActivity(intent);
                break;
            case R.id.button_options:
                System.out.println("case R.id.button_options:");
                intent = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent);
                break;
        }
    }




}