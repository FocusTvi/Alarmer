package com.example.alarmer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the title and ID of the alarm
        String title = intent.getStringExtra("title");
        int id = intent.getIntExtra("id", -1);
        intent = new Intent(context, StopAlarmActivity.class );
        intent.putExtra("id",id);
        intent.putExtra("title",title);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


        Intent serviceIntent = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("id", id);
        context.startService(serviceIntent);
    }


}
