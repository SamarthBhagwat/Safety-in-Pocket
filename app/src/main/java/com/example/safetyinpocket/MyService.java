package com.example.safetyinpocket;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    public int onStartCommand(Intent intent, int flags, int startId){
        createNotificationChannel();
        Intent intent1=new Intent(getApplicationContext(),DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);
        Notification notification=new NotificationCompat.Builder(this,"ChannelId1")
                .setContentTitle("SIP")
                .setContentText("Shake Service is running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
        startForeground(1,notification);
        startService(new Intent(getApplicationContext(),Shake.class));
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel("ChannelId1","Foreground notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();

        super.onDestroy();

    }

}
