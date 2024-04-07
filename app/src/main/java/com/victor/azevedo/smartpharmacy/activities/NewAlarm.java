package com.victor.azevedo.smartpharmacy.activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.victor.azevedo.smartpharmacy.R;
import com.victor.azevedo.smartpharmacy.data.Alarm;
import com.victor.azevedo.smartpharmacy.data.AlarmDao;
import com.victor.azevedo.smartpharmacy.data.AlarmModel;
import com.victor.azevedo.smartpharmacy.data.AppDatabase;
import com.victor.azevedo.smartpharmacy.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class NewAlarm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    AlarmManager alarmManager;
    Context context;
    PendingIntent pendingIntent;
    TimePicker timePickerAlarm;
    EditText editTextNameMedication;
    EditText editTextDose;
    EditText editTextInvetory;
    Button buttonSave;
    Spinner spinnerDailyFrequency;
    private int frequency = 1;
    String selectedDaysByUser;
    Alarm idNewAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm);
        this.context = this;

        // initialize our alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePickerAlarm = (TimePicker) findViewById(R.id.timerPickerMedication);

        // create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.daily_frequency, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDailyFrequency = (Spinner) findViewById(R.id.spinnerDailyFrequency);
        spinnerDailyFrequency.setAdapter(adapter);
        spinnerDailyFrequency.setOnItemSelectedListener(this);

        final MaterialDayPicker materialDayPicker = findViewById(R.id.day_picker);
        materialDayPicker.setLocale( new Locale("pt"));
        materialDayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(@NonNull List<MaterialDayPicker.Weekday> selectedDays) {
                StringBuilder translatedDays = new StringBuilder();
                for(int i=0; i < selectedDays.size(); i++){
                    if(selectedDays.size() > 1){
                        translatedDays.append(" ");
                    }
                    switch (selectedDays.get(i)){
                        case SUNDAY:
                            translatedDays.append("Dom");
                            break;
                        case MONDAY:
                            translatedDays.append("Seg");
                            break;
                        case TUESDAY:
                            translatedDays.append("Ter");
                            break;
                        case WEDNESDAY:
                            translatedDays.append("Qua");
                            break;
                        case THURSDAY:
                            translatedDays.append("Qui");
                            break;
                        case FRIDAY:
                            translatedDays.append("Sex");
                            break;
                        case SATURDAY:
                            translatedDays.append("Sab");
                            break;
                    }
                }
                selectedDaysByUser = translatedDays.toString();
            }
        });

        editTextNameMedication = (EditText) findViewById(R.id.editTextNameMedication);
        editTextDose= (EditText) findViewById(R.id.editTextDosageMedication);
        editTextInvetory= (EditText) findViewById(R.id.editTextInventoryMedication);

        buttonSave = (Button) findViewById(R.id.buttonSaveNewAlarm);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                String nameMedication = editTextNameMedication.getText().toString();
                final String nameMedicationFinal = !nameMedication.equals("") ? nameMedication  : "Remédio";
                String doseString = editTextDose.getText().toString();
                doseString = !doseString.equals("") ? doseString : "1";
                int dose = Integer.parseInt(doseString);
                String inventoryString = editTextDose.getText().toString();
                inventoryString = !inventoryString.equals("") ? inventoryString : "10";
                int inventory = Integer.parseInt(inventoryString);

                int random = new Random().nextInt((10000) + 1);

                // setting calendar instance with the hour and minute that we picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, timePickerAlarm.getHour());
                calendar.set(Calendar.MINUTE, timePickerAlarm.getMinute());

                final long time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Insert Data
                        AppDatabase appDatabase = AppDatabase.getInstance(NewAlarm.this);

                        Alarm alarm = new Alarm( nameMedicationFinal, time, frequency, dose, inventory, selectedDaysByUser);

                        appDatabase.alarmDao().insertAll(alarm);
                        idNewAlarm = appDatabase.alarmDao().findLastAlarm();

                        System.out.println(String.valueOf(idNewAlarm.uid));
                        // create an intent to the Alarm Receiver class
                        Intent my_intent = new Intent(NewAlarm.this, AlarmReceiver.class);
                        // put in extra string into my_intent
                        // tells the clock that you pressed the "alarm on" button
                        my_intent.putExtra("extra", "alarm on");

                        // put in an extra int into my_intent
                        // tells the clock that you want a certain value from the drop-down menu/spinner
                        my_intent.putExtra("idAlarm", String.valueOf(idNewAlarm.uid));
                        Log.e("idAlarm", String.valueOf(idNewAlarm.uid));

                        // create a pending intent that delays the intent
                        // until the specified calendar time
                        pendingIntent = PendingIntent.getBroadcast(NewAlarm.this, 0,
                                my_intent, 0);

                        long timeAmPm = time;
                        if (System.currentTimeMillis() > time) {
                            // setting time as AM and PM

                            if (calendar.AM_PM == 0)
                                timeAmPm = time + (1000 * 60 * 60 * 12);
                            else
                                timeAmPm = time + (1000 * 60 * 60 * 24);
                        }

                        // set the alarm manager
                /*alarmManager.set(AlarmManager.RTC_WAKEUP, time,
                        pendingIntent);*/
                        // Alarm rings continuously until toggle button is turned off
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAmPm, 10000, pendingIntent);

                        // Get Data   AppDatabase.getInstance(context).userDao().getAllUsers();
                    }
                });

                // get the int values of the hour and minute
                //int hour = timePickerAlarm.getHour();
                //int minute = timePickerAlarm.getMinute();

                // convert the int values to strings
                /*String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                // convert 24-hour time to 12-hour time
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //10:7 --> 10:07
                    minute_string = "0" + String.valueOf(minute);
                }*/

                // method that changes the update text Textbox
                //set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                Intent ntent = new Intent(NewAlarm.this, MainActivity.class);
                startActivity(ntent);
                //finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (pos) {
            case 0:
                frequency = 1;
                break;
            case 1:
                frequency = 12;
                break;
            case 2:
                frequency = 6;
                break;
            case 3:
                frequency = 4;
                break;
            case 4:
                frequency = 3;
                break;
            case 5:
                frequency = 2;
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        frequency = 1;
    }

    // OnToggleClicked() method is implemented the time functionality
    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(NewAlarm.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            // calender is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, timePickerAlarm.getCurrentHour());
            calendar.set(Calendar.MINUTE, timePickerAlarm.getCurrentMinute());

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            Intent intent = new Intent(this, AlarmReceiver.class);

            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }
}