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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        EasyBanner bannerView=findViewById(R.id.banner);
        List<String> list=new ArrayList<>();
//        list.add(R.drawable.pc_1);
//        list.add(R.drawable.pc_2);
//        list.add(R.drawable.pc_3);
//        list.add(R.drawable.pc_4);
//        list.add(R.drawable.pc_5);
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133899&di=9c17b21cbf5025523a298c0010cf19c7&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F267f9e2f070828380b781339b399a9014c08f1a2.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133898&di=3aee968ade37e9708f9d04e8a178656a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fb17eca8065380cd7cd1c95c3aa44ad3459828199.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548221133898&di=61baa1f3633212c558c892d1dfeddabf&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01dc955879de99a801219c77cca4a2.png%401280w_1l_2o_100sh.png");

        BannerAdapter<String> adapter=new BannerAdapter<String>() {
            @Override
            public void onImageLoad(ImageView imageView, String object) {
                Picasso.with(getApplicationContext()).load(object).placeholder(R.drawable.pc_default).into(imageView);
            }

            @Override
            public void onItemClick(View imageView, String object) {
                Log.i("ssssssssss", object);
            }
        };
        bannerView.setAdapter(adapter).init();
    }
}
