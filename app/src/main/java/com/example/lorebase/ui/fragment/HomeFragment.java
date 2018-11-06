package com.example.lorebase.ui.fragment;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.lorebase.R;
import com.example.lorebase.adapter.ProjectLatestAdapter;
import com.example.lorebase.bean.ProjectLatest;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.bean.Banner;
import com.example.lorebase.ui.activity.AgentWebActivity;
import com.example.lorebase.util.EndlessOnScrollListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.FlyRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
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

        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.smart_refresh_home);

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(getContext()));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            //下拉刷新
            list_project.clear();
            getProject();
            projectLatestAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });

        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            //上拉加载
            page++; //warn：page的上限
            getProject();
            projectLatestAdapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
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

                        Gson gson  = new Gson();
                        //右边是解析成javaBean,右边是从javabean取出list，整体存到banner_t
                        banner_t = gson.fromJson(response,Banner.class).getData();
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

}
