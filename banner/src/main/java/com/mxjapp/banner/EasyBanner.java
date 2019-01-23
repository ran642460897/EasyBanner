package com.mxjapp.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * user: Jason Ran
 * date: 2019/1/22.
 */
public class EasyBanner extends FrameLayout{
    private CustomViewPager viewPager;
    private List<RadioButton> indicators;
    private int indicatorRes;
    private int indicatorLayoutGravity;
    private int indicatorLayoutMargins[]=new int[4];//start,top,end,bottom
    private int indicatorDivider;
    private int initialPosition=0;
    private int interval;
    private BannerAdapter adapter; 

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
    public void init(){
        if(adapter==null) throw new NullPointerException();
        initView();
        if(adapter.getSize()>1) startHandler();
    }
    private void initView(){
        initViewPager();
        initIndicatorView();
    }
    private void initViewPager(){
        List<View> views=new ArrayList<>();
        if(adapter!=null){
            for (int i=0;i<adapter.getSize();i++) views.add(generateImageView(i));


            if(adapter.getSize()==2||adapter.getSize()==3){//when the size is 2 or 3,add more
                for(int i=0;i<adapter.getSize();i++) views.add(generateImageView(i));
            }
        }
        viewPager=new CustomViewPager(getContext());
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setScrollable(views.size()>1);
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
                        stopHandler();
                        break;
                    case 2://end moving
                        startHandler();
                        break;
                }
            }
        });
        if(adapter.data!=null) {
            viewPager.setCurrentItem(1000*adapter.data.size()+initialPosition,false);
        }
        addView(viewPager,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }
    private void initIndicatorView(){
        indicators = new ArrayList<>();
        RadioGroup indicatorContainer = new RadioGroup(getContext());
        indicatorContainer.setOrientation(LinearLayout.HORIZONTAL);
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
        LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity= indicatorLayoutGravity;
        params.setMargins(indicatorLayoutMargins[0],indicatorLayoutMargins[1],indicatorLayoutMargins[2],indicatorLayoutMargins[3]);
        addView(indicatorContainer,params);
    }
    @SuppressWarnings("unchecked")
    private ImageView generateImageView(final int position){
        ImageView imageView=new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter!=null) adapter.onItemClick(v,adapter.getItem(position));
            }
        });

        if(adapter!=null) adapter.onImageLoad(imageView,adapter.getItem(position));
        return imageView;
    }
    private void startHandler(){
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
    private void stopHandler(){
        if(picHandler!=null&&picRunnable!=null){
            handlerLooping=false;
            picHandler.removeCallbacks(picRunnable);
        }
    }
    public void restart(){
        startHandler();
    }
    public void stop(){
        stopHandler();
    }

    public EasyBanner setAdapter(BannerAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
