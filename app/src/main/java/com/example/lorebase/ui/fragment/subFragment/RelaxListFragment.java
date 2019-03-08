package com.example.lorebase.ui.fragment.subFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.lorebase.R;
import com.example.lorebase.adapter.AdapterRecyclerViewVideo;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.util.JZMediaIjkplayer;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelaxListFragment extends Fragment {
    private int identity_id;
    private View view;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewVideo adapterVideoList;

    public static RelaxListFragment getInstance(int we_chat_id) {
        RelaxListFragment relaxListFragment = new RelaxListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstName.ID, we_chat_id);
        relaxListFragment.setArguments(bundle);
        return relaxListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            identity_id = getArguments().getInt(ConstName.ID, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_relax_list, container, false);
        Jzvd.setMediaInterface(new JZMediaIjkplayer());
        initView();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_relax_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterVideoList = new AdapterRecyclerViewVideo(getContext());
        recyclerView.setAdapter(adapterVideoList);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                Jzvd.onChildViewAttachedToWindow(view, R.id.video_player);
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                Jzvd jzvd = view.findViewById(R.id.video_player);
//                if (jzvd != null && jzvd.jzDataSource.containsTheUrl(JZMediaManager.getCurrentUrl())) {
//                    Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
//                    if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
//                        Jzvd.releaseAllVideos();
//                    }
//                }
                Jzvd.onChildViewDetachedFromWindow(view);
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }


    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Objects.requireNonNull(getActivity()).finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
