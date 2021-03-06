package com.example.lorebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lorebase.MapReceiver;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.News;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.NavigationActivity;
import com.example.lorebase.ui.activity.RelaxActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.TimeUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Banner.DataBean> banner_t;
    private List<News.DataBean> beanList_news;
    private List<Article.DataBean.DatasBean> beanList_article;
    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<Banner.DataBean> banner_t, List<News.DataBean> beanList_news,
                        List<Article.DataBean.DatasBean> beanList_article) {
        this.banner_t = banner_t;
        this.beanList_news = beanList_news;
        this.beanList_article = beanList_article;
    }

    public void setBanner_t(List<Banner.DataBean> banner_t) {
        this.banner_t = banner_t;
    }

    public void setBeanList_news(List<News.DataBean> beanList_news) {
        this.beanList_news = beanList_news;
    }

    public void setBeanList_article(List<Article.DataBean.DatasBean> beanList_article) {
        this.beanList_article = beanList_article;
    }

    public void addArticle(List<Article.DataBean.DatasBean> beanList_article) {
        this.beanList_article.addAll(beanList_article);
        notifyDataSetChanged();
    }

    public void addBanner(List<Banner.DataBean> banner_t) {
        this.banner_t.addAll(banner_t);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        if (viewType == ConstName.BANNER_HOME) {
            return new Holder_banner(LayoutInflater.from(context).inflate(R.layout.layout_banner, parent, false));
        } else if (viewType == ConstName.TAB_HOME) {
            return new Holder_tab(LayoutInflater.from(context).inflate(R.layout.home_table, parent, false));
        } else if (viewType == ConstName.FLIPPER_HOME) {
            return new Holder_flipper(LayoutInflater.from(context).inflate(R.layout.layout_view_flipper, parent, false));
        } else {
            return new Holder_article(LayoutInflater.from(context).inflate(R.layout.fragment_lore_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (banner_t != null && beanList_article != null && beanList_news != null) {
            if (holder instanceof Holder_banner) {
                initBanner(holder);
            } else if (holder instanceof Holder_tab) {
                initTable(holder);
            } else if (holder instanceof Holder_flipper) {
                initFlipper(holder);
            } else {
                initArticle(holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ConstName.BANNER_HOME;
        } else if (position == 1) {
            return ConstName.TAB_HOME;
        } else if (position == 2) {
            return ConstName.FLIPPER_HOME;
        } else {
            return ConstName.ARTICLE_HOME;
        }

    }

    public static class Holder_banner extends RecyclerView.ViewHolder {
        SliderLayout sliderLayout;
        LinearLayout linearLayout;
        PagerIndicator pagerIndicator;
        View view;

        Holder_banner(View view) {
            super(view);
            linearLayout = (LinearLayout) view;
            sliderLayout = view.findViewById(R.id.slide_layout);
            pagerIndicator = view.findViewById(R.id.custom_indicator);
        }

    }

    public static class Holder_tab extends RecyclerView.ViewHolder {
        LinearLayout project, navigation;
        ImageView image_project, image_navigation;

        Holder_tab(View view) {
            super(view);
            project = view.findViewById(R.id.view_project);
            navigation = view.findViewById(R.id.view_navigation);
            image_project = view.findViewById(R.id.image_project);
            image_navigation = view.findViewById(R.id.image_navigation);
        }
    }

    public static class Holder_flipper extends RecyclerView.ViewHolder {
        ViewFlipper viewFlipper;
        ImageView flipper_image;

        Holder_flipper(View view) {
            super(view);
            viewFlipper = view.findViewById(R.id.flipper);
            flipper_image = view.findViewById(R.id.flipper_image);
        }
    }

    public static class Holder_article extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        Holder_article(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.lore_rv);
        }
    }

    //bind Banner data
    private void initBanner(RecyclerView.ViewHolder holder) {
        //遍历bannerList,取出数据，填充到textSliderView.  textSliderView监听轮播图点击事件
        if (banner_t != null) {
            for (final Banner.DataBean banner : banner_t) {
                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView.image(banner.getImagePath());
                textSliderView.description(banner.getTitle());
                textSliderView.setOnSliderClickListener(slider -> {

                    MapReceiver.getInstance().setPositionInterface((Latitude, Longitude, country, province, city, district, street) ->
                            MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                            null, banner.getTitle(), banner.getUrl(),
                            TimeUtils.date2String(new Date(System.currentTimeMillis())),
                            false, Latitude, Longitude, true, country, province, city, district, street)));

                    Intent web_intent = new Intent(context, AgentWebActivity.class);

                    Uri uri = Uri.parse(banner.getUrl());
                    web_intent.setData(uri);    //1.setData()传url地址
                    web_intent.putExtra(ConstName.TITLE, banner.getTitle());
                    web_intent.putExtra(ConstName.IS_OUT, true);
                    context.startActivity(web_intent);
                });
                ((Holder_banner) holder).sliderLayout.addSlider(textSliderView);  //添加每一个banner
            }
        }
        ((Holder_banner) holder).sliderLayout.setCustomIndicator(((Holder_banner) holder).pagerIndicator); //指示器默认
        ((Holder_banner) holder).sliderLayout.setDuration(3000);//每个banner持续时间3s
        ((Holder_banner) holder).sliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut); // transfer animation
    }

    //bind Tab data
    private void initTable(RecyclerView.ViewHolder holder) {

        Glide.with(Objects.requireNonNull(context)).load(R.drawable.ic_video).into(((Holder_tab) holder).image_project);
//        Glide.with(context).load(R.drawable.ic_navigate).into(((Holder_tab) holder).image_navigation);

        ((Holder_tab) holder).project.setOnClickListener(v -> context.startActivity(new Intent(context, RelaxActivity.class)));
        ((Holder_tab) holder).navigation.setOnClickListener(v -> context.startActivity(new Intent(context, NavigationActivity.class)));
    }

    //bind Flipper data
    private void initFlipper(RecyclerView.ViewHolder holder) {
        Glide.with(context).load(R.drawable.icon_news).into(((Holder_flipper) holder).flipper_image);
        for (News.DataBean t : beanList_news) {
            LinearLayout item_view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_viewflipper, null);
            TextView content_tv = item_view.findViewById(R.id.viewflipper_content);
            content_tv.setText(t.getName());
            content_tv.setOnClickListener(v -> {
                MapReceiver.getInstance().setPositionInterface((Latitude, Longitude, country, province, city, district, street) -> {
                    L.v("mapHomeList", Latitude + " \n" + Longitude + "  有没有啊");
                    MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(new BrowseHistory(
                            null, t.getName(), t.getLink(),
                            TimeUtils.date2String(new Date(System.currentTimeMillis())),
                            false, Latitude, Longitude,true,country, province, city, district, street));
                });
                Intent intent = new Intent(context, AgentWebActivity.class);
                intent.setData(Uri.parse(t.getLink()));
                intent.putExtra(ConstName.IS_OUT, true);
                intent.putExtra(ConstName.TITLE, t.getName());
                context.startActivity(intent);
            });
            ((Holder_flipper) holder).viewFlipper.addView(item_view);
        }
    }

    //bind article data
    private void initArticle(RecyclerView.ViewHolder holder) {

        GridLayoutManager manager = new GridLayoutManager(context, 1);
        HomeListAdapter homeListAdapter = new HomeListAdapter(beanList_article);
        ((Holder_article) holder).recyclerView.setLayoutManager(manager);
        ((Holder_article) holder).recyclerView.setAdapter(homeListAdapter);
    }

}
