package com.example.whackamole.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.whackamole.R;
import com.example.whackamole.game.MenuGameActivity;
import com.example.whackamole.models.User;

public class LogingActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
    }

    public void acceptLoging(View view) {
        System.out.println("acceptLoging(\"msm\", message);");
        EditText nickname = (EditText) findViewById(R.id.login_nickname);
        EditText password = (EditText) findViewById(R.id.login_password);

        user.setNickname(nickname.getText().toString());
        user.setPassword(password.getText().toString());

        System.out.println(user.toString());

        // Validar User y password

        // Start game
        Intent intent = new Intent(this, MenuGameActivity.class);
        startActivity(intent);
        finish();


    }
}