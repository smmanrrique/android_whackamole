package com.example.whackamole.game;

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

import androidx.appcompat.app.AppCompatActivity;
import com.example.whackamole.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class StartGameActivity extends AppCompatActivity {

    // Relatively static initial declarations
    int varRandMole;
    private TextView mTimeView;
    private TextView mScoreView;
    public int varScore = 0;
    private int varLives = 5;
    final Handler handler = new Handler();
    public boolean varClose = false;

    // This is our game length (seconds as a function of millis)
    private int maxTime = 60 * 1000;
    // Leaving redundant calc which enables seconds to be set using some logic/prefs
    private long stepTime = 1 * 1000;

    // This is our delay per mole popping up (difficulty)
    public int timeInterval = 1000;
    // This is the time a mole spends above ground (difficulty)
    public int moleUpTime = 350;

    // Main game countdown
    public CountDownTimer mTimer = new myTimer(maxTime, stepTime);

    public MediaPlayer mPlayerWhack;
    public MediaPlayer mPlayerMiss;

    public String currentDiff;

    public ImageView molesClick [] = new ImageView [9];

    public int yValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        mTimeView = (TextView) findViewById(R.id.textTimeVal);
        mScoreView = (TextView) findViewById(R.id.textScoreVal);

        // Get saved difficulty, default to Medium if no pref exists
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        currentDiff = sharedPref.getString("saved_difficulty", "Medium");

        // Start the game!
        mTimer.start();
        handler.post(moleLoop);

        varClose = false;

        mPlayerWhack = MediaPlayer.create(getApplicationContext(), R.raw.whack);
        mPlayerMiss = MediaPlayer.create(getApplicationContext(), R.raw.miss);

        molesClick [0] = (ImageView) findViewById(R.id.imageMole1);
        molesClick [1] = (ImageView) findViewById(R.id.imageMole2);
        molesClick [2] = (ImageView) findViewById(R.id.imageMole3);
        molesClick [3] = (ImageView) findViewById(R.id.imageMole4);
        molesClick [4] = (ImageView) findViewById(R.id.imageMole5);
        molesClick [5] = (ImageView) findViewById(R.id.imageMole6);
        molesClick [6] = (ImageView) findViewById(R.id.imageMole7);
        molesClick [7] = (ImageView) findViewById(R.id.imageMole8);
        molesClick [8] = (ImageView) findViewById(R.id.imageMole9);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Scale mole translation based on device dimensions
        int sHeight = metrics.heightPixels;
        yValue = (sHeight/12)*-1;

    }

    /**
     * Some android specific class methods to better handle some back-behaviour
     * such as stopping game sounds and the async timer when user navigates away from the main game.
     * Using a boolean to determine tell the logic whether activity is running or not (I can't
     * believe there isn't a better way of doing this).
     */

    @Override
    public void onPause() {
        super.onPause();

        varClose = true;
        mTimer.cancel();

        mPlayerWhack.stop();
        mPlayerMiss.stop();

    }

    @Override
    public void onStop() {
        super.onStop();

        varClose = true;
        mTimer.cancel();

        mPlayerWhack.stop();
        mPlayerMiss.stop();

    }

    @Override
    public void onResume(){
        super.onResume();

        varClose = false;

    }

    // Public timer class which is handling the game clock
    public class myTimer extends CountDownTimer {
        public myTimer(int maxTime, long stepTime) {
            super(maxTime, stepTime);

        }
        @Override

        // Called when the timer finishes
        public void onFinish() {

            // Call endgame class and pass score, reason (due to time out)
            this.cancel();
            String messageTime = getString(R.string.str_end_time);
            EndGame(varScore, messageTime);

            // Reset difficulty vars
            timeInterval = 1000;
            moleUpTime = 350;

        }

        // Ticker called every x millis until done
        public void onTick(long millisUntilFinished) {

            // Using to set the time value every second (1000ms)
            mTimeView.setText(String.valueOf(millisUntilFinished / 1000));

            // Ramp the difficulty up every 5 seconds
            if (((millisUntilFinished/1000)%5 == 0) && (millisUntilFinished/1000) != 60){
                increaseDifficulty();
            }

        }
    }

    // Functions to incrementally increase difficulty
    public void increaseDifficulty(){

        String diff1 = getString(R.string.diff1);
        String diff3 = getString(R.string.diff3);

        // When difficulty increase is called, decrease time between moles, and surface time by
        // an amount based on the current difficulty
        if (currentDiff.equals(diff1)){
            timeInterval *= 0.99;
            moleUpTime *= 0.99;
        } else if (currentDiff.equals(diff3)) {
            timeInterval *= 0.90;
            moleUpTime *= 0.90;
        } else {
            timeInterval *= 0.95;
            moleUpTime *= 0.95;
        }

    }

    // End the game! Extras passed are our final score, and the reason the game is over
    public void EndGame(int EndScore, String Reason) {

        Intent intent = new Intent(getApplicationContext(), EndActivity.class);
        intent.putExtra("score", EndScore);
        intent.putExtra("reason", Reason);

        mTimer.cancel();
        startActivity(intent);
        this.finish();

    }

    // Game loop is a runnable which calls itself every timeInterval (millis)
    public Runnable moleLoop = new Runnable() {

        int varPrevRandMole = 10;

        @Override
        public void run () {

            // Pick a mole at random, if you get the same twice, re-roll until it's different
            varRandMole = new Random().nextInt(8);

            if (varRandMole == varPrevRandMole){
                do
                    varRandMole = new Random().nextInt(8);
                while (varRandMole == varPrevRandMole);
            }

            varPrevRandMole = varRandMole;

            // Pop the mole up
            molesClick[varRandMole].animate().translationY(yValue).setDuration(moleUpTime);

            // Timer to pop our mole back down if player fails to hit it
            new Timer().schedule(new TimerTask() {
                public void run() {

                    if (!varClose) {

                        for (int i = 0; i < 9; i++) {
                            if (molesClick[i].getTranslationY() == yValue) {

                                final int j = i;

                                // Sets the mole back to its beginning position
                                // run this update on the UI thread as we need a "looper" thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        molesClick[j].animate().translationY(0).setDuration(5);
                                    }
                                });

                                if (mPlayerMiss.isPlaying() && mPlayerMiss != null) {
                                    mPlayerMiss.stop();
                                    mPlayerMiss.reset();
                                    mPlayerMiss.release();
                                }
                                mPlayerMiss.start();

                                // Deduct a life if we miss a mole
                                varLives -= 1;
                                updateLives(varLives);

                            }
                        }
                    }
                }
            }, timeInterval);

            if (!varClose) {
                handler.postDelayed(moleLoop, timeInterval);
            }
        }
    };

    // Handling our life indicators
    public void updateLives(int Lives){

        final ImageView heart1= (ImageView) findViewById(R.id.imageHeart1);
        final ImageView heart2= (ImageView) findViewById(R.id.imageHeart2);
        final ImageView heart3= (ImageView) findViewById(R.id.imageHeart3);
        final ImageView heart4= (ImageView) findViewById(R.id.imageHeart4);
        final ImageView heart5= (ImageView) findViewById(R.id.imageHeart5);

        // Start taking off lives, when none are left, call our game end method
        if (Lives == 4){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heart5 != null){
                        heart5.setImageResource(R.drawable.placeholder_heart_empty);
                    }
                }
            });
        } else if (Lives == 3){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heart4 != null) {
                        heart4.setImageResource(R.drawable.placeholder_heart_empty);
                    }
                }
            });
        } else if (Lives == 2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heart3 != null){
                        heart3.setImageResource(R.drawable.placeholder_heart_empty);
                    }
                }
            });
        } else if (Lives == 1){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heart2 != null) {
                        heart2.setImageResource(R.drawable.placeholder_heart_empty);
                    }
                }
            });
        } else if (Lives == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heart1 != null) {
                        heart1.setImageResource(R.drawable.placeholder_heart_empty);
                    }
                }
            });
            String messageLives = getString(R.string.str_end_lives);
            if (!varClose) {
                EndGame(varScore, messageLives);
            }
        }

    }

    // Updates score text field
    public void updateScore(int Score){
        mScoreView.setText(String.valueOf(Score));
    }

    // OnClick function for mole objects when we hit them
    public void onClick(View v) {

        // Show hit-reg for testing

        //         Toast.makeText(v.getContext(),
        //                "Hit Registered",
        //                Toast.LENGTH_LONG).show();

        // Switch statement to find the right mole and pop him down
        switch(v.getId()) {
            case R.id.imageMole1:
                if (molesClick[0].getTranslationY() < 0) {
                    molesClick[0].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole2:
                if (molesClick[1].getTranslationY() < 0) {
                    molesClick[1].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole3:
                if (molesClick[2].getTranslationY() < 0) {
                    molesClick[2].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole4:
                if (molesClick[3].getTranslationY() < 0) {
                    molesClick[3].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole5:
                if (molesClick[4].getTranslationY() < 0) {
                    molesClick[4].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole6:
                if (molesClick[5].getTranslationY() < 0) {
                    molesClick[5].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole7:
                if (molesClick[6].getTranslationY() < 0) {
                    molesClick[6].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole8:
                if (molesClick[7].getTranslationY() < 0) {
                    molesClick[7].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
            case R.id.imageMole9:
                if (molesClick[8].getTranslationY() < 0) {
                    molesClick[8].animate().translationY(0).setDuration(20);
                    directHit();
                }
                break;
        }
    }

    // When mole is hit, play sound and update score
    public void directHit(){

        if (mPlayerWhack != null && mPlayerWhack.isPlaying()){
            mPlayerWhack.stop();
            mPlayerWhack.reset();
            mPlayerWhack.release();
        }

        mPlayerWhack = MediaPlayer.create(getApplicationContext(), R.raw.whack);
        mPlayerWhack.start();

        // Award points, update score
        varScore += 250;
        updateScore(varScore);
    }
}










