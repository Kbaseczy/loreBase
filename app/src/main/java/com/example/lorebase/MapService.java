package com.example.lorebase;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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
import com.example.lorebase.ui.activity.LocationActivity;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PositionInterface;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MapService extends JobIntentService {
    public LocationClient locationClient;
    private BaiduMap baiduMap;
    private PositionInterface positionInterface;
    public void setPositionInterface(PositionInterface positionInterface) {
        this.positionInterface = positionInterface;
    }

    public MapService() {

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        getLocation();

        L.v("onHandleWork");
    }


    void getLocation(){
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(new MyLocationListener());
        updateLocation();
        locationClient.start();
    }
   public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation ||
                    location.getLocType() == BDLocation.TypeNetWorkLocation) {
                if (positionInterface != null)
                    positionInterface.transferPosition(location.getLatitude(), location.getLongitude());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }
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
}
