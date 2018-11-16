package com.example.lorebase.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * kbase_article表，简单list显示name,点进去显示content,长按
 */
public class CollectFragment extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collect,null);
        return view;
    }


}
