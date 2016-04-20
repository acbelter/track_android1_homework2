package com.acbelter.android1.homework2.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acbelter.android1.homework2.MainApplication;
import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.adapter.TechItemsCursorFragmentPagerAdapter;
import com.acbelter.android1.homework2.db.DbHelper;

public class TechPagerFragment extends Fragment {
    private int mStartPosition;
    private ViewPager mPager;

    public static TechPagerFragment newInstance(int startPosition) {
        TechPagerFragment fragment = new TechPagerFragment();
        fragment.mStartPosition = startPosition;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_pager, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DbHelper dbHelper = MainApplication.getDbHelper();
        Cursor cursor = dbHelper.getTechnologies();
        TechItemsCursorFragmentPagerAdapter pagerAdapter =
                new TechItemsCursorFragmentPagerAdapter(getActivity(), getFragmentManager(), cursor);
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(mStartPosition);
    }

    public static String tag() {
        return TechPagerFragment.class.getSimpleName();
    }
}
