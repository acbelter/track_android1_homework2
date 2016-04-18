package com.acbelter.android1.homework2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.TechItem;
import com.acbelter.android1.homework2.imgloader.ImageLoader;
import com.acbelter.android1.homework2.imgloader.MultipleImageLoader;
import com.acbelter.android1.homework2.ui.TechItemFragment;

public class TechItemsCursorPagerAdapter extends FragmentStatePagerAdapter {
    private ImageLoader mImageLoader;
    private Cursor mCursor;

    public TechItemsCursorPagerAdapter(Context context, FragmentManager supportFragmentManager, Cursor cursor) {
        super(supportFragmentManager);
        this.mCursor = cursor;
        mImageLoader = new MultipleImageLoader(context, R.drawable.ic_settings_black_48dp);
    }

    @Override
    public Fragment getItem(int position) {
        if (mCursor == null) {
            return null;
        }

        mCursor.moveToPosition(position);
        TechItem item = new TechItem(mCursor);
        return TechItemFragment.newInstance(mImageLoader, item);
    }

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return;
        }

        mCursor = cursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}