package com.example.lorebase.ui.fragment.subFragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lorebase.R;
import com.example.lorebase.adapter.RecyclerBaseAdapter;
import com.example.lorebase.bean.VideoModel;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.holder.RecyclerItemViewHolder;
import com.example.lorebase.util.HttpUtil;
import com.example.lorebase.util.L;
import com.example.lorebase.util.NetWorkHelper;
import com.example.lorebase.util.ToastUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelaxListFragment extends Fragment {
    private int identity_id;
    private View view;
    private RecyclerBaseAdapter recyclerBaseAdapter;
    private List<VideoModel> dataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private GSYVideoHelper smallVideoHelper;

    private int firstVisibleItem, lastVisibleItem;

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
        initView();
        L.v("onCreateView execute.  " + identity_id);

        if (HttpUtil.isNetworkAvailable(getActivity()))
            if (!NetWorkHelper.isWifi(Objects.requireNonNull(getActivity())))
                ToastUtil.showShortToastTop("当前非wifi网络，请注意流量使用", getActivity());
        return view;
    }

    private void initView() {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_relax_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        FrameLayout frameLayout = view.findViewById(R.id.video_full_container);
        resolveData();
        //配置 小窗口
        recyclerBaseAdapter = new RecyclerBaseAdapter(getActivity(), dataList);
        recyclerView.setAdapter(recyclerBaseAdapter);

        smallVideoHelper = new GSYVideoHelper(getActivity(), new NormalGSYVideoPlayer(getActivity()));
        smallVideoHelper.setFullViewContainer(frameLayout);

        GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
        gsySmallVideoHelperBuilder
                .setHideActionBar(true)
                .setHideStatusBar(true)
                .setNeedLockFull(true)
                .setCacheWithPlay(true)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(true)
                .setLockLand(true).setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + smallVideoHelper.getGsyVideoPlayer().getDuration() + " CurrentPosition " + smallVideoHelper.getGsyVideoPlayer().getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        smallVideoHelper.releaseVideoPlayer();
                        recyclerBaseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        smallVideoHelper.setGsyVideoOptionBuilder(gsySmallVideoHelperBuilder);
        recyclerBaseAdapter.setVideoHelper(smallVideoHelper, gsySmallVideoHelperBuilder);
        //recycler 滑动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(RecyclerItemViewHolder.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(getActivity(), 150);  //上下文环境 this --context -->point
                            //actionbar为true才不会掉下面去
                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (smallVideoHelper.isSmall()) {
                            smallVideoHelper.smallVideoToNormal();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smallVideoHelper.releaseVideoPlayer();
        GSYVideoManager.releaseAllVideos();
    }

    private void resolveData() {
        for (int i = 0; i < 19; i++) {
            VideoModel videoModel = new VideoModel();
            dataList.add(videoModel);
        }
        if (recyclerBaseAdapter != null)
            recyclerBaseAdapter.notifyDataSetChanged();
    }
}
