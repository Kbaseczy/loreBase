package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.adapter.FragmentAdapterWeChat;
import com.example.lorebase.bean.WeChat;
import com.example.lorebase.ui.fragment.subFragment.RelaxListFragment;
import com.example.lorebase.ui.fragment.subFragment.WeChatArticleFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelaxFragment extends Fragment {
    private View view;

    public static RelaxFragment getInstantce() {
        RelaxFragment fragment = new RelaxFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_relax, null);
        initRelax();
        return view;
    }

    private void initRelax() {
        ViewPager viewPager = view.findViewById(R.id.viewpager_relax);
        TabLayout tabLayout = view.findViewById(R.id.tab_relax);

        String[] title = {"category1","category2","category3"};
        int[] identity = {0,1,2};
        for (String aTitle : title) {
            tabLayout.addTab(tabLayout.newTab().setText(aTitle));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int ident:identity) {
            fragments.add(RelaxListFragment.getInstance(ident));
        }

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }
}
