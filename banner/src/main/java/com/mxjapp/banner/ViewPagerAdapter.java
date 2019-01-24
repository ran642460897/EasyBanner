package com.mxjapp.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * user: Jason Ran
 * date: 2018/6/11.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<ImageView> views;

    public ViewPagerAdapter() {
        views=new ArrayList<>();
    }

    public ViewPagerAdapter(List<ImageView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size()>1?Integer.MAX_VALUE:views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        try {
            container.addView(views.get(position%views.size()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return views.get((position)%views.size());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(views.size()==0) return;
        container.removeView(views.get((position)%views.size()));
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    public List<ImageView> getViews() {
        return views;
    }
    public void addItem(ImageView imageView){
        views.add(imageView);
    }
    public void removeItem(int position){
        if(position>=0&&position<views.size()) views.remove(position);
    }
    public int getRealSize(){
        return views.size();
    }
    public ImageView getItem(int position){
        return position>=0&&position<views.size()? views.get(position):null;
    }
}
