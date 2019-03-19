package com.example.lorebase.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.lorebase.R;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PositionInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(new MyLocationListener());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("实时位置");
        toolbar.setNavigationOnClickListener(v -> finish());
        mapView = findViewById(R.id.bMap_view);
        mapView.setOnClickListener(v -> alertText());
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
//        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
//        locationText = findViewById(R.id.text_location);
        checkPermission();
    }

    private void overLay(BDLocation location) {
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.position);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);

        //用来构造InfoWindow的Button
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.color.Grey);
        button.setText("InfoWindow");

        //构造InfoWindow
        //point 描述的位置点
        //-100 InfoWindow相对于point在y轴的偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, point, -100);

        //使InfoWindow生效
        baiduMap.showInfoWindow(mInfoWindow);
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

        overLay(location);

        if (positionInterface != null)
            positionInterface.transferPosition(location.getLatitude(), location.getLongitude());
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
    public void onStart() {
        super.onStart();
        L.v("LocationActivity","onStart");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用该程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    initLocation();
                } else {
                    Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[0]); //todo 将permissionList转为Array
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            initLocation();
        }
    }

    private void alertText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip)
                .setIcon(R.mipmap.ic_launcher_round)
                .setMessage(currentPosition);//清空数据库
        builder.create().show(); //遗漏
    }


}
