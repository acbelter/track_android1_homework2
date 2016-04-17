package com.acbelter.android1.homework2;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acbelter.android1.homework2.db.DbHelper;

public class TechListFragment extends ListFragment {
    private TechItemsCursorAdapter mTechItemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tech_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mTechItemsAdapter == null) {
            DbHelper dbHelper = MainApplication.getDbHelper();
            Cursor cursor = dbHelper.getTechnologies();
            mTechItemsAdapter = new TechItemsCursorAdapter(getActivity(), cursor);
        }
        setListAdapter(mTechItemsAdapter);
    }

    public static String tag() {
        return TechListFragment.class.getSimpleName();
    }
}
