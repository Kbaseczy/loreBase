package com.example.lorebase.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lorebase.BaseActivity;
import com.example.lorebase.R;
import com.example.lorebase.bean.LoreTree;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.ui.fragment.subFragment.LoreListFragment;
import com.example.lorebase.util.ToastUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

/*
        LoreTree(父级)  - cid> Lore（子级）  - > LoreListFrag(标题列表) - link,title> agentWeb
        todo question：子级中cid如何传到LoreListFragment(或者说LoreListFragment如何获取数据源)
 */
public class LoreActivity extends BaseActivity {

    Toolbar toolbar;
    String super_name;
    List<LoreTree.DataBean.ChildrenBean> childrenBean;
    LoreTree.DataBean father_bean;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lore);
        getFromTree();  //获取来自LoreTree的数据：chapterName，父级目录的子级对象（若干个）-子级对象对应着cid->cid获取知识数据
        // getLore();  //传入cid访问服务器，获取该类下的知识标题（若干项） - fragment的recyclerView的数据来源
        toolbar = findViewById(R.id.toolbar_lore);
        toolbar.setTitle(super_name);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra(ConstName.FRAGMENT, 2));
            overridePendingTransition(R.animator.go_out, R.animator.go_in);
            finish();
        });

        viewPager = findViewById(R.id.vp_lore_list);
        initViewPager(); // 初始化ViewPager，viewPager,tabLayout,Fragment 三者的关联
    }

    //从上级目录获取的数据:父级名（superChapterName）/类名（chapterName）/chapterID
    private void getFromTree() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            father_bean = (LoreTree.DataBean) bundle.getSerializable(ConstName.OBJ);
        } else {
            ToastUtil.showShortToastCenter("unexpected err.");
        }
        if (father_bean != null) {
            //父级目录进入子级目录,获取当前父级目录名
            super_name = father_bean.getName();
            //获取当前父级目录下的子级对象 "father":{"child","child","child"};存储在对应list中
            childrenBean = father_bean.getChildren();
        }
    }

    //tab+viewpager+fragment
    private void initViewPager() {
        tabLayout = findViewById(R.id.tab_lore_title);
        for (LoreTree.DataBean.ChildrenBean child : childrenBean) {
            tabLayout.addTab(tabLayout.newTab().setText(child.getName()));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (LoreTree.DataBean.ChildrenBean child : childrenBean) {
            fragments.add(new LoreListFragment().instantiate(child.getId())); //todo ★point
        }

        FragmentStatePagerAdapter fragmentAdapterLoreList = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return childrenBean != null ? childrenBean.size() : 0;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return childrenBean.get(position).getName();
            }
        };
        viewPager.setAdapter(fragmentAdapterLoreList);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }
}
