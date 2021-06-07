package com.example.whackamole.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.game.MenuGameActivity;
import com.example.whackamole.models.Loging;
import com.example.whackamole.models.User;
import com.example.whackamole.services.APIClientInterface;
import com.example.whackamole.services.LogingService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogingActivity extends AppCompatActivity {

    Loging loging;
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    LogingService logingService = retrofit.create(LogingService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
    }

    public void acceptLoging(View view) {
        System.out.println("acceptLoging(\"msm\", message);");
        EditText nickname = (EditText) findViewById(R.id.login_nickname);
        EditText password = (EditText) findViewById(R.id.login_password);

        loging = new Loging(nickname.getText().toString(),
                            password.getText().toString());

        Call<Loging> call = logingService.doGetLoging(loging);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                System.out.println(call.request().url().toString());
                Response<Loging> response = call.execute();
                System.out.println(response.isSuccessful());
                if(response.isSuccessful()){
                    // Start game
                    Intent intent = new Intent(this, MenuGameActivity.class);
                    intent.putExtra("nickname", loging.getNickname());
                    startActivity(intent);
                    finish();
                }else{
                    System.out.println(response.errorBody());

                    // display message
                    Toast.makeText(view.getContext(),
                            "WRONG CREDENTIALS",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}