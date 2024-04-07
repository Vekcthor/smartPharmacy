package com.victor.azevedo.smartpharmacy.data;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AlarmModel {
    private final String medicationName;
    private final int id;
    private final long time;
    private final int dailyFrequency;
    private final int dose;
    private final int inventory;
    private final String weekdays;

    public AlarmModel(int id, String medicationName, long time, String weekdays, int dailyFrequency, int dose, int inventory) {
        this.id = id;
        this.medicationName = medicationName;
        this.time = time;
        this.weekdays = weekdays;
        this.dailyFrequency = dailyFrequency;
        this.dose = dose;
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public long getTime() {
        return time;
    }

    public String getWeekdays() {
        return weekdays;
    }

    public int getDailyFrequency() {
        return dailyFrequency;
    }

    public int getDose() {
        return dose;
    }

    public int getInventory() {
        return inventory;
    }
}
