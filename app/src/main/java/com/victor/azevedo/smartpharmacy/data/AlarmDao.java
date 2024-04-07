package com.victor.azevedo.smartpharmacy.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarm WHERE uid IN (:alarmIds)")
    List<Alarm> loadAllByIds(int[] alarmIds);

    @Query("SELECT * FROM alarm WHERE medication_name LIKE :name LIMIT 1")
    Alarm findByName(String name);

    @Query("SELECT * FROM alarm ORDER BY uid DESC LIMIT 1;")
    Alarm findLastAlarm();

    @Query("SELECT * FROM alarm WHERE uid = :alarmId")
    Alarm loadById(int alarmId);

    @Query("UPDATE alarm SET inventory=:newInventory WHERE uid = :alarmId")
    void updateById(int newInventory, int alarmId);

    @Insert
    void insertAll(Alarm... alarms);

    @Delete
    void delete(Alarm alarm);
}