package com.example.lorebase;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PositionInterface;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MapService extends Service {
    public LocationClient locationClient;
    private PositionInterface positionInterface;

    public void setPositionInterface(PositionInterface positionInterface) {
        this.positionInterface = positionInterface;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getLocation();
        L.v("mapService", "运行了吗");
        return super.onStartCommand(intent, flags, startId);
    }

    void getLocation() {
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
                L.v("mapService", location.getLatitude() + "\t" + location.getLongitude() + "   here");

                receiver(location.getLatitude(), location.getLongitude());
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

    @Override
    public void onDestroy() {
        locationClient.stop();
        localBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    LocalBroadcastManager localBroadcastManager;
    MapReceiver receiver;

    private void receiver(double latitude, double longitude) {
        receiver = new MapReceiver();
        IntentFilter intentFilter = new IntentFilter();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
//        intentFilter.addAction("MAP_RECEIVER");
        localBroadcastManager.registerReceiver(receiver, intentFilter);

        Intent intent = new Intent();
        intent.setAction("android.map.MapReceiver");
        intent.putExtra(ConstName.LATITUDE, latitude);
        intent.putExtra(ConstName.LONGITUDE, longitude);
        localBroadcastManager.sendBroadcast(intent);
    }
}
