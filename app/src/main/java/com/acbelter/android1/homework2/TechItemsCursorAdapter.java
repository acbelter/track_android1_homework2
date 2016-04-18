package com.acbelter.android1.homework2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acbelter.android1.homework2.db.DbHelper;
import com.acbelter.android1.homework2.imgloader.ImageLoader;
import com.acbelter.android1.homework2.imgloader.MultipleImageLoader;

public class TechItemsCursorAdapter extends CursorAdapter {
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;

    public TechItemsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mImageLoader = new MultipleImageLoader(context, R.drawable.ic_settings_black_48dp);
        mImageLoader.setRequiredSizeDimens(R.dimen.preview_image_size, R.dimen.preview_image_size);
        mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        ImageView preview;
        TextView title;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_tech, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.preview = (ImageView) view.findViewById(R.id.preview);
        holder.title = (TextView) view.findViewById(R.id.title);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(DbHelper.INDEX_TITLE));
        String pictureUrl = cursor.getString(DbHelper.INDEX_PICTURE);
        mImageLoader.loadImage(Api.BASE_URL + pictureUrl, holder.preview);
    }
}
