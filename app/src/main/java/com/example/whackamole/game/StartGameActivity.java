package com.example.whackamole.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.whackamole.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class StartGameActivity extends AppCompatActivity {

    private final static Logger LOGGER = Logger.getLogger(StartGameActivity.class.getName());

    // Declaring TextView
    public TextView showTime;
    public TextView showScore;
    public TextView showLive;

    // Variables to music game
    public MediaPlayer mPlayerWhack;
    public MediaPlayer mPlayerMiss;

    // Game Time in millis
    static final int MILLIS_TIME = 1000;
    static final int GOPHER_WAIT_TIME = 300;
    static final int TRANSLATION_DURATION = 30;
    public int maxTime = 60 * MILLIS_TIME;
    public long stepTime = 1 * MILLIS_TIME;

    // Relatively static initial declarations
    public int numHole;
    public int gameScore = 0;
    public int gameLive = 5;
    public boolean flagEndGame = false;
    final Handler handler = new Handler();

    // This is our delay per mole popping up (difficulty)
    public int timeInterval = MILLIS_TIME;
    public int gopherWaitTime = GOPHER_WAIT_TIME;

    public CountDownTimer matchTimer = new gameTimer(maxTime, stepTime); // countdown function game

    public String gameLevel;
    public ImageView[] gopherHole;

    public int yPixelCoordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        // Get game difficulty Level
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gameLevel = sharedPref.getString("difficulty_level", "Easy");

        showTime = (TextView) findViewById(R.id.textShowTime);
        showScore = (TextView) findViewById(R.id.textShowScore);
        showLive = (TextView) findViewById(R.id.textShowLive);
        showScore.setText(String.valueOf(gameScore));
        showLive.setText(String.valueOf(gameLive));


        // Start the game
        matchTimer.start();
        handler.post(gameLoop);

        flagEndGame = false;
        mPlayerWhack = MediaPlayer.create(getApplicationContext(), R.raw.hitted);
        mPlayerMiss = MediaPlayer.create(getApplicationContext(), R.raw.missed);

        gopherHole  = setImages(); // Start images

        // Getting  Screen metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        yPixelCoordinate = ((int) metrics.heightPixels/12)*-1;

    }

    public ImageView[] setImages(){
        // returning  ImageView array
        return new ImageView[] {findViewById(R.id.imageMole1),findViewById(R.id.imageMole2),
                findViewById(R.id.imageMole3), findViewById(R.id.imageMole4),
                findViewById(R.id.imageMole5),findViewById(R.id.imageMole6),
                findViewById(R.id.imageMole7),findViewById(R.id.imageMole8),
                findViewById(R.id.imageMole9)};
    }

    @Override
    public void onPause() {
        super.onPause();

        flagEndGame = true;
        matchTimer.cancel();

        mPlayerWhack.stop();
        mPlayerMiss.stop();

    }

    public class gameTimer extends CountDownTimer { // game clock
        public gameTimer(int maxTime, long stepTime) { super(maxTime, stepTime);}

        @Override
        public void onFinish() {
            this.cancel();
            EndGame(gameScore, getString(R.string.str_end_time));

        }

        public void onTick(long millisUntilFinished) {
            int currentTime = (int) (millisUntilFinished / MILLIS_TIME);
            showTime.setText(String.valueOf(currentTime)); // update gameTime

            if ((currentTime%5 == 0) &&  (currentTime != 60)){
                switch(gameLevel) {
                    case "Easy":
                        timeInterval *= 0.99;
                        gopherWaitTime *= 0.99;
                    case "Medium":
                        timeInterval *= 0.95;
                        gopherWaitTime *= 0.95;
                    case "Hard":
                        timeInterval *= 0.90;
                        gopherWaitTime *= 0.90;
                }
            }

        }
    }



    public void EndGame(int EndScore, String Reason) {     // Push message End the game!
        System.out.println("void EndGame(int EndScore, String Reason)");
        System.out.println("void EndGame(int EndScore, String Reason)");
//        MessageGameActivity msmPush = new MessageGameActivity();
//        msmPush.show();
        Intent intent = new Intent(getApplicationContext(), MenuGameActivity.class);
//        intent.putExtra("score", EndScore);
//        intent.putExtra("reason", Reason);
        matchTimer.cancel();
        startActivity(intent);
        this.finish();

    }

    // Game loop is a runnable which calls itself every timeInterval (millis)
    public Runnable gameLoop = new Runnable() {

        @Override
        public void run () {
            numHole = new Random().nextInt(8);
            LOGGER.info("Select new hole ---> "+ String.valueOf(numHole));

            // Gopher up
            gopherHole[numHole].animate().translationY(yPixelCoordinate).setDuration(gopherWaitTime);

            // Hide gopher
            new Timer().schedule(new TimerTask() {
                public void run() {
                    if (!flagEndGame) {
                        for (int i = 0; i < 9; i++) {
                            if (gopherHole[i].getTranslationY() == yPixelCoordinate) {
                                final int j = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gopherHole[j].animate().translationY(0).setDuration(5);
                                    }
                                });
                                mPlayerMiss.start();
                            }
                        }
                    }
                }
            }, timeInterval);


            updateLives(); // Update lives

            if (!flagEndGame) {
                handler.postDelayed(gameLoop, timeInterval);
            }
        }
    };

    public void updateLives(){
        gameLive -= 1;
        if(gameLive > 0){
            showLive.setText(String.valueOf(gameLive));
        }else{
            EndGame(gameScore, getString(R.string.str_end_lives));
        }
    }

    public void onClick(View v) { //  hit gopher
        // display message
         Toast.makeText(v.getContext(),
                "Hit Registered",
                Toast.LENGTH_LONG).show();

        switch(v.getId()) {
            case R.id.imageMole1:
                if (gopherHole[0].getTranslationY() < 0) {
                    gopherHole[0].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole2:
                if (gopherHole[1].getTranslationY() < 0) {
                    gopherHole[1].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole3:
                if (gopherHole[2].getTranslationY() < 0) {
                    gopherHole[2].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole4:
                if (gopherHole[3].getTranslationY() < 0) {
                    gopherHole[3].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole5:
                if (gopherHole[4].getTranslationY() < 0) {
                    gopherHole[4].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole6:
                if (gopherHole[5].getTranslationY() < 0) {
                    gopherHole[5].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole7:
                if (gopherHole[6].getTranslationY() < 0) {
                    gopherHole[6].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole8:
                if (gopherHole[7].getTranslationY() < 0) {
                    gopherHole[7].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
            case R.id.imageMole9:
                if (gopherHole[8].getTranslationY() < 0) {
                    gopherHole[8].animate().translationY(0).setDuration(TRANSLATION_DURATION);
                    directHit();
                }
                break;
        }
    }

    // When mole is hit, play sound and update score
    public void directHit(){
        gameMusic(mPlayerWhack);
        gameScore += 100;
        showScore.setText(String.valueOf(gameScore));
    }

    public void gameMusic(MediaPlayer music){
        if (music != null && music.isPlaying()){
            music.stop();
            music.reset();
            music.release();
        }
        music.start();
    }




}










