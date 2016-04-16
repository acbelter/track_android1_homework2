package com.acbelter.android1.homework2;

public interface DataLoadingListener {
    void onDataLoaded();
    void onDataLoadingFailed(int errorResId);
}