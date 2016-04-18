package com.acbelter.android1.homework2.ui;

public interface DataLoadingListener {
    void onDataLoaded();
    void onDataLoadingFailed(int errorResId);
}