package com.example.lorebase.ui.fragment.subFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lorebase.R;

/**
 * A simple {@link Fragment} subclass.
 * homeFragment中的第三级Fragment
 * 获取对应题目的内容，分级显示，本级(MItem2Fragment)获取到具体内容
 * Bundle 传递数据
 */
public class MItem2Fragment extends Fragment implements View.OnClickListener {
    private TextView content;
    private ImageButton back;
    private View view;

    public MItem2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mitem2,null) ;
        initView();
        return view;
    }

    private void initView() {
        content = view.findViewById(R.id.article_content);
        back = view.findViewById(R.id.mit2_back_mit1);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager= getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WeChatFragment mif1 = new WeChatFragment();

        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter).
                replace(R.id.content_layout,mif1).
                addToBackStack(null);
        transaction.commit();
    }
}
