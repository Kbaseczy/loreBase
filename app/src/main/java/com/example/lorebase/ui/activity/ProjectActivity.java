package com.example.lorebase.ui.activity;

import android.os.Bundle;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.adapter.FragmentAdapterProjectList;
import com.example.lorebase.bean.ProjectChapter;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.ItemProjectFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

/*
    和LoreActivity 共用的toolBar_tab_lore布局
 */
public class ProjectActivity extends BaseActivity {

    private ViewPager viewPager;
    private List<ProjectChapter.DataBean> beanList_chapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initView();
        getProjectChapter();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_lore);
        toolbar.setTitle(R.string.project);
        toolbar.setNavigationOnClickListener(v->finish());
        viewPager = findViewById(R.id.vp_project);
    }

    private void getProjectChapter(){
        String url = UrlContainer.baseUrl+UrlContainer.PROJECT;
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
                        beanList_chapter = gson.fromJson(response, ProjectChapter.class).getData();
                        initViewPager();
                    }
                });
    }

    private void initViewPager(){
        TabLayout tabLayout = findViewById(R.id.tab_lore_title);
        for (ProjectChapter.DataBean project : beanList_chapter) {
            tabLayout.addTab(tabLayout.newTab().setText(project.getName()));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (ProjectChapter.DataBean project : beanList_chapter) {
            fragments.add(new ItemProjectFragment().instance(project.getId())); //todo ★point
        }

        FragmentAdapterProjectList fragmentAdapterProjectList =
                new FragmentAdapterProjectList(getSupportFragmentManager(),fragments,beanList_chapter);
        viewPager.setAdapter(fragmentAdapterProjectList);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }
}
