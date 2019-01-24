package com.mxjapp.easybanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mxjapp.banner.BannerAdapter;
import com.mxjapp.banner.EasyBanner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BannerAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        EasyBanner bannerView=findViewById(R.id.banner);
        List<String> list=new ArrayList<>();
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133899&di=9c17b21cbf5025523a298c0010cf19c7&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F267f9e2f070828380b781339b399a9014c08f1a2.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133898&di=3aee968ade37e9708f9d04e8a178656a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fb17eca8065380cd7cd1c95c3aa44ad3459828199.jpg");
//        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133898&di=61baa1f3633212c558c892d1dfeddabf&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01dc955879de99a801219c77cca4a2.png%401280w_1l_2o_100sh.png");
//        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412229&di=f48c18ba189f63c8dfabaa6e3371dcc3&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01a19556e25d4d6ac72531cb68ffe2.jpg");
        adapter=new BannerAdapter<String>(list) {
            @Override
            public void onImageLoad(ImageView imageView, String object) {
                Picasso.with(getApplicationContext()).load(object).placeholder(R.drawable.pc_default).into(imageView);
            }

            @Override
            public void onItemClick(View imageView, String object) {
                Log.i("ssssssssss", object);
            }
        };
        bannerView.setAdapter(adapter);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ssssssssssssssss","click");
                List<String> list=new ArrayList<>();
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412236&di=df789ec178ba8ded5800f4982c70cdac&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01659f56d6b7f56ac7252ce679dd12.png");
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412231&di=d3fad4d0cdd05eebb9e8e58bd58cf572&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01475b570bc70932f8751b3fc3cd07.jpg");
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412230&di=1fde1e3d6bbb64c2c659a89302c931ae&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0182c559f1b6e2a801202b0c19cb2e.png");
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412230&di=97b0b373a5946af58f929a823616e29a&imgtype=0&src=http%3A%2F%2Fimages.hisupplier.com%2Fvar%2FuserImages%2F201509%2F28%2F163325331520_s.jpg");
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412228&di=f9f0abe94ca48e333dadafd38491f55b&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F010cc55604fbdf32f875a132414ca6.jpg");
//                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548319412227&di=43c1e510d02c1f6b1292f724fb80ca62&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0198dd56d937e66ac7252ce612186f.jpg");
                adapter.updateData(list);
            }
        });
    }
}
