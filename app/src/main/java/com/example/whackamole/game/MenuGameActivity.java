package com.example.whackamole.game;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.R;

public class MenuGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                System.out.println(" case R.id.button_start:");
                break;
            case R.id.button_scores:
                System.out.println("case R.id.button_scores:");
                break;
            case R.id.button_options:
                System.out.println("case R.id.button_options:");
                break;
        }
    }




}