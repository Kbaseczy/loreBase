package com.example.lorebase.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    private Boolean is_done;
    private View view;
    public static TodoFragment getInstance(Boolean is_done) {
        TodoFragment todoFragment = new TodoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstName.IS_DONE,is_done);
        todoFragment.setArguments(bundle);
        return todoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            is_done = bundle.getBoolean(ConstName.IS_DONE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        if(is_done)
            Toast.makeText(getActivity(), "complete", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "todo todo", Toast.LENGTH_SHORT).show();
        return view;
    }

}
