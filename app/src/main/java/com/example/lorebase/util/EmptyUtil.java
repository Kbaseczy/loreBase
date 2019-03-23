package com.example.lorebase.util;

import com.example.lorebase.R;
import com.example.lorebase.ui.fragment.subFragment.EmptyFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EmptyUtil {
    public static void goEmpty(FragmentManager manager,int layoutId) {
        EmptyFragment emptyFragment = new EmptyFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(
                R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_exit,
                R.animator.fragment_slide_right_enter).
                replace(layoutId, emptyFragment);
        transaction.commitAllowingStateLoss();
    }
}
