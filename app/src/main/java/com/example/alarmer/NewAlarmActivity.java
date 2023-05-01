package com.example.alarmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class NewAlarmActivity extends AppCompatActivity {

    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        setTitle("New Alarm");

        btnSave = findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTitle = findViewById(R.id.edit_text_title);
                String title = editTextTitle.getText().toString();

                if (title.isEmpty()) {
                    editTextTitle.setError("Title cannot be empty");
                    editTextTitle.requestFocus();
                    return;
                }

                TimePicker timePicker = findViewById(R.id.time_picker);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);


                Alarm newAlarm = new Alarm(-1, time, title, true);

                //Adding the new alarm in the database
                int id = ((AlarmApplication) getApplication()).addAlarm(newAlarm);
                newAlarm.setId(id);

//                // Set the time when the alarm should ring
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);


                AlarmUtil.setAlarm(getApplicationContext(), newAlarm);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_top);
    }

}