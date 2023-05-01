package com.example.alarmer;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmApplication extends Application {
    private static final String DB_NAME = "db_alarmer";
    private static final int DB_VERSION = 1;
    private SQLiteOpenHelper helper;
    private boolean isForeground = true;

    private AlarmsFragment alarmsFragment;

    public void setAlarmsFragment(AlarmsFragment alarmsFragment) {
        this.alarmsFragment = alarmsFragment;
    }

    public AlarmsFragment getAlarmsFragment() {
        return alarmsFragment;
    }

    // Define the table name and columns
    private static final String TABLE_NAME = "tb_alarms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ENABLED = "enabled";

    @Override
    public void onCreate() {

        super.onCreate();
//        registerActivityLifecycleCallbacks(this);
        helper = new SQLiteOpenHelper(this, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String createTableSql = "CREATE TABLE " + TABLE_NAME + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_TIME + " TEXT, "
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_ENABLED + " INTEGER DEFAULT 0)";
                db.execSQL(createTableSql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };

    }


    // Save an alarm to the database
    public int addAlarm(Alarm alarm) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, alarm.getTime());
        values.put(COLUMN_TITLE, alarm.getTitle());
        values.put(COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);

        long newRowId = db.insert(TABLE_NAME, null, values);

        db.close();
        return (int) newRowId;
    }


    public List<Alarm> loadAlarms() {

        List<Alarm> alarmList = new ArrayList<>();
//        alarmList.add(new Alarm(1, "8:00 AM", "Wake Up", true));
//        alarmList.add(new Alarm(2, "3:00 PM", "Gym Time", true));
//        return alarmList;
        // Define the columns you want to retrieve from the table
        String[] projection = {
                COLUMN_ID,
                COLUMN_TIME,
                COLUMN_TITLE,
                COLUMN_ENABLED
        };

        // Sort the results by the ID in ascending order
        String sortOrder = COLUMN_ID + " ASC";
        SQLiteDatabase db = helper.getReadableDatabase();
        // Query the table and retrieve the results
        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,                            // The columns to retrieve
                null,                                  // The columns for the WHERE clause
                null,                                  // The values for the WHERE clause
                null,                                  // Don't group the results
                null,                                  // Don't filter by row groups
                sortOrder                              // The sort order
        );

        // Loop through the results and add each row to the list
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            boolean enabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENABLED)) == 1;

            Alarm alarm = new Alarm(id, time, title, enabled);
            alarmList.add(alarm);
        }

        // Close the cursor and return the list
        cursor.close();
        return alarmList;
    }

    public void updateAlarm(Alarm alarm) {
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111");
        System.out.println(alarm.getId());
        System.out.println(alarm.isEnabled());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //we are just updating the enabled column
        values.put(COLUMN_ENABLED, alarm.isEnabled());
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(alarm.getId())};
        db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }


    public boolean deleteAlarm(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
//        if (deletedRows > 0) {
//            mAlarms.remove(getAlarmIndex(id));
//            notifyDataSetChanged();
//        }
    }

//    public void updateResults() {
//        SQLiteDatabase db = helper.getReadableDatabase();
//        String selectSql = "SELECT result, COUNT(*) AS count FROM tb_game_results GROUP BY result";
//        Cursor cursor = db.rawQuery(selectSql, null);
////        Map<MainActivity.GameResult, Integer> counts = new HashMap<>();
////        while (cursor.moveToNext()) {
////            MainActivity.GameResult result = MainActivity.GameResult.valueOf(cursor.getString(cursor.getColumnIndex("result")));
////            int count = cursor.getInt(cursor.getColumnIndex("count"));
////            counts.put(result, count);
////        }
////
////        this.wins = counts.getOrDefault(MainActivity.GameResult.WIN, 0);
////        this.loses = counts.getOrDefault(MainActivity.GameResult.LOSE, 0);
////        this.ties = counts.getOrDefault(MainActivity.GameResult.TIE, 0);
//
//        // Close the database connection
//        cursor.close();
//        db.close();
//    }

//    public void resetGameStats() {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        db.execSQL("DELETE FROM tb_game_results");
//        wins = 0;
//        loses = 0;
//        ties = 0;
//        MainActivity mainActivity = getMainActivity();
//        mainActivity.performReset();
//
//    }

//    @Override
//    public void onActivityResumed(Activity activity) {
//        // The app is in the foreground
//        isForeground = true;
//    }
//
//    @Override
//    public void onActivityPaused(Activity activity) {
//        // The app is in the background
//        isForeground = false;
//    }
//
//    @Override
//    public void onActivityStopped(Activity activity) {
//        // The app is no longer visible to the user
//        if (!isForeground) {
//            // Start the service when the app is in the background
//            Intent startIntent = new Intent(getApplicationContext(), NotificationService.class);
//            startService(startIntent);
//        }
//    }
//
//    @Override
//    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
//
//    }
//
//    @Override
//    public void onActivityDestroyed(@NonNull Activity activity) {
//
//    }
//
//    @Override
//    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onActivityStarted(Activity activity) {
//        // The app is visible to the user
//        // Stop the service when the app is in the foreground
//        Intent stopIntent = new Intent(getApplicationContext(), NotificationService.class);
//        stopService(stopIntent);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        // Check if the app is in landscape mode
//        super.onConfigurationChanged(newConfig);
//        stopService(new Intent(getApplicationContext(), NotificationService.class));
//    }
//

}
