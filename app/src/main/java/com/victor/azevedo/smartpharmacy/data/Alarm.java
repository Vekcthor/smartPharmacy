package com.victor.azevedo.smartpharmacy.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

@Entity(tableName = "alarm")
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "medication_name")
    public String medicationName;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "daily_frequency")
    public int dailyFrequency;

    @ColumnInfo(name = "dose")
    public int dose;

    @ColumnInfo(name = "inventory")
    public int inventory;

    @ColumnInfo(name = "weekdays")
    public String weekdays;

    public Alarm(String medicationName, long time, int dailyFrequency, int dose, int inventory, String weekdays) {
        this.medicationName = medicationName;
        this.time = time;
        this.dailyFrequency = dailyFrequency;
        this.dose = dose;
        this.inventory = inventory;
        this.weekdays = weekdays;
    }
}
