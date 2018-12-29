package com.example.lorebase.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.ConstName;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelaxFragment extends Fragment {

    public static RelaxFragment getInstantce(String name) {
        RelaxFragment fragment = new RelaxFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstName.TITLE, name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relax, null);
        return view;
    }
}
