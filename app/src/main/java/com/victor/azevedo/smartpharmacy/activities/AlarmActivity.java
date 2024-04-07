package com.victor.azevedo.smartpharmacy.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.victor.azevedo.smartpharmacy.R;
import com.victor.azevedo.smartpharmacy.data.Alarm;
import com.victor.azevedo.smartpharmacy.data.AppDatabase;

public class AlarmActivity extends AppCompatActivity {
    Button buttonDismiss, buttonSnooze;
    TextView textViewDosageMedicationAlarm, textViewDosageMedicationName;
    AlarmManager alarmManager;
    ImageView imageViewClock;

    PendingIntent pending_intent;

    Alarm alarm;
    AppDatabase appDatabase;
    String idAlarm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        imageViewClock = (ImageView)findViewById(R.id.activity_ring_clock);

        textViewDosageMedicationAlarm = (TextView)findViewById(R.id.textViewDosageMedicationAlarm);
        textViewDosageMedicationName = (TextView)findViewById(R.id.textViewDosageMedicationName);

        buttonDismiss = (Button)findViewById(R.id.activity_ring_dismiss);
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int inventory = alarm.inventory;
                        int dose = alarm.dose;

                        appDatabase.alarmDao().updateById(Math.max((inventory - dose), 0), alarm.uid);

                        AlarmActivity.this.finish();
                    }
                });

            }
        });
        buttonSnooze = (Button)findViewById(R.id.activity_ring_snooze);
        buttonSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("opa", "Snooze");
            }
        });

        animateClock();

        Intent intent = getIntent();

        idAlarm = intent.getStringExtra("idAlarm");

        appDatabase = AppDatabase.getInstance(AlarmActivity.this);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                alarm = appDatabase.alarmDao().loadById(Integer.parseInt(idAlarm));

                textViewDosageMedicationAlarm.setText("Tomar " + alarm.dose + " comprimidos");
                textViewDosageMedicationName.setText(alarm.medicationName);
            }
        });
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(imageViewClock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}
