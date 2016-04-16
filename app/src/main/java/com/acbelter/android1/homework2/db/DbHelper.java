package com.acbelter.android1.homework2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acbelter.android1.homework2.TechItem;

import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "evo_db";

    public static final String TABLE_TECHNOLOGY = "technology";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_INFO = "info";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLE_TECHNOLOGY + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ID + " INTEGER,"
                + COLUMN_PICTURE + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_INFO + " TEXT)";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNOLOGY);
        onCreate(db);
    }

    public void insertTechnologies(List<TechItem> items) {
        SQLiteDatabase db = getWritableDatabase();
        for (TechItem item : items) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, item.id);
            values.put(COLUMN_PICTURE, item.picture);
            values.put(COLUMN_TITLE, item.title);
            values.put(COLUMN_INFO, item.info);
            db.insert(TABLE_TECHNOLOGY, null, values);
        }
        db.close();
    }

    public Cursor getTechnologies() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_TECHNOLOGY, null, null, null, null, null, null, null);
    }

    public void deleteTechnologies() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TECHNOLOGY, null, null);
        db.close();
    }
}
