package com.example.alarmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private AlarmsFragment alarmsFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_alarms:
                    loadFragment(alarmsFragment);
                    return true;
                case R.id.nav_stopwatcher:
                    loadFragment(new StopwatcherFragment());
                    return true;
                case R.id.nav_timer:
                    loadFragment(new TimerFragment());
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        alarmsFragment = new AlarmsFragment();
        ((AlarmApplication) getApplication()).setAlarmsFragment(alarmsFragment);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(alarmsFragment); // Load the alarms fragment by default

// Load the AlarmsFragment by default
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlarmsFragment()).commit();

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (currentFragment instanceof AlarmsFragment) {
            RecyclerView recyclerView = findViewById(R.id.alarmsRecyclerView);
            AlarmAdapter alarmAdapter = (AlarmAdapter) recyclerView.getAdapter();
            alarmAdapter.setAlarmList(((AlarmApplication) getApplication()).loadAlarms());
            alarmAdapter.notifyDataSetChanged();
        }

    }
}