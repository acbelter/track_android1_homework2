package com.acbelter.android1.homework2;

import android.app.Application;

import com.acbelter.android1.homework2.db.DbHelper;

public class MainApplication extends Application {
    public static final String TAG = "HW2";
    private static DbHelper sDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sDbHelper = new DbHelper(this);
    }

    public static synchronized DbHelper getDbHelper() {
        return sDbHelper;
    }
}
