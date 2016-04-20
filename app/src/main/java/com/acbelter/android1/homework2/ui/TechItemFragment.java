package com.acbelter.android1.homework2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.TechItem;
import com.acbelter.android1.homework2.imgloader.ImageLoader;

public class TechItemFragment extends Fragment {
    private ImageLoader mImageLoader;
    private TechItem mItem;

    public static TechItemFragment newInstance(ImageLoader imageLoader, TechItem item) {
        TechItemFragment fragment = new TechItemFragment();
        fragment.mImageLoader = imageLoader;
        fragment.mItem = item;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.pager_item_tech, container, false);

        ImageView fullImage = (ImageView) itemView.findViewById(R.id.full_image);
        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView info = (TextView) itemView.findViewById(R.id.info);

        int fullImageMargin = (int) getResources().getDimension(R.dimen.full_image_margin);
        int requiredSize = container.getMeasuredWidth() - 2 * fullImageMargin;
        mImageLoader.setRequiredSize(requiredSize, requiredSize);

        mImageLoader.loadImage(mItem.getPictureUrl(), fullImage);
        title.setText(mItem.title);
        info.setText(mItem.info);

        return itemView;
    }
}
