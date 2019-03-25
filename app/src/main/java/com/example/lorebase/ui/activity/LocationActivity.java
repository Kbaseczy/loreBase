package com.example.lorebase.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.lorebase.R;
import com.example.lorebase.util.ActivityCollector;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PositionInterface;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 权限申请和变量实例化先后顺序  导致的crash -->具体表现为：权限申请后才实例化
 */
public class LocationActivity extends Activity {
    public LocationClient locationClient;
    private TextView locationText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFistLocate = true;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    private StringBuilder currentPosition;
    private PositionInterface positionInterface;
    public void setPositionInterface(PositionInterface positionInterface) {
        this.positionInterface = positionInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.v("LocationActivity","onCreate");
        setContentView(R.layout.activity_location);
        ActivityCollector.addActivtity(this);
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(new MyLocationListener());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("实时位置");
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
            overridePendingTransition(R.animator.go_in, R.animator.go_out);
        });
        mapView = findViewById(R.id.bMap_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);  //地图显示类型
        initLocation();
    }

    private void initLocation() {
        updateLocation();
        locationClient.start();
    }

    private void updateLocation() {
        // todo 通过LocationClientOption 实现实时更新定位信息->5s/次
        //todo  LocationClientOption
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors); //todo 设置定位方式GPS
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        locationClient.setLocOption(option);
    }

    private void navigateTo(BDLocation location) {
        if (isFistLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(20f); //todo 设置缩放级别`
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
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }

            runOnUiThread(() -> {
                currentPosition = new StringBuilder();
                currentPosition.append("Latitude:").append(location.getLatitude()).append("\t\t\t");
                currentPosition.append("Longitude:").append(location.getLongitude()).append("\n");
                currentPosition.append("Country:").append(location.getCountry()).append("\t\t\t");
                currentPosition.append("Province:").append(location.getProvince()).append("\n");
                currentPosition.append("City:").append(location.getCity()).append("\t\t\t");
                currentPosition.append("District:").append(location.getDistrict()).append("\n");
                currentPosition.append("Street:").append(location.getStreet()).append("\t\t\t");
                currentPosition.append("Location style:");
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    currentPosition.append("GPS");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    currentPosition.append("NetWork");
                }
//                locationText.setText(currentPosition);
            });
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        L.v("LocationActivity","onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        L.v("LocationActivity","onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        L.v("LocationActivity","onDestroy");
    }
}
