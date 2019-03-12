package com.example.lorebase.holder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lorebase.R;
import com.example.lorebase.bean.VideoModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

public class RecyclerItemViewHolder extends BaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context;

    private FrameLayout listItemContainer;

    private ImageView listItemBtn;

    private ImageView imageView;

    private GSYVideoHelper smallVideoHelper;

    private GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public RecyclerItemViewHolder(Context context, View v) {
        super(v);
        this.context = context;
        imageView = new ImageView(context);
        listItemContainer = v.findViewById(R.id.list_item_container);
        listItemBtn = v.findViewById(R.id.list_item_btn);
    }

    public void onBind(final int position, VideoModel videoModel) {

        //增加封面
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.cherry);

        smallVideoHelper.addVideoPlayer(position, imageView, TAG, listItemContainer, listItemBtn);

        listItemBtn.setOnClickListener(v -> {
            smallVideoHelper.setPlayPositionAndTag(position, TAG);
            getAdapter().notifyDataSetChanged();
            //listVideoUtil.setLoop(true);
            String url = "http://wdquan-space.b0.upaiyun.com/VIDEO/2018/11/22/ae0645396048_hls_time10.m3u8";
            //listVideoUtil.setCachePath(new File(FileUtils.getPath()));

            gsySmallVideoHelperBuilder.setVideoTitle("title " + position).setUrl(url);

            smallVideoHelper.startPlay();

            //必须在startPlay之后设置才能生效
//            listVideoUtil.getGsyVideoPlayer().getTitleTextView().setVisibility(View.VISIBLE);
        });
    }


    public void setVideoHelper(GSYVideoHelper smallVideoHelper, GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder) {
        this.smallVideoHelper = smallVideoHelper;
        this.gsySmallVideoHelperBuilder = gsySmallVideoHelperBuilder;
    }
}





