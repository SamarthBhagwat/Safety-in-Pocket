package com.example.safetyinpocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;

public class Shake extends Service implements SensorEventListener {
    public final int MIN_TIME_BETWEEN_SHAKE= 5000;
    SensorManager sensorManager= null;


    Vibrator vibrator= null;
    private  long lastShakeTime= 0;

    private  float shakeThreshold= 80.0f;


    public Shake(){}

    //public static boolean isServiceRunning() {
    //  return true;
   // }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);


        if (sensorManager!=null)
        {
            Sensor accelerometer= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            long curTime=System.currentTimeMillis();

            if ((curTime-lastShakeTime) > MIN_TIME_BETWEEN_SHAKE)
            {
                float x=event.values[0];
                float y=event.values[1];
                float z=event.values[2];

                double acceleration;
                acceleration = Math.sqrt(Math.pow(x,2) + Math.pow(y,2)  + Math.pow(z,2))-SensorManager.GRAVITY_EARTH ;
                if (acceleration > shakeThreshold)
                {
                    lastShakeTime = curTime;

                    try {

                        DashboardActivity.getInstance().sendSMS();

                        Uri alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION );
                        MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
                        mp.start();


                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
