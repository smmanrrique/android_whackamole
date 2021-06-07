package com.example.whackamole.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.game.MenuGameActivity;
import com.example.whackamole.game.StartGameActivity;
import com.example.whackamole.models.Loging;
import com.example.whackamole.models.User;
import com.example.whackamole.services.UserServices;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupActivity extends AppCompatActivity {

    User user = new User();
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    UserServices userServices = retrofit.create(UserServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void acceptLoging(View view) {
        System.out.println("acceptLoging(\"msm\", message);");
        EditText nickname = (EditText) findViewById(R.id.login_nickname);
        EditText password = (EditText) findViewById(R.id.login_password);
        EditText email = (EditText) findViewById(R.id.login_email);

        user.setEmail(email.getText().toString());
        user.setNickname(nickname.getText().toString());
        user.setPassword(password.getText().toString());

        Call<User> call = userServices.doPostUser(user);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                System.out.println(call.request().url().toString());
                Response<User> response = call.execute();
                System.out.println(response.isSuccessful());
                if(response.isSuccessful()){
                    // Menu game
                    Intent intent = new Intent(this, MenuGameActivity.class);
                    intent.putExtra("nickname", user.getNickname());
                    startActivity(intent);
                    finish();
                }else{
                    System.out.println(response.errorBody());

                    // display message
                    Toast.makeText(view.getContext(),
                            "ERROR",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
