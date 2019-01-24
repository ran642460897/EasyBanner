package com.mxjapp.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * user: Jason Ran
 * date: 2019/1/22.
 */
public class EasyBanner extends FrameLayout{
    public final static int MESSAGE_UPDATE=1000;
    private CustomViewPager viewPager;
    private RadioGroup indicatorContainer;
    private List<RadioButton> indicators;
    private int indicatorRes;
    private int indicatorLayoutGravity;
    private int indicatorLayoutMargins[]=new int[4];//start,top,end,bottom
    private int indicatorDivider;
    private int initialPosition=0;
    private int interval;
    private BannerAdapter adapter;
    private ViewPagerAdapter viewPagerAdapter;

    private UpdateHandler updateHandler;
    private Handler picHandler;
    private Runnable picRunnable;
    private boolean handlerLooping=false;
    public EasyBanner(@NonNull Context context) {
        this(context,null);
    }

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initUpdateHandler();
        initView();
    }
    private void initAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyBanner);
        indicatorDivider=typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicatorDivider,0);
        indicatorLayoutGravity=typedArray.getInteger(R.styleable.EasyBanner_indicatorLayoutGravity,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        indicatorRes=typedArray.getResourceId(R.styleable.EasyBanner_indicator,R.drawable.selector_ic_indicator);
        interval=typedArray.getInteger(R.styleable.EasyBanner_interval,5000);
        indicatorLayoutMargins[0]=typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicatorLayoutMarginStart,0);
        indicatorLayoutMargins[1]=typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicatorLayoutMarginTop,0);
        indicatorLayoutMargins[2]=typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicatorLayoutMarginEnd,0);
        indicatorLayoutMargins[3]=typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicatorLayoutMarginBottom,0);
        typedArray.recycle();
    }
    private void initUpdateHandler(){
        updateHandler=new UpdateHandler(this);
    }
    private void initView(){
        //view pager
        viewPager=new CustomViewPager(getContext());
        viewPagerAdapter=new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(indicators!=null&&indicators.size()>1) indicators.get(i%indicators.size()).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i){
                    case 1://start moving
                        stopLooping();
                        break;
                    case 2://end moving
                        startLooping();
                        break;
                }
            }
        });
        addView(viewPager,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        //indicator
        indicatorContainer = new RadioGroup(getContext());
        LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= indicatorLayoutGravity;
        params.setMargins(indicatorLayoutMargins[0],indicatorLayoutMargins[1],indicatorLayoutMargins[2],indicatorLayoutMargins[3]);
        addView(indicatorContainer,params);
    }
    @SuppressWarnings("unchecked")
    private void updateViewPager(){
        if(adapter==null) return;
        int preViewSize=viewPagerAdapter.getViews().size();
        int curViewSize=adapter.getSize()==2||adapter.getSize()==3?adapter.getSize()*2:adapter.getSize();

        if(preViewSize>0){
            int relPosition=curViewSize>1?viewPager.getCurrentItem()%curViewSize:viewPager.getCurrentItem()%preViewSize;
            for(int i=relPosition;i>0;i--){
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1,false);
            }
        }

        if(curViewSize>preViewSize) {
            for (int i = 0; i < curViewSize - preViewSize; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewPagerAdapter.addItem(imageView);
            }
        }else if(curViewSize<preViewSize){
            for(int i=preViewSize-1;i>curViewSize-1;i--) viewPagerAdapter.removeItem(i);
        }

        for(int j=0;j<viewPagerAdapter.getRealSize();j++){
            final int position=j%adapter.getSize();
            adapter.onImageLoad(viewPagerAdapter.getItem(j),adapter.getItem(position));
            viewPagerAdapter.getItem(j).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.onItemClick(v,adapter.getItem(position));
                }
            });
        }
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setScrollable(adapter.getSize()>1);
        if(viewPager.getCurrentItem()==0&&preViewSize==0&&curViewSize>1) viewPager.setCurrentItem(100*viewPagerAdapter.getRealSize()+initialPosition,false);
    }
    private void updateIndicatorView(){
        indicators = new ArrayList<>();
        indicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
        if(indicatorContainer.getChildCount()>0) indicatorContainer.removeAllViews();
        if(adapter!=null) {
            for(int i=0;i<adapter.getSize();i++) {
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setButtonDrawable(indicatorRes);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if(i>0) params.setMarginStart(indicatorDivider);
                indicatorContainer.addView(radioButton,params);
                indicators.add(radioButton);

                if(initialPosition==i) radioButton.setChecked(true);
            }

            if(adapter.getSize()==1) indicatorContainer.removeAllViews();//when only one page
        }
    }

    private void startLooping(){
        if(picHandler==null) picHandler=new Handler();
        if(picRunnable==null) picRunnable=new Runnable() {
            @Override
            public void run() {
                handlerLooping=false;
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        };
        if(!handlerLooping) {
            picHandler.postDelayed(picRunnable, interval);
            handlerLooping=true;
        }
    }
    private void stopLooping(){
        if(picHandler!=null&&picRunnable!=null){
            handlerLooping=false;
            picHandler.removeCallbacks(picRunnable);
        }
    }
    public void restart(){
        startLooping();
    }
    public void stop(){
        startLooping();
    }
    protected void update(){
        Log.i("sssssssssssss","update");
        stopLooping();
        if(adapter==null) return;
        if(adapter.getHandler()==null) adapter.bindHandler(updateHandler);
        updateViewPager();
        updateIndicatorView();
        if(adapter.getSize()>1) startLooping();
        setVisibility(adapter.getSize()==0?GONE:VISIBLE);
    }

    public void setAdapter(BannerAdapter adapter) {
        this.adapter = adapter;
        update();
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    static class UpdateHandler extends Handler{
        private WeakReference<EasyBanner> reference;
        UpdateHandler(EasyBanner view){
            reference=new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==MESSAGE_UPDATE) reference.get().update();
        }
    }
}
