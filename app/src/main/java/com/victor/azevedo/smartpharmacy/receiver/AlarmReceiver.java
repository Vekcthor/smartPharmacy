package com.victor.azevedo.smartpharmacy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.victor.azevedo.smartpharmacy.service.RingtoneService;

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {

        // we will use vibrator first
        /*Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // setting default ringtone
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        // play ringtone
        ringtone.play();*/

        Log.e("We are in the receiver.", "Yay!");

        // fetch extra strings from the intent
        // tells the app whether the user pressed the alarm on button or the alarm off button
        String get_your_string = intent.getExtras().getString("extra");
        String idAlarm = intent.getExtras().getString("idAlarm");

        Log.e("What is the key? ", get_your_string);

        // fetch the extra longs from the intent
        // tells the app which value the user picked from the drop down menu/spinner
        //Integer get_your_whale_choice = intent.getExtras().getInt("whale_choice");

        //Log.e("The whale choice is ", get_your_whale_choice.toString());

        // create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtoneService.class);

        // pass the extra string from Receiver to the Ringtone Playing Service
        service_intent.putExtra("extra", get_your_string);
        // pass the extra integer from the Receiver to the Ringtone Playing Service
        service_intent.putExtra("idAlarm", idAlarm);

        // start the ringtone service
        context.startService(service_intent);
    }
}
