package com.example.alarmer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private List<Alarm> alarmList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Alarms");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.alarmsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Adding the floating action button to the screen
        FloatingActionButton fabAddAlarm = view.findViewById(R.id.fab_add_alarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewAlarmActivity.class);
                startActivity(intent, ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_bottom, R.anim.stay).toBundle());

            }
        });

        this.loadAdapter();
        return view;
    }

    public void loadAdapter() {
        // Set up the AlarmAdapter
        AlarmApplication app = ((AlarmApplication) requireActivity().getApplication());
        alarmList = app.loadAlarms();

        adapter = new AlarmAdapter(getActivity(), alarmList);

        //Setting the swipe event to the list
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

    }


}
