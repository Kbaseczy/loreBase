package com.example.lorebase.ui.fragment.subFragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.lorebase.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {
    public LocationClient locationClient;
    private TextView locationText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFistLocate = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext()); //TODO 需要在加载布局之前运行，初始化地图SDK
        View view = inflater.inflate(R.layout.fragment_location,null);
        locationClient = new LocationClient(getActivity().getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        mapView = view.findViewById(R.id.bMap_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationText = view.findViewById(R.id.text_location);

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]); //todo 将permissionList转为Array
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else{
            initLocation();
        }

        return view;
    }
    private void initLocation(){
        updateLocation();
        locationClient.start();
    }
    private void updateLocation(){
        // todo 通过LocationClientOption 实现实时更新定位信息->5s/次
        //todo  LocationClientOption
        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors); todo 设置定位方式GPS
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    private void navigateTo(BDLocation location){
        //todo baidumap  用到的
        // TODO 获取当前位置map   LatLng,MapStatusUpdate,MapStatusUpdateFactory,animateMapStatus
        if(isFistLocate){
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(20f); //todo 设置缩放级别
            baiduMap.animateMapStatus(update);
            isFistLocate = false;
        }
        // todo 获取当前所在位置  MyLocationData.Builder   MyLocationData  setMyLocationData
        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(getActivity(),"必须同意所有权限才能使用该程序",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    initLocation();
                }else{
                    Toast.makeText(getActivity(),"未知错误",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            if(location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }

            getActivity().runOnUiThread(() -> {
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append("Latitude:").append(location.getLatitude()).append("\t\t\t");
                currentPosition.append("Longitude:").append(location.getLongitude()).append("\n");
                currentPosition.append("Country:").append(location.getCountry()).append("\t\t\t");
                currentPosition.append("Province:").append(location.getProvince()).append("\n");
                currentPosition.append("City:").append(location.getCity()).append("\t\t\t");
                currentPosition.append("District:").append(location.getDistrict()).append("\n");
                currentPosition.append("Street:").append(location.getStreet()).append("\t\t\t");
                currentPosition.append("Location style:");
                if(location.getLocType() == BDLocation.TypeGpsLocation){
                    currentPosition.append("GPS");
                }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                    currentPosition.append("NetWork");
                }
                locationText.setText(currentPosition);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

}
