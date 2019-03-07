package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.subFragment.RelaxListFragment;
import com.example.lorebase.util.L;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 *
 * 1.设置自定义View 失败
 * 2.视频播放不出
 */
public class RelaxFragment extends Fragment {
    private View view;

    private int images[] = {R.drawable.icon_tab, R.drawable.icon_tab2, R.drawable.icon_tab3};
    private String[] title = {"1", "2", "3"};

    public static RelaxFragment getInstance() {
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

//        tabLayout.post(() -> IndicatorLineUtil.setIndicator(tabLayout, 40, 40));

        int[] identity = {0, 1, 2};
        for (int i=0;i<title.length;i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        for (int i = 0; i < title.length; i++) {
//            tabLayout.addTab(tabLayout.newTab());
//            tabLayout.getTabAt(i).setCustomView(customTab(i));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int ident : identity) {
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

    private View customTab(int position) {
        L.v("customTab","运行勒吗。");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        ImageView imageView = view.findViewById(R.id.custom_tab_image);
        TextView textView = view.findViewById(R.id.custom_tab_title);
//        imageView.setImageResource(images[position]);
        Glide.with(getActivity()).load(images[position]).into(imageView);
        textView.setText(title[position]);
        return view;
    }
}
