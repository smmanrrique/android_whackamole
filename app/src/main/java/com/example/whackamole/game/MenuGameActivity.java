package com.example.whackamole.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.R;

public class MenuGameActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game);
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