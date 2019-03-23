package com.example.lorebase.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.lorebase.BaseActivity;
import com.example.lorebase.MyApplication;
import com.example.lorebase.R;
import com.example.lorebase.adapter.BrowseHistoryAdapter;
import com.example.lorebase.bean.BrowseHistory;
import com.example.lorebase.greenDao.BrowseHistoryDao;
import com.example.lorebase.util.L;
import com.example.lorebase.util.ToastUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseHistoryActivity extends BaseActivity {

    private List<BrowseHistory> browseHistoryList;
    private FloatingActionButton fab_delete, fab_top;

    public LocationClient locationClient;
    private BaiduMap baiduMap;
    MapView mapView;
    private boolean isFistLocate = true;
    private StringBuilder currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_history);
        initRecycler();
        initMap();
        overLay();
    }

    @SuppressLint("RestrictedApi")
    private void initRecycler() {
        Toolbar toolbar = findViewById(R.id.toolbar_browse_history);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.browse_history__collapsing_toolbar);
        ImageView portrait = findViewById(R.id.browse_history_portrait_image_view);
        RecyclerView recyclerView = findViewById(R.id.browse_history_list);
        fab_top = findViewById(R.id.fab_browse_history_top);
        fab_delete = findViewById(R.id.fab_browse_history_delete);
        NestedScrollView nestedScrollView = findViewById(R.id.nest_scroll_bh);

        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
        collapsingToolbarLayout.setTitle(getString(R.string.nav_browser));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbarLayout.setBackgroundColor(Color.GRAY);
        Glide.with(this).load(R.drawable.image_timetree).into(portrait);

        GridLayoutManager manager = new GridLayoutManager(this, 1);
        BrowseHistoryDao browseHistoryDao = MyApplication.getDaoSession().getBrowseHistoryDao();
        browseHistoryList = browseHistoryDao.queryBuilder().list();
        BrowseHistoryAdapter adapter = new BrowseHistoryAdapter(browseHistoryList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemGridDecoration(this));
        fab_delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tip)
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setMessage(R.string.tip_content_clear_history)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        browseHistoryDao.deleteAll();
                        if (browseHistoryList != null) {
                            browseHistoryList.clear();
                        }
                        fab_delete.setVisibility(View.INVISIBLE);
                        fab_top.setVisibility(View.INVISIBLE);
                        baiduMap.clear();  //清除覆盖物
                        adapter.notifyDataSetChanged();
                        Glide.with(this).load(R.drawable.empty_cup).into(portrait);
                    }); //清空数据库
            builder.create().show(); //遗漏
        });

        fab_top.setOnClickListener(v -> nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_UP)));

        if (browseHistoryList.size() == 0) {
            Glide.with(this).load(R.drawable.empty_cup).into(portrait);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab_delete.setVisibility(browseHistoryList.size() == 0 ? View.INVISIBLE : View.VISIBLE);
        fab_top.setVisibility(browseHistoryList.size() < 15 ? View.INVISIBLE : View.VISIBLE);

        mapView.onResume();
        L.v("LocationActivity", "onResume");
    }

    //todo map

    void initMap() {
        locationClient = new LocationClient(this);

        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location.getLocType() == BDLocation.TypeGpsLocation ||
                        location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    navigateTo(location);
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {
                super.onConnectHotSpotMessage(s, i);
            }
        });
        mapView = findViewById(R.id.bMap_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        updateLocation();
        locationClient.start();
    }

    //图层
    private void overLay() {
        //38.86145	121.523533
        List<BrowseHistory> list = MyApplication.getDaoSession().getBrowseHistoryDao().queryBuilder().list();
        L.v("overlay",list.size()  +" size");
        for (BrowseHistory browseHistory:list) {

            L.v("overlay",browseHistory.getTitle());
            L.v("overlay",browseHistory.getLatidude()+"  latitude");
            L.v("overlay",browseHistory.getLongitude()+" longitude");

            LatLng point = new LatLng(browseHistory.getLatidude(),browseHistory.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_position);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .title(browseHistory.getTitle());
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(option);
            baiduMap.setOnMarkerClickListener(marker -> {
                ToastUtil.showLongToastCenter(marker.getTitle(),this);
                return true;
            });
        }
    }

    //更新位置
    private void updateLocation() {
        // todo 通过LocationClientOption 实现实时更新定位信息->5s/次
        //todo  LocationClientOption
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy); //todo 设置定位方式GPS
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        locationClient.setLocOption(option);
    }

    //定位当前位置  显示map时，使当前位置为第一视图
    private void navigateTo(BDLocation location) {
        if (isFistLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(10f); //todo 设置缩放级别`
            baiduMap.animateMapStatus(update);
            isFistLocate = false;
        }
        // todo 获取当前所在位置  MyLocationData.Builder   MyLocationData  setMyLocationData
        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(100)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        baiduMap.setMyLocationData(locationData);

//        overLay(location);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        L.v("LocationActivity", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        L.v("LocationActivity", "onDestroy");
    }
}
