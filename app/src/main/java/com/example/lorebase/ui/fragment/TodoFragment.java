package com.example.lorebase.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    Boolean is_done;

    public static TodoFragment getInstance(Boolean is_done) {
        TodoFragment todoFragment = new TodoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConstName.IS_DONE,is_done);
        todoFragment.setArguments(bundle);
        return todoFragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

}
