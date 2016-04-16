package com.acbelter.android1.homework2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acbelter.android1.homework2.TechnologyItem;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "evo_db";
    private static final String TABLE_TECHNOLOGY = "technology";

    private static final String KEY_ID = "id";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INFO = "info";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLE_TECHNOLOGY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PICTURE + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_INFO + " TEXT)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNOLOGY);
        onCreate(db);
    }

    public void insertTechnologies(List<TechnologyItem> items) {
        SQLiteDatabase db = getWritableDatabase();
        for (TechnologyItem item : items) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, item.id);
            values.put(KEY_PICTURE, item.picture);
            values.put(KEY_TITLE, item.title);
            values.put(KEY_INFO, item.info);
            db.insert(TABLE_TECHNOLOGY, null, values);
        }
        db.close();
    }

    public void deleteTechnologies() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TECHNOLOGY, null, null);
        db.close();
    }
}
