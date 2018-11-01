package com.example.lorebase.ui.fragment.subFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.SpaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * in the spaceFragment
 */
public class PersonalFragment extends Fragment {
    private ImageButton back;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        View view = inflater.inflate(R.layout.fragment_personal, null);
        back = view.findViewById(R.id.backToSpace);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction.setCustomAnimations(
                        R.animator.fragment_slide_left_enter,
                        R.animator.fragment_slide_left_exit,
                        R.animator.fragment_slide_right_exit,
                        R.animator.fragment_slide_right_enter).
                        replace(R.id.content_layout, new SpaceFragment());  //replace实现返回上一层效果
                        //addToBackStack(null); 当前界面加入回退栈
                transaction.commit();
                getActivity().onBackPressed();
            }
        });

        return view;
    }
    }



