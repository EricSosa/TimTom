package com.example.timtom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Button buttonNew;
    private Button buttonHabs;
    ArrayList<habit> habitList;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        buttonNew = findViewById(R.id.buttNewHabit);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewHabitForm();
            }
        });

        buttonHabs = findViewById(R.id.buttHabits);
        buttonHabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHabits();
            }
        });

        progressBar = findViewById(R.id.circulo);
        //to get a clock
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = findViewById(R.id.date);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);

                                //get hour for progress circle
                                SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
                                int x = Integer.parseInt(sdf2.format(date));
                                progressStatus = 100-(x*100/24);
                                progressBar.setProgress(progressStatus);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }


            }
        };
        t.start();
    }

    public void openNewHabitForm(){
        Intent intent = new Intent(this, NewHabitActivity.class);
        startActivity(intent);
    }

    public void openHabits(){
        Intent intent = new Intent(this, HabitsActivity.class);
        startActivity(intent);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<habit>>() {}.getType();
        habitList = gson.fromJson(json, type);

        if (habitList == null) {
            habitList = new ArrayList<>();
        }
    }
}