package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.bean.ProjectChapter;
import com.example.lorebase.contain_const.UrlContainer;
import com.example.lorebase.ui.fragment.subFragment.ItemProjectFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Request;

/*
    和LoreActivity 共用的toolBar_tab_lore布局
 */
public class ProjectFragment extends Fragment {

    @BindView(R.id.tab_lore_title)
    TabLayout tabLoreTitle;
    @BindView(R.id.vp_project)
    ViewPager vpProject;
    private List<ProjectChapter.DataBean> beanList_chapter;

    public static ProjectFragment getInstance() {
        ProjectFragment fragment = new ProjectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        getProjectChapter();
        return view;
    }

    private void getProjectChapter() {
        String url = UrlContainer.baseUrl + UrlContainer.PROJECT;
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

    private void initViewPager() {
        for (ProjectChapter.DataBean project : beanList_chapter) {
            tabLoreTitle.addTab(tabLoreTitle.newTab().setText(project.getName()));
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
        vpProject.setAdapter(fragmentAdapterProjectList);
        vpProject.setOffscreenPageLimit(2);
        tabLoreTitle.setupWithViewPager(vpProject);
    }

}
