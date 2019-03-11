package com.example.lorebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lorebase.R;
import com.example.lorebase.contain_const.VideoConstant;
import com.example.lorebase.holder.BaseHolder;
import com.example.lorebase.util.LoadVideoScreenShot;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecyclerViewVideo extends RecyclerView.Adapter<AdapterRecyclerViewVideo.MyViewHolder> {

    private int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Context context;

    public AdapterRecyclerViewVideo(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_relax_list, parent,
                false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(position);
        holder.setAdapter(this);
    }

    @Override
    public int getItemCount() {
        return videoIndexs.length;
    }

    public class MyViewHolder extends BaseHolder {
        public final static String TAG = "RecyclerView2List";
        StandardGSYVideoPlayer videoPlayer;
        TextView name;
        private GSYVideoOptionBuilder gsyVideoOptionBuilder;
        ImageView imageView;

        MyViewHolder(View itemView, Context context) {
            super(itemView);
            videoPlayer = itemView.findViewById(R.id.video_player);
            name = itemView.findViewById(R.id.relax_item_name);
            imageView = new ImageView(context);
            gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        }

        void onBind(final int position) {

            name.setText(VideoConstant.videoTitles[0][position]);
            //增加封面
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LoadVideoScreenShot.loadVideoScreenshot(context,"http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4",imageView,1000);
            if (imageView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) imageView.getParent();
                viewGroup.removeView(imageView);
            }

            Map<String, String> header = new HashMap<>();
            header.put("ee", "33");

            gsyVideoOptionBuilder
                    .setIsTouchWiget(false)
                    .setThumbImageView(imageView)
                    .setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
//             VideoConstant.videoUrls[0][position]
                    .setVideoTitle(VideoConstant.videoTitles[0][position])
                    .setCacheWithPlay(false)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setPlayTag(TAG)
                    .setMapHeadData(header)
                    .setShowFullAnimation(true)
                    .setNeedLockFull(true)
                    .setPlayPosition(position)
                    .setVideoAllCallBack(new GSYSampleCallBack() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            if (!videoPlayer.isIfCurrentIsFullscreen()) {
                                //静音
                                GSYVideoManager.instance().setNeedMute(true);
                            }
                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            //全屏不静音
                            GSYVideoManager.instance().setNeedMute(true);
//                            windowIsTranslucent=true;
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
                            GSYVideoManager.instance().setNeedMute(false);
                            videoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                        }
                    }).build(videoPlayer);


            //增加title
            videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);

            //设置返回键
            videoPlayer.getBackButton().setVisibility(View.GONE);

            //设置全屏按键功能
            videoPlayer.getFullscreenButton().setOnClickListener(v -> resolveFullBtn(videoPlayer));
        }

        /**
         * 全屏幕按键处理
         */
        private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
            standardGSYVideoPlayer.startWindowFullscreen(context, false, true);
        }
    }



}
