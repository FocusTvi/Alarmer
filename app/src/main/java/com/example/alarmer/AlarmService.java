package com.example.alarmer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmService extends Service {

    private Ringtone ringtone;
    static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 0 ;

    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Step 1: we must create a notification channel to show the information
        createNotificacionChannel(this);


        // Get the title and ID of the alarm
        String title = intent.getStringExtra("title");
        int id = intent.getIntExtra("id", -1);


        // Ring the tone
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
        }
        ringtone.play();


        //We set the intent to stop the alert
        final Intent notificationIntent = new Intent(this, StopAlarmActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("id", id);
        PendingIntent pendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle(title)
                .setContentText("Alarm ringing")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
//                .addAction(R.drawable.ic_baseline_stop_24, "Stop", pendingIntent)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(id,notification );
//            startForeground(NOTIFICATION_ID, notification);
        }


        // Stop the service
//        stopSelf();

        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ringtone.isPlaying()){
            ringtone.stop();
        }
    }

    void createNotificacionChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Test Channel", NotificationManager.IMPORTANCE_DEFAULT);

            //Register our channel with the system
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
