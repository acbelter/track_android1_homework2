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
    private TechItem mItem;
    private ImageLoader mImageLoader;

    public static TechItemFragment newInstance(ImageLoader imageLoader, TechItem item) {
        TechItemFragment fragment = new TechItemFragment();
        fragment.mImageLoader = imageLoader;
        fragment.mItem = item;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_item, container, false);
        ImageView fullImage = (ImageView) view.findViewById(R.id.full_image);
        TextView info = (TextView) view.findViewById(R.id.info);

        mImageLoader.loadImage(mItem.getPictureUrl(), fullImage);
        info.setText(mItem.info);

        return view;
    }
}
