package com.example.whackamole.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whackamole.R;
import com.example.whackamole.models.Score;
import com.example.whackamole.services.UserServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ScoresActivity extends AppCompatActivity {

    ArrayList<Score> scores = new ArrayList<Score>();

    UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        scores.add(new Score("jose", 9));
        scores.add(new Score("j212ose", 92));
        scores.add(new Score("arrar", 535));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();

        Call<List> call = userServices.getPosts();

        init();
    }

    @SuppressLint("ResourceAsColor")
    public void init() {
        TableLayout table = (TableLayout) findViewById(R.id.table_score);
        TableRow tableRow = new TableRow(this);
        TextView tableCol0 = new TextView(this);
        tableCol0.setText(" No ");
        tableCol0.setTextColor(R.color.white);
//        tableCol0.set;
        tableRow.addView(tableCol0);
        TextView tableCol1 = new TextView(this);
        tableCol1.setText(" Gamer ");
        tableCol1.setTextColor(R.color.white);
        tableCol1.setPadding(25,25,25,25);
        tableRow.addView(tableCol1);
        TextView tableCol2 = new TextView(this);
        tableCol2.setText(" Score ");
        tableCol2.setTextColor(R.color.white);
        tableRow.addView(tableCol2);
        table.addView(tableRow);
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(R.color.white);
            t1v.setGravity(Gravity.LEFT);
            t1v.setTextSize(14);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(score.getNickname());
            t2v.setTextColor(R.color.white);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(14);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(String.valueOf(score.getScore()));
            t3v.setTextColor(R.color.white);
            t3v.setGravity(Gravity.RIGHT);
            t3v.setTextSize(14);
            tbrow.addView(t3v);
            table.addView(tbrow);
        }

    }
    
}
