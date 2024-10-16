package com.example.autoussdwithapi.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.autoussdwithapi.R;

public class Forground extends Service {


    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    try {
                        Thread.sleep(2000);
                        Log.e("forground", "Service is running");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        }).start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        final String Channel_ID = "Forground_Channel_ID";
        NotificationChannel build = new NotificationChannel(
                Channel_ID,
                Channel_ID,
                NotificationManager.IMPORTANCE_HIGH

        );


            getSystemService(NotificationManager.class).createNotificationChannel(build);

            Notification.Builder notification = new Notification.Builder(this,Channel_ID)
                    .setContentTitle("CurentPay AI")
                    .setContentText("Service Enable")
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
            startForeground(1001,notification.build());
        }








        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }






}
