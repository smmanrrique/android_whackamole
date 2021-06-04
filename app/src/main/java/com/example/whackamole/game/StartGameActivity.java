package com.example.whackamole.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.models.Game;
import com.example.whackamole.models.User;
import com.example.whackamole.services.GameService;
import com.example.whackamole.services.UserServices;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class StartGameActivity extends AppCompatActivity {

    private final static Logger LOGGER = Logger.getLogger(StartGameActivity.class.getName());
    public User user;
    public Game game;
    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    GameService gameService = retrofit.create(GameService.class);

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
    public int maxTime = 30 * MILLIS_TIME;
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

        user = new User();
        user.setNickname("string");
        user.setPassword("string");

//        if(getIntent().getExtras() != null) {
//            user = (User) getIntent().getSerializableExtra("User");
//            System.out.println(user);
//            game= new Game(user.getNickname(), user.getId());
//
//            System.out.println(game);
//            Call<Game> call = gameService.doPostGame(game);
//
//            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//            if (SDK_INT > 8){
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//
//                try {
//                    System.out.println(call.request().url().toString());
//                    Response<Game> response = call.execute();
//                    if(response.isSuccessful()){
//                        game = response.body();
//                        System.out.println(game.getId());
//                        System.out.println(game);
//                    }else{
//                        System.out.println("------|>response.errorBody()");
//                        System.out.println(response.errorBody());
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//        }


        // Get game difficulty Level
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gameLevel = sharedPref.getString("difficulty_level", "Easy");

        showTime = (TextView) findViewById(R.id.textShowTime);
        showScore = (TextView) findViewById(R.id.textShowScore);
        showLive = (TextView) findViewById(R.id.textShowLive);
        showScore.setText(String.valueOf(gameScore));
        showLive.setText(String.valueOf(gameLive));

        matchTimer.start(); // Start the game
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


    public class gameTimer extends CountDownTimer { // game clock
        public gameTimer(int maxTime, long stepTime) { super(maxTime, stepTime);}

        @Override
        public void onFinish() {
            this.cancel();
            // TODO CAMBIAR MENSAJE
            modalMessage(getString(R.string.str_end_time));

        }

        public void onTick(long millisUntilFinished) {
            int currentTime = (int) (millisUntilFinished / MILLIS_TIME);
            showTime.setText(String.valueOf(currentTime)); // update gameTime

            updateLives();

            if ((currentTime%5 == 0) &&  (currentTime != 60)){
                switch(gameLevel) {
                    case "Easy":
                        timeInterval *= 0.99;
                        gopherWaitTime *= 0.99;
                    case "Medium":
                        timeInterval *= 0.97;
                        gopherWaitTime *= 0.97;
                    case "Hard":
                        timeInterval *= 0.95;
                        gopherWaitTime *= 0.95;
                }
            }
        }
    }

    public void modalMessage(String sms){
        flagEndGame = true;
        matchTimer.cancel();
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.str_end_game))
                .setMessage(sms)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EndGame(sms);
                    }
                })
                .setCancelable(false)
                .setIcon(R.drawable.whackamole_start)
                .show();
    }

    public void EndGame( String reason) {     // Push message End the game!
        System.out.println("void EndGame(int EndScore, String Reason)");
        System.out.println("void EndGame(int EndScore, String Reason)");

        System.out.println(game);
        game.setScore(gameScore);
        game.setWinner(user.getNickname());
        System.out.println(game.toString());

        Call<Game> call = gameService.doPutGame(game.getId(), game);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                System.out.println(call.request().url().toString());
                Response<Game> response = call.execute();
                if(response.isSuccessful()){
                    System.out.println(game);
                }else{
                    System.out.println("------|>response.errorBody()");
                    System.out.println(response.errorBody());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        Intent intent = new Intent(getApplicationContext(), MenuGameActivity.class);
        matchTimer.cancel();
        startActivity(intent);
        this.finish();

    }


    public Runnable gameLoop = new Runnable() { // Game loop run every timeInterval
        @Override
        public void run () {
            numHole = new Random().nextInt(8);

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
//
                                    }
                                });
                                System.out.println(gameLive);
                                gameLive -= 1;
                                System.out.println("-----------------1");
                                System.out.println(gameLive);
//                                updateLives(); // Update lives
                                mPlayerMiss.start();
                            }
                        }
                    }
                }
            }, timeInterval);

            if (!flagEndGame) {handler.postDelayed(gameLoop, timeInterval);}
        }
    };

    public void updateLives(){
        if(gameLive > -1){
            showLive.setText(String.valueOf(gameLive));
        }else{
            modalMessage(getString(R.string.str_end_lives));
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










