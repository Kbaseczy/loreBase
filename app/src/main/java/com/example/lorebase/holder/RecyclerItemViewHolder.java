package com.example.lorebase.holder;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.lorebase.R;
import com.example.lorebase.bean.VideoModel;
import com.example.lorebase.util.LoadVideoScreenShot;
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
        String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        //增加封面
//        String url = "https://pan.baidu.com/play/video#/video?path=%2FJava%26Android%2FAndroid%E4%BB%8E%E5%85%A5%E9%97%A8%E5%88%B0%E7%B2%BE%E9%80%9A%2F6.%E7%BD%91%E7%BB%9C%E9%80%9A%E4%BF%A1%2F3%E3%80%81Android%E4%B8%AD%E5%9F%BA%E4%BA%8ESocket%E7%9A%84%E7%BD%91%E7%BB%9C%E9%80%9A%E4%BF%A1%2F3%E3%80%81%E4%BD%BF%E7%94%A8ServerSocket%E5%BB%BA%E7%AB%8B%E8%81%8A%E5%A4%A9%E6%9C%8D%E5%8A%A1%E5%99%A8-1.mp4&t=-1";
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LoadVideoScreenShot.loadVideoScreenshot(context
                , url
                ,imageView,1000);

        smallVideoHelper.addVideoPlayer(position, imageView, TAG, listItemContainer, listItemBtn);

        listItemBtn.setOnClickListener(v -> {
            smallVideoHelper.setPlayPositionAndTag(position, TAG);
            getAdapter().notifyDataSetChanged();
            //listVideoUtil.setLoop(true);
//            String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
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





