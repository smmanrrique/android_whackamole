package com.example.whackamole.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.R;
import com.example.whackamole.game.MenuGameActivity;
import com.example.whackamole.game.StartGameActivity;
import com.example.whackamole.models.User;

public class SignupActivity extends AppCompatActivity {

    User user = new User();

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

        System.out.println(user.toString());

        //Create user in db


        // Menu game
        Intent intent = new Intent(this, MenuGameActivity.class);
        startActivity(intent);
    }
}
