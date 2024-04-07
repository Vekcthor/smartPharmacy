package com.victor.azevedo.smartpharmacy.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.victor.azevedo.smartpharmacy.R;
import com.victor.azevedo.smartpharmacy.activities.AlarmActivity;
import com.victor.azevedo.smartpharmacy.activities.MainActivity;

public class RingtoneService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // fetch the extra string from the alarm on/alarm off values
        String state = intent.getExtras().getString("extra");
        String idAlarm = intent.getExtras().getString("idAlarm");
        // fetch the whale choice integer values
        //Integer whale_sound_choice = intent.getExtras().getInt("whale_choice");

        Log.e("Ringtone extra is ", state);

        // put the notification here, test it out

        // notification
        // set up the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // set up an intent that goes to the Main Activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), AlarmActivity.class);

        intent_main_activity.putExtra("idAlarm", idAlarm);
        // set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);

        // make the notification parameters
        NotificationCompat.Builder builder;
        Notification notification_popup = null;

        int NOTIFICATION_ID = 2119;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "SMART_PHARMACY_SERVICE";
            CharSequence name = "SMART_PHARMACY";
            String Description = "Channel made for the app SMART_PHARMACY";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.CYAN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);


            builder  = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_alarm_black_24dp)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setAutoCancel(true);

            Intent resultIntent = new Intent(this, AlarmActivity.class);
            resultIntent.putExtra("idAlarm", idAlarm);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }else{
            notification_popup  = new Notification.Builder(this)
                    .setContentTitle("An alarm is going off!")
                    .setContentText("Click me!")
                    .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                    .setContentIntent(pending_intent_main_activity)
                    .setAutoCancel(true)
                    .build();
        }
        // this converts the extra strings from the intent
        // to start IDs, values 0 or 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }
        // if else statements

        // if there is no music playing, and the user pressed "alarm on"
        // music should start playing
        if (!this.isRunning && startId == 1) {
            Log.e("there is no music, ", "and you want start");

            this.isRunning = true;
            this.startId = 0;

            Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
            vibrator.vibrate(4000);

            // set up the start command for the notification
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                notify_manager.notify(0, notification_popup);
            }
            // play the whale sound depending on the passed whale choice id
            // create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.alarm);
            // start the ringtone
            media_song.start();
        }

        // if there is music playing, and the user pressed "alarm off"
        // music should stop playing
        else if (this.isRunning && startId == 0) {
            Log.e("there is music, ", "and you want end");
            // stop the ringtone
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        // these are if the user presses random buttons
        // just to bug-proof the app
        // if there is no music playing, and the user pressed "alarm off"
        // do nothing
        else if (!this.isRunning && startId == 0) {
            Log.e("there is no music, ", "and you want end");

            this.isRunning = false;
            this.startId = 0;
        }
        // if there is music playing and the user pressed "alarm on"
        // do nothing
        else if (this.isRunning && startId == 1) {
            Log.e("there is music, ", "and you want start");
            this.isRunning = true;
            this.startId = 1;
        }
        // can't think of anything else, just to catch the odd event
        else {
            Log.e("else ", "somehow you reached this");
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Log.e("on Destroy called", "ta da");
        super.onDestroy();
        this.isRunning = false;
    }

}
