package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.bean.ProjectChapter;
import com.example.lorebase.http.RetrofitApi;
import com.example.lorebase.ui.fragment.subFragment.ItemProjectFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Callback;
import retrofit2.Response;

/*
    和LoreActivity 共用的toolBar_tab_lore布局
 */
public class ProjectFragment extends Fragment {

    private List<ProjectChapter.DataBean> beanList_chapter;
    private View view;

    public static ProjectFragment getInstance() {
        ProjectFragment fragment = new ProjectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_project, null);
        getProjectChapter();
        return view;
    }

    private void getProjectChapter() {
        RetrofitApi api = MyApplication.retrofit.create(RetrofitApi.class);
        retrofit2.Call<ProjectChapter> chapterCall = api.getProjectChapter();
        chapterCall.enqueue(new Callback<ProjectChapter>() {
            @Override
            public void onResponse(retrofit2.Call<ProjectChapter> call, Response<ProjectChapter> response) {
                if (response.body() != null) {
                    beanList_chapter  = response.body().getData();
                     initViewPager();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ProjectChapter> call, Throwable t) {

            }
        });
    }

    private void initViewPager() {
        ViewPager viewPager = view.findViewById(R.id.vp_project);
        TabLayout tabLayout = view.findViewById(R.id.tab_lore_title_project);
        for (ProjectChapter.DataBean project : beanList_chapter) {
            tabLayout.addTab(tabLayout.newTab().setText(project.getName()));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (ProjectChapter.DataBean project : beanList_chapter) {
            fragments.add(new ItemProjectFragment().instance(project.getId())); //todo ★point
        }

        FragmentStatePagerAdapter fragmentAdapterProjectList =
                new FragmentStatePagerAdapter(getFragmentManager()) {
                    @Override
                    public Fragment getItem(int position) {
                        return fragments.get(position);
                    }

                    @Override
                    public int getCount() {
                        return fragments.size();
                    }

                    @Nullable
                    @Override
                    public CharSequence getPageTitle(int position) {
                        return beanList_chapter.get(position).getName();
                    }
                };
        viewPager.setAdapter(fragmentAdapterProjectList);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }

}
