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
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.Article;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.bean.News;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.activity.NavigationActivity;
import com.example.lorebase.ui.activity.ProjectActivity;
import com.example.lorebase.util.DividerItemGridDecoration;
import com.scwang.smartrefresh.header.DropboxHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Banner.DataBean> banner_t = new ArrayList<>();
    private List<News.DataBean> beanList_news = new ArrayList<>();
    private List<Article.DataBean.DatasBean> beanList_article = new ArrayList<>();
    private Context context;

    public HomeAdapter(Context context, List<Banner.DataBean> banner_t, List<News.DataBean> beanList_news,
                       List<Article.DataBean.DatasBean> beanList_article) {
        this.context = context;
        this.banner_t = banner_t;
        this.beanList_news = beanList_news;
        this.beanList_article = beanList_article;
    }

    public HomeAdapter(Context context){
        this.context = context;
    }
    public void addList(List<Banner.DataBean> banner_t, List<News.DataBean> beanList_news,
                        List<Article.DataBean.DatasBean> beanList_article) {
        this.banner_t.addAll(banner_t);
        this.beanList_news.addAll(beanList_news);
        this.beanList_article.addAll(beanList_article);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.banner_t.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        this.banner_t.clear();
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

        private void stop() {
            sliderLayout.stopAutoCycle();
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

        private void stop() {
            if (viewFlipper != null)
                viewFlipper = null;
        }
    }

    public static class Holder_article extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        SmartRefreshLayout smartRefreshLayout;

        Holder_article(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.lore_rv);
            smartRefreshLayout = view.findViewById(R.id.smart_refresh_lore);
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
                    MyApplication.getDaoSession().getBrowseHistoryDao().insertOrReplace(
                            new BrowseHistory(null, banner.getTitle(), banner.getUrl(), null));
                    Intent web_intent = new Intent(context, AgentWebActivity.class);

                    Uri uri = Uri.parse(banner.getUrl());
                    web_intent.setData(uri);    //1.setData()传url地址
                    web_intent.putExtra(ConstName.TITLE, banner.getTitle());
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

        Glide.with(Objects.requireNonNull(context)).load(R.drawable.icon_project).into(((Holder_tab) holder).image_project);
        Glide.with(context).load(R.drawable.icon_navigation).into(((Holder_tab) holder).image_navigation);

        ((Holder_tab) holder).project.setOnClickListener(v -> context.startActivity(new Intent(context, ProjectActivity.class)));
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
                Intent intent = new Intent(context, AgentWebActivity.class);
                intent.setData(Uri.parse(t.getLink()));
                intent.putExtra(ConstName.TITLE, t.getName());
                context.startActivity(intent);
            });
            ((Holder_flipper) holder).viewFlipper.addView(item_view);
        }
    }

    //bind article data
    private void initArticle(RecyclerView.ViewHolder holder) {

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        HomeListAdapter homeListAdapter = new HomeListAdapter(beanList_article);
        ((Holder_article) holder).recyclerView.setLayoutManager(layoutManager);
        ((Holder_article) holder).recyclerView.setAdapter(homeListAdapter);
        ((Holder_article) holder).recyclerView.clearOnScrollListeners();
        ((Holder_article) holder).recyclerView.addItemDecoration(new DividerItemGridDecoration(context));
//        ((Holder_article) holder).recyclerView.setNestedScrollingEnabled(true);
        ((Holder_article) holder).smartRefreshLayout.setRefreshHeader(new DropboxHeader(context));
        ((Holder_article) holder).smartRefreshLayout.setRefreshFooter(new BallPulseFooter(context));
//        ((Holder_article) holder).smartRefreshLayout.autoRefresh();
//        ((Holder_article) holder).smartRefreshLayout.autoLoadMore();
    }


}
