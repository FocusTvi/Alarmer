package com.example.alarmer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StopwatcherFragment extends Fragment {

    private Chronometer chronometer;
    private boolean isRunning;
    private SharedPreferences preferences;
    private long startTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Init the sharedprefrences
        preferences = getActivity().getPreferences(getActivity().getApplicationContext().MODE_PRIVATE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stopwatcher, container, false);

        chronometer = view.findViewById(R.id.chronometer);
        getActivity().setTitle("Stopwatcher");
        FloatingActionButton fabStopwatcher = view.findViewById(R.id.fab_stopwatcher);
        fabStopwatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable icon = null;
                if (!isRunning) {
                    startTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    isRunning = true;
                    icon = getResources().getDrawable(R.drawable.ic_baseline_stop_24);

                } else {
                    isRunning = false;
                    chronometer.stop();
                    icon = getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24);
                }
                fabStopwatcher.setImageDrawable(icon);
            }
        });


//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        startTime = preferences.getLong("start_time", 0);
        long stop_time = preferences.getLong("stop_time", 0);
        isRunning = preferences.getBoolean("is_running", false);

        FloatingActionButton fabStopwatcher = getView().findViewById(R.id.fab_stopwatcher);
        Drawable icon = null;
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();
        if (isRunning) {
            icon = getResources().getDrawable(R.drawable.ic_baseline_stop_24);


            // Calculate the time that has passed in seconds
            long timePassedSinceLastTime = ((currentTime - stop_time));

            if (startTime != 0) {
                chronometer.setBase(SystemClock.elapsedRealtime() - startTime - timePassedSinceLastTime);
                chronometer.start();
            }
        } else {
//            int seconds = (int) (startTime / 1000) % 60; // The elapsed seconds
//            int minutes = (int) ((startTime / (1000*60)) % 60);
//            chronometer.setBase(SystemClock.elapsedRealtime() - (startTime));
////            chronometer.start();
            icon = getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24);
        }
        fabStopwatcher.setImageDrawable(icon);

    }


    @Override
    public void onStop() {
        super.onStop();
        startTime = SystemClock.elapsedRealtime() - chronometer.getBase();

        SharedPreferences.Editor editor = preferences.edit();
        // Get the current time in milliseconds
        long currentTime = System.currentTimeMillis();

        editor.putLong("start_time", startTime);
        editor.putLong("stop_time", currentTime);
        editor.putBoolean("is_running", isRunning);
        editor.commit();
    }

}
