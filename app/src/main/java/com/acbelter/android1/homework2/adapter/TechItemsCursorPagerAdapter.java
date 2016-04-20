package com.acbelter.android1.homework2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.TechItem;
import com.acbelter.android1.homework2.imgloader.ImageLoader;
import com.acbelter.android1.homework2.imgloader.MultipleImageLoader;

public class TechItemsCursorPagerAdapter extends PagerAdapter {
    private ImageLoader mImageLoader;
    private Cursor mCursor;
    private LayoutInflater mInflater;
    private int mFullImageMargin;
    private boolean mIsRequiredSizeChanged;

    public TechItemsCursorPagerAdapter(Context context, Cursor cursor) {
        mInflater = LayoutInflater.from(context);
        mImageLoader = new MultipleImageLoader(context, 0);
        mFullImageMargin = (int) context.getResources().getDimension(R.dimen.full_image_margin);
        mCursor = cursor;
    }

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mInflater.inflate(R.layout.pager_item_tech, container, false);
        ImageView fullImage = (ImageView) itemView.findViewById(R.id.full_image);
        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView info = (TextView) itemView.findViewById(R.id.info);

        mCursor.moveToPosition(position);
        TechItem item = new TechItem(mCursor);

        if (!mIsRequiredSizeChanged) {
            int requiredSize = container.getMeasuredWidth() - 2 * mFullImageMargin;
            mImageLoader.setRequiredSize(requiredSize, requiredSize);
            mIsRequiredSizeChanged = true;
        }
        mImageLoader.loadImage(item.getPictureUrl(), fullImage);
        title.setText(item.title);
        info.setText(item.info);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}