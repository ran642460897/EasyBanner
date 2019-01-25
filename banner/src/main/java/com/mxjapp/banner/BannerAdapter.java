package com.mxjapp.banner;

import android.os.Message;
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
    private EasyBanner.UpdateHandler handler;

    public BannerAdapter(List<T> data) {
        this.data = data;
    }

    public BannerAdapter() {
        this.data=new ArrayList<>();
    }
    public void updateData(List<T> list,boolean clear){
        if(clear) data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }
    public void updateData(List<T> list){
        updateData(list,true);
    }
    public abstract void onImageLoad(ImageView imageView,T object);
    public abstract void onItemClick(View imageView, T object);
    public T getItem(int position){
        return data!=null&&data.size()>position?data.get(position):null;
    }
    public int getSize(){
        return data!=null?data.size():0;
    }

    public List<T> getData() {
        return data;
    }

    public void notifyDataSetChanged(){
        if(handler!=null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = EasyBanner.MESSAGE_UPDATE;
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }
    void bindHandler(EasyBanner.UpdateHandler handler){
        this.handler=handler;
    }
    EasyBanner.UpdateHandler getHandler(){
        return handler;
    }
}
