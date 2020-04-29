package com.example.timtom;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class NewHabitActivity extends AppCompatActivity {
    ArrayList<habit> medList;
    ArrayList<Calendar> calendarTimes = new ArrayList<>();;
    private String name;
    private String times;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhabit);
        //load medList with function created bellow
        loadData();

        //buttonDone should add the new habit and save ArrayList
        Button buttonDone = findViewById(R.id.but_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et1 = findViewById(R.id.editName);
                name = et1.getEditableText().toString();

                TextView et2 = findViewById(R.id.time_display);
                times = et2.getText().toString();

                Random randi = new Random();
                int rand = randi.nextInt(99999999);
                medList.add( new habit(name, times, rand, calendarTimes));
                saveData();
                openHome();
            }
        });

        ///////////////////////////////////////////////////////////
        //-------------------------Times---------------------------
        ///////////////////////////////////////////////////////////
        final TextView timeTextView = findViewById(R.id.time_display);

        Button timePicker = findViewById(R.id.button_setTime);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewHabitActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String min = "";
                                if (minute<10){
                                    min="0";
                                }
                                min = min + minute;

                                timeTextView.append("(" + hourOfDay + ":" + min + ")");
                                Calendar chosen = Calendar.getInstance();
                                chosen.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                chosen.set(Calendar.MINUTE, Integer.parseInt(min));
                                chosen.set(Calendar.SECOND, 0);
                                //used in setting the alarms
                                calendarTimes.add(chosen);
                            }
                        }, 0, 0, false);
                timePickerDialog.show();
            }
        });

    }



    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(medList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<habit>>() {}.getType();
        medList = gson.fromJson(json, type);
        if (medList == null) {
            medList = new ArrayList<>();
        }
    }
    public void openHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
