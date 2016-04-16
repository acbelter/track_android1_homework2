package com.acbelter.android1.homework2;

public class TechItem {
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
