package com.example.timtom;

import java.util.ArrayList;
import java.util.Calendar;

public class habit {

    String name;
    String times;
    int dosage;
    int period;
    int rand;
    ArrayList<Calendar> calendarTimes;


    public habit(String name, String times, int dosage, int period, int rand,  ArrayList<Calendar> calendarTimes) {
        this.name = name;
        this.times = times;
        this.dosage = dosage;
        this.period = period;
        this.rand = rand;
        this.calendarTimes = calendarTimes;
    }

    public String getName() {
        return name;
    }

    public String getTimes() {
        return times;
    }

    public int getDosage() {
        return dosage;
    }

    public int getPeriod() {
        return period;
    }

    public int getRand() {
        return rand;
    }

    public ArrayList<Calendar> getCalendarTimes() {
        return calendarTimes;
    }
}
