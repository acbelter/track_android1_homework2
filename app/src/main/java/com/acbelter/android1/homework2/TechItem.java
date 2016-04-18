package com.acbelter.android1.homework2;

import android.database.Cursor;

import com.acbelter.android1.homework2.db.DbHelper;

public class TechItem {
    public int id;
    public String picture;
    public String title;
    public String info;

    public TechItem() {}

    public TechItem(Cursor cursor) {
        id = cursor.getInt(DbHelper.INDEX_ID);
        picture = cursor.getString(DbHelper.INDEX_PICTURE);
        title = cursor.getString(DbHelper.INDEX_TITLE);
        info = cursor.getString(DbHelper.INDEX_INFO);
    }

    public String getPictureUrl() {
        return picture != null ? Api.BASE_URL + picture : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechItem that = (TechItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "TechItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
