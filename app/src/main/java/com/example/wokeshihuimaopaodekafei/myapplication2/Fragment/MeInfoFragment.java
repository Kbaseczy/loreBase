package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;


import android.annotation.SuppressLint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.wokeshihuimaopaodekafei.myapplication2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeInfoFragment extends Fragment {
    View view;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ImageButton back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_me_info, container, false);
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        back = (ImageButton) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.main_ViewPager,new MeFragment()); //replace实现返回上一层效果
                //addToBackStack(null);   当前界面加入回退栈
                transaction.commit();
            }
        });
        return view;
    }


}
