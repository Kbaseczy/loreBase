package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lorebase.R;
import com.example.lorebase.adapter.ProjectLatestAdapter;
import com.example.lorebase.bean.ProjectLatest;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.ui.fragment.subFragment.AddCategoryFragment;
import com.example.lorebase.ui.fragment.subFragment.WeChatFragment;
import com.example.lorebase.ui.fragment.subFragment.ModifyCategoryFragment;
import com.example.lorebase.util.L;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * 1发出请求request——>2得到响应response——>3解析数据——>4List容纳数据——>5适配器适配到recyclerView
 */
public class HomeFragment extends Fragment{
    private View view;

    private List<ProjectLatest.DataBean.DatasBean> list_project = new ArrayList<>();

    private int page = 0;

    private List<Banner.DataBean> banner_t;

    SliderLayout sliderLayout;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        sliderLayout = view.findViewById(R.id.slide_layout);
        getBanner();  //include initBanner()
        getProject();  //include initWeChar()
        return view;
    }

    private void getProject(){
        //开始获取不到数据，嘻嘻~~URL地址写混淆 / url页码拼接问题
        String url = UrlContainer.baseUrl+"article/listproject/"+page+"/json";
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
                        //TODO :Expected Object but Array -> ok
                        list_project = gson.fromJson(response, ProjectLatest.class).getData().getDatas();

                        initProject();
                    }
                });
    }
    private void initProject() {
        RecyclerView project_recycler = view.findViewById(R.id.home_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);

        project_recycler.setLayoutManager(layoutManager);

        ProjectLatestAdapter projectLatestAdapter = new ProjectLatestAdapter(list_project);

        project_recycler.setAdapter(projectLatestAdapter);

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.swipe_refresh);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary
                                             ,R.color.colorAccent
                                             ,R.color.colorPrimaryDark
                                             ,R.color.Green); //todo 刷新的颜色改变

//               //下拉刷新 有bug  todo 刷新暂时去掉
//        refreshLayout.setOnRefreshListener(() -> {
//            getProject();
//            projectLatestAdapter.notifyDataSetChanged();
//            refreshLayout.setRefreshing(false);
//        });

        //上拉加载
        project_recycler.addOnScrollListener(new EndlessOnScrollListener(layoutManager) {
            @Override
            void onLoadMore(int current_page) {
                page ++;     //页码加1，  todo 这里是否要去控制page的页码上限
                getProject();
            }
        });
    }
    private void initSlider(){
        //遍历bannerList,取出数据，填充到textSliderView.  textSliderView监听轮播图点击事件
        if(banner_t != null){
            for(final Banner.DataBean banner:banner_t){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView.image(banner.getImagePath());
                textSliderView.description(banner.getTitle());
                textSliderView.setOnSliderClickListener(slider -> {
                    Log.v("banner->AgentWeb","click test");
                    Intent web_intent = new Intent(getActivity(),AgentWebActivity.class);

                    Uri uri = Uri.parse(banner.getUrl()) ;
                    web_intent.setData(uri);    //1.setData()传url地址
                    web_intent.putExtra(ConstName.TITLE,banner.getTitle());
                    startActivity(web_intent);
                });
                sliderLayout.addSlider(textSliderView);  //添加每一个banner
            }
        }
        sliderLayout.setCustomIndicator( view.findViewById(R.id.custom_indicator)); //指示器默认
        sliderLayout.setDuration(3000);//每个banner持续时间3s
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack); // transfer animation
    }
    private void getBanner(){
        String url = UrlContainer.baseUrl+UrlContainer.MAIN_BANNER;
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
//                        Log.v("Banner~~~~~~~~~~",response);

                        Gson gson  = new Gson();
                        //右边是解析成javaBean,右边是从javabean取出list，整体存到banner_t
                        banner_t = gson.fromJson(response,Banner.class).getData();
//                        for(Banner.DataBean banner1:banner_t){
//                            Log.v("Bean取出List",banner1.getTitle());
//                        }
                        initSlider();
                    }
                });
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener{

        private LinearLayoutManager mLinearLayoutManager;

        //当前页，从第一页开始
        private int current_page;

        //已加载出来的item总数
        private int total_ItemCount;

        //存储上一个total_ItemCount
        private int previousTotal = 0;

        //可见的item数
        private int visibleItemCount;

        //屏幕中第一个可见item
        private int firstVisibleItem;

        //是否加载数据
        private boolean loading = true;

        EndlessOnScrollListener(LinearLayoutManager linearLayoutManager){
            this.mLinearLayoutManager = linearLayoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            visibleItemCount = recyclerView.getChildCount();
            total_ItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if(loading){
                if(total_ItemCount > previousTotal){

                    loading = false;  //数据加载结束
                    previousTotal = total_ItemCount;
                }
            }

            if(!loading && total_ItemCount - visibleItemCount <= firstVisibleItem){
                current_page ++;
                onLoadMore(current_page);
                loading = true;
            }
        }

        abstract void onLoadMore(int current_page) ;
    }

}
