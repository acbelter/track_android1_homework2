package com.acbelter.android1.homework2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.acbelter.android1.homework2.TechItem;
import com.acbelter.android1.homework2.imgloader.ImageLoader;
import com.acbelter.android1.homework2.imgloader.MultipleImageLoader;
import com.acbelter.android1.homework2.ui.TechItemFragment;

public class TechItemsCursorFragmentPagerAdapter extends FragmentPagerAdapter {
    private ImageLoader mImageLoader;
    private Cursor mCursor;

    public TechItemsCursorFragmentPagerAdapter(Context context, FragmentManager fm, Cursor cursor) {
        super(fm);
        mImageLoader = new MultipleImageLoader(context, 0);
        mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        mCursor.moveToPosition(position);
        return TechItemFragment.newInstance(mImageLoader, new TechItem(mCursor));
    }

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }
}
