package com.example.lorebase;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;

import com.example.lorebase.ui.activity.MainActivity;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String channelId = "chat";
        String name = "LoreBase提醒";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId,name,importance);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;  //  用户手动设置时间
//        int anHour = 20 * 1000;  //20s.
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        assert manager != null;
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void createNotificationChannel(String id, String name, int importance){
        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setShowBadge(true);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        notiInfo();
    }

    void notiInfo(){
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel("chat");
            if(channel.getImportance()==NotificationManager.IMPORTANCE_NONE){
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID,channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动打开通知", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,"chat")
                .setContentTitle("LoreBase提醒")
                .setContentText("阅读：提升时间到啦~")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setNumber(2)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);
    }
}
