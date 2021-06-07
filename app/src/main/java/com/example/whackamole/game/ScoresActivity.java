package com.example.whackamole.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whackamole.APIClient;
import com.example.whackamole.R;
import com.example.whackamole.models.Game;
import com.example.whackamole.models.Score;
import com.example.whackamole.services.GameService;
import com.example.whackamole.services.UserServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScoresActivity extends AppCompatActivity {

    List<Score> scores = new ArrayList<Score>();

    APIClient apiClient = new APIClient();
    Retrofit retrofit = apiClient.getClient();
    GameService gameService = retrofit.create(GameService .class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Get match
        Call<List<Score>> call = gameService.doGetScores();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Response<List<Score>> response = call.execute();
                if(response.isSuccessful()){
                    scores = response.body();
                    for (Score s : scores) {
                        scores.add(new Score(s.nickname, s.score));
                    }
                }else{
                    System.out.println(response.errorBody());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        init();
    }

    @SuppressLint("ResourceAsColor")
    public void init() {
        TableLayout table = (TableLayout) findViewById(R.id.table_score);
        TableRow tableRow = new TableRow(this);
        TextView tableCol0 = new TextView(this);
        tableCol0.setText(" Number   ");
        tableCol0.setTextSize(28);
        tableCol0.setTextColor(R.color.brown);
        tableRow.addView(tableCol0);
        TextView tableCol1 = new TextView(this);
        tableCol1.setText(" Gamer   ");
        tableCol1.setTextSize(28);
        tableCol1.setTextColor(R.color.brown);
        tableCol1.setPadding(25,25,25,25);
        tableRow.addView(tableCol1);
        TextView tableCol2 = new TextView(this);
        tableCol2.setText(" Score ");
        tableCol2.setTextSize(28);
        tableCol2.setTextColor(R.color.brown);
        tableRow.addView(tableCol2);
        table.addView(tableRow);
        for (int i = 0; i < scores.size()-1; i++) {
            Score score = scores.get(i);
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(R.color.black);
            t1v.setGravity(Gravity.LEFT);
            t1v.setTextSize(22);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(score.getNickname());
            t2v.setTextColor(R.color.black);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(22);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(String.valueOf(score.getScore()));
            t3v.setTextColor(R.color.black);
            t3v.setGravity(Gravity.RIGHT);
            t3v.setTextSize(22);
            tbrow.addView(t3v);
            table.addView(tbrow);
        }

    }
    
}
