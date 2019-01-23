package com.mxjapp.banner;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * user: Jason Ran
 * date: 2019/1/23.
 */
public abstract class BannerAdapter<T> {
    List<T> data;

    public BannerAdapter(List<T> data) {
        this.data = data;
    }

    public BannerAdapter() {
        this.data=new ArrayList<>();
    }
    public abstract void onImageLoad(ImageView imageView,T object);
    public abstract void onItemClick(View imageView, T object);
}
