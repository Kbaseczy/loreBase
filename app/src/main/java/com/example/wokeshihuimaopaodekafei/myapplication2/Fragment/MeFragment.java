package com.example.wokeshihuimaopaodekafei.myapplication2.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wokeshihuimaopaodekafei.myapplication2.R;


public class MeFragment extends Fragment {
    LinearLayout info,exit;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mefragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        info=(LinearLayout) view.findViewById(R.id.infoMe);
        exit=(LinearLayout) getActivity().findViewById(R.id.exit);

        MeListener meListener = new MeListener();
        info.setOnClickListener(meListener);
        exit.setOnClickListener(meListener);
    }

    private class MeListener implements View.OnClickListener{

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.exit:
                    getActivity().finish(); // 这里只退出当前的Activity --> Fragment所在的Activity-MainActivity
                    //  new LoginActivity().finish();// 退出LoginActivity
                    //清除登录信息，用户名，密码
                    Toast.makeText(getActivity(), "Exit Successful.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.infoMe:
                    transaction.setCustomAnimations(
                            R.animator.fragment_slide_left_enter,
                            R.animator.fragment_slide_left_exit,
                            R.animator.fragment_slide_right_exit,
                            R.animator.fragment_slide_right_enter).
                            replace(R.id.main_ViewPager,new MeInfoFragment()).
                            addToBackStack("spaceFragment");
                    break;
            }
        }
    }
}