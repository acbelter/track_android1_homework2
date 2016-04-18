package com.acbelter.android1.homework2.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.acbelter.android1.homework2.MainApplication;
import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.adapter.TechItemsCursorAdapter;
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        Cursor cursor = (Cursor) mTechItemsAdapter.getItem(position);
//        int itemId = cursor.getInt(DbHelper.INDEX_ID);
        ft.replace(R.id.fragment_container, TechPagerFragment.newInstance(position), TechPagerFragment.tag());
        ft.addToBackStack(null);
        ft.commit();
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
