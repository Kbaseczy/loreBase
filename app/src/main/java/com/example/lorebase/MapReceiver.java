package com.example.lorebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lorebase.contain_const.ConstName;
import com.example.lorebase.util.L;

public class MapReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        L.v("MapReceiver",intent.getIntExtra(ConstName.LATITUDE,0)+
        intent.getIntExtra(ConstName.LONGITUDE,0)+"  广播传数据");

    }
}
