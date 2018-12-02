package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.HomeTabAdapter;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.News;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.NavigationActivity;
import com.example.lorebase.ui.activity.ProjectActivity;
import com.example.lorebase.ui.fragment.subFragment.HomeTabListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * 1发出请求request——>2得到响应response——>3解析数据——>4List容纳数据——>5适配器适配到recyclerView
 */
public class HomeFragment extends Fragment {
    private View view;

    private int page = 0;

    private List<Banner.DataBean> banner_t;
    private List<News.DataBean> beanList_news;

    private SliderLayout sliderLayout;

    private ViewFlipper viewFlipper;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        sliderLayout = view.findViewById(R.id.slide_layout);
        getBanner();  //include initBanner()
        initViewPager(); //tab :latest article/project
        initFlipper();
        getFlipper();
        initTable();
        return view;
    }

    private void initSlider() {
        //遍历bannerList,取出数据，填充到textSliderView.  textSliderView监听轮播图点击事件
        if (banner_t != null) {
            for (final Banner.DataBean banner : banner_t) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImagePath());
                textSliderView.description(banner.getTitle());
                textSliderView.setOnSliderClickListener(slider -> {
                    MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(
                            new BrowseHistory(null, banner.getTitle(), banner.getUrl(), null));
                    Intent web_intent = new Intent(getActivity(), AgentWebActivity.class);

                    Uri uri = Uri.parse(banner.getUrl());
                    web_intent.setData(uri);    //1.setData()传url地址
                    web_intent.putExtra(ConstName.TITLE, banner.getTitle());
                    startActivity(web_intent);
                });
                sliderLayout.addSlider(textSliderView);  //添加每一个banner
            }
        }
        sliderLayout.setCustomIndicator(view.findViewById(R.id.custom_indicator)); //指示器默认
        sliderLayout.setDuration(3000);//每个banner持续时间3s
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut); // transfer animation
    }

    private void getBanner() {
        String url = UrlContainer.baseUrl + UrlContainer.MAIN_BANNER;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Gson gson = new Gson();
                        //右边是解析成javaBean,右边是从javabean取出list，整体存到banner_t
                        banner_t = gson.fromJson(response, Banner.class).getData();
                        initSlider();
                    }
                });
    }

    private void initViewPager() {
        TabLayout tab = view.findViewById(R.id.tab_home);

        ViewPager viewPager = view.findViewById(R.id.viewPager_home);
        String title[] = {"article", "project"};
        for (String aTitle : title) {
            tab.addTab(tab.newTab().setText(aTitle));
        }

        List<Fragment> fragments = new ArrayList<>();
        String url_article = UrlContainer.baseUrl + "article/list/" + page + "/json";
        String url_project = UrlContainer.baseUrl + "article/listproject/" + page + "/json";
        String url[] = {url_article, url_project};
        for (String urla : url) {
            fragments.add(HomeTabListFragment.newInstance(urla));
        }
        HomeTabAdapter adapter = new HomeTabAdapter(getFragmentManager(), fragments, title);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(viewPager);
    }

    private void initFlipper() {
        viewFlipper = view.findViewById(R.id.flipper);
        ImageView flipper_image = view.findViewById(R.id.flipper_image);
        Glide.with(this).load(R.drawable.icon_news).into(flipper_image);
    }

    private void getFlipper() {
        String url = UrlContainer.baseUrl + UrlContainer.FRIEND;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        beanList_news = gson.fromJson(response, News.class).getData();
                        setData();
                    }
                });
    }

    private void setData() {
        for (News.DataBean t : beanList_news) {
            LinearLayout item_view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_viewflipper, null);
            TextView content_tv = item_view.findViewById(R.id.viewflipper_content);
            content_tv.setText(t.getName());
            content_tv.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AgentWebActivity.class);
                intent.setData(Uri.parse(t.getLink()));
                intent.putExtra(ConstName.TITLE, t.getName());
                startActivity(intent);
            });
            viewFlipper.addView(item_view);
        }
    }

    private void initTable(){
        LinearLayout project = view.findViewById(R.id.view_project);
        LinearLayout navigation = view.findViewById(R.id.view_navigation);
        ImageView image_project = view.findViewById(R.id.image_project);
        ImageView image_navigation = view.findViewById(R.id.image_navigation);

        Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.icon_project).into(image_project);
        Glide.with(getActivity()).load(R.drawable.icon_navigation).into(image_navigation);

        project.setOnClickListener(v -> startActivity(new Intent(getActivity(),ProjectActivity.class)));
        navigation.setOnClickListener(v -> startActivity(new Intent(getActivity(),NavigationActivity.class)));
    }
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        sliderLayout.stopAutoCycle();
        if (viewFlipper != null)
            viewFlipper = null;
        super.onStop();
    }

}
