package com.example.timtom;

import java.util.ArrayList;
import java.util.Calendar;

public class habit {

    String name;
    String times;
    int rand;
    ArrayList<Calendar> calendarTimes;


    public habit(String name, String times, int rand,  ArrayList<Calendar> calendarTimes) {
        this.name = name;
        this.times = times;
        this.rand = rand;
        this.calendarTimes = calendarTimes;
    }

    public String getName() {
        return name;
    }

    public String getTimes() {
        return times;
    }

    public int getRand() {
        return rand;
    }

    public ArrayList<Calendar> getCalendarTimes() {
        return calendarTimes;
    }
}
