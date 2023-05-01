package com.example.alarmer;

public class Alarm {
    private int mId;
    private String mTime;
    private boolean mEnabled;
    private String mTitle;



    public Alarm(int id, String time, String title, boolean enabled) {
        mId = id;
        mTime = time;
        mTitle = title;
        mEnabled = enabled;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }


    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
}

