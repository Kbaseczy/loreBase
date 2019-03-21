package com.example.lorebase.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.util.L;
import com.example.lorebase.util.PositionInterface;

public class MapReceiver extends BroadcastReceiver {

    HomeListAdapter homeListAdapter = new HomeListAdapter();
    public void setPositionInterface(PositionInterface positionInterface) {
        this.positionInterface = positionInterface;
    }

    PositionInterface positionInterface;

    @Override
    public void onReceive(Context context, Intent intent) {
        L.v("MapReceiver", intent.getDoubleExtra(ConstName.LATITUDE, 0) + "\t" +
                intent.getDoubleExtra(ConstName.LONGITUDE, 0) + "  广播传数据");
        if (positionInterface != null) {
            positionInterface.transferPosition(intent.getDoubleExtra(ConstName.LATITUDE, 0)
                    , intent.getDoubleExtra(ConstName.LONGITUDE, 0));
        }
        homeListAdapter.setPosition(intent.getDoubleExtra(ConstName.LATITUDE, 0),
                intent.getDoubleExtra(ConstName.LONGITUDE, 0));

    }
}
