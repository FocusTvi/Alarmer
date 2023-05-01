package com.example.alarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class StopAlarmActivity extends AppCompatActivity {

    private int alarmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        View view = findViewById(R.id.stop_alarm_container);
        Snackbar.make( view, "The alarm is ringing", Snackbar.LENGTH_LONG).show();

        // Get the alarm ID from the extra
        alarmId = getIntent().getIntExtra("id", -1);


        // Add a click listener to the stop button
        Button btnStop = findViewById(R.id.button_stop_alarm);
        TextView titleTextView = findViewById(R.id.titleTextView);

        titleTextView.setText(getIntent().getStringExtra("title"));
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disabling the alarm
                AlarmApplication app = (AlarmApplication) getApplicationContext();
                app.updateAlarm(new Alarm(alarmId,"","",false));
                // Stop the alarm
                stopAlarm();

                // Finish the activity
                finish();
            }
        });
    }

    private void stopAlarm() {
        // Stop the alarm sound
        Intent intent = new Intent(this.getApplicationContext(), AlarmService.class);
        stopService(intent);
        // Cancel the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(alarmId);

        intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);


        // TODO: Update the database to mark the alarm as disabled
    }
}