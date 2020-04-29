package com.example.timtom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class HabitsActivity extends AppCompatActivity {
    private RecyclerView myRecycler;
    private RecyclerAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    ArrayList<habit> medList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits);

        //load medList with function created bellow
        loadData();
        buildRecyclerView();

        Button buttonReset = findViewById(R.id.save);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                openHome();
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

    public void buildRecyclerView(){
        myRecycler = findViewById(R.id.recyclerView);
        myRecycler.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new RecyclerAdapter(medList);


        myRecycler.setLayoutManager(myLayoutManager);
        myRecycler.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {

            @Override
            public void onDeleteClick(int position) {
                //remove alarms first
                habit currentItem = medList.get(position);
                ArrayList<Calendar> calendarTimes = currentItem.calendarTimes;
                int rand = currentItem.rand;
                for (int i = 0; i < calendarTimes.size(); i++) {
                    cancelAlarm(rand,i);
                }

                //remove habit from list
                medList.remove(position);
                myAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Removed", Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onAlarm_OnClick(int position) {
                habit currentItem = medList.get(position);
                ArrayList<Calendar> calendarTimes = currentItem.calendarTimes;
                int rand = currentItem.rand;
                Toast.makeText(getApplicationContext(),"Alarm On", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < calendarTimes.size(); i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        startAlarm(calendarTimes.get(i),rand,i);
                    }
                }
            }

            @Override
            public void onAlarm_offClick(int position) {
                habit currentItem = medList.get(position);
                ArrayList<Calendar> calendarTimes = currentItem.calendarTimes;
                int rand = currentItem.rand;
                Toast.makeText(getApplicationContext(),"Alarm Off", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < calendarTimes.size(); i++) {
                    cancelAlarm(rand,i);
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c, int rand, int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        //set unique code to initial milliseconds + i
        int uniqueID = rand+i;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, intent, 0); //The second parameter is unique to this PendingIntent
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), pendingIntent);
    }

    private void cancelAlarm(int rand,int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        int uniqueID = rand+i;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, intent, 0); //The second parameter is unique to this PendingIntent
        alarmManager.cancel(pendingIntent);
    }
}
