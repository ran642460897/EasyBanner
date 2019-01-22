package com.mxjapp.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * user: Jason Ran
 * date: 2018/6/11.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private int updatePagePosition=-1;

    public ViewPagerAdapter() {
    }

    public ViewPagerAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        if(views==null) return 0;
        else if(views.size()<=1) return views.size();
        else return Integer.MAX_VALUE;
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
        return views.get(position%views.size());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
         if(views.size()>3)container.removeView(views.get(position%views.size()));
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        if(updatePagePosition>0&&updatePagePosition<views.size()){
            if(object==views.get(updatePagePosition)) return POSITION_NONE;
            else return POSITION_UNCHANGED;
        }else return POSITION_NONE;

    }
    public void updateData(List<View> views) {
        updatePagePosition=-1;
        this.views = views;
        notifyDataSetChanged();
    }

    public void setUpdatePagePosition(int updatePagePosition) {
        this.updatePagePosition = updatePagePosition;
    }
}
