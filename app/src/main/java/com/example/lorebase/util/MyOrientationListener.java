package com.example.lorebase.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {


    private SensorManager mSensorManager;// 传感器管理者
    private Sensor mAccelerometer;// 加速度传感器
    private Sensor mMagnetic;// 地磁传感器
    private Context mContext;// 上下文
    private OnOrientationListener mOnOrientationListener;// 方向监听
    private float[] mAccelerometerValues = new float[3];// 用于保存加速度值
    private float[] mMagneticValues = new float[3];// 用于保存地磁值

    public MyOrientationListener(Context context) {
        this.mContext = context;
        start();
    }

    public interface OnOrientationListener {
        void onOrientationChanged(float azimuth, float pitch, float roll);
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.mOnOrientationListener = onOrientationListener;
    }

    public void start() {
        // 实例化传感器管理工具
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    public void registerListener() {
        if (mSensorManager != null && mAccelerometer != null && mMagnetic != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    private void calculateOrientation() {
        float[] values = new float[3];// 最终结果
        float[] R = new float[9];// 旋转矩阵
        SensorManager.getRotationMatrix(R, null, mAccelerometerValues, mMagneticValues);// 得到旋转矩阵
        SensorManager.getOrientation(R, values);// 得到最终结果
        float azimuth = (float) Math.toDegrees(values[0]);// 航向角
        if (azimuth < 0) {
            azimuth += 360;
        }
        azimuth = azimuth / 5 * 5;// 做了一个处理，表示以5°的为幅度
        float pitch = (float) Math.toDegrees(values[1]);// 俯仰角
        float roll = (float) Math.toDegrees(values[2]);// 翻滚角
        if (mOnOrientationListener != null) {
            mOnOrientationListener.onOrientationChanged(azimuth, pitch, roll);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagneticValues = event.values;
        }
        calculateOrientation();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
