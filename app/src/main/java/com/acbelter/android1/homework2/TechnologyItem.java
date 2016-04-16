package com.acbelter.android1.homework2;

public class TechnologyItem {
    public int id;
    public String picture;
    public String title;
    public String info;

    public String getPictureUrl() {
        return Api.BASE_URL + picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnologyItem that = (TechnologyItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "TechnologyItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
