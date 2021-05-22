package com.example.whackamole.game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.R;

import java.util.logging.Logger;

public class OptionsActivity extends AppCompatActivity {

    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;

    private final static Logger LOGGER = Logger.getLogger(StartGameActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedPref = this.getPreferences(this.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


    public void onClick(View view) {
        System.out.println(" case R.id.button_start:");
        switch (view.getId()) {
            case R.id.button_level1:
                setSms(String.valueOf(R.string.level1));
                break;
            case R.id.button_level2:
                setSms(String.valueOf(R.string.level2));
                break;
            case R.id.button_level3:
                setSms(String.valueOf(R.string.level3));
                break;
        }


    }

    public void setSms(String sms) {
        System.out.println(" case R.id.button_start:");
        editor.putString("saved_difficulty",sms);
        editor.apply();
        Toast.makeText(OptionsActivity.this, sms, Toast.LENGTH_SHORT).show();

    }
}
