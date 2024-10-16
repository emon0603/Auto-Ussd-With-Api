package com.example.autoussdwithapi.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.autoussdwithapi.R;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private NotificationManager notificationManager;

    private int currentindex = 0;
    public static String TAG = "USSD";
    private List<String> numList;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        showPersistentNotification();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Your Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    private void showPersistentNotification() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Accessibility Service Enabled")
                    .setContentText("Click to access accessibility settings")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true) // এটি নোটিফিকেশনকে পেরম্যানেন্ট বানাবে
                    .setContentIntent(pendingIntent) // ক্লিক করলে এই Intent চালাবে
                    .build();
        }

        notificationManager.notify(1, notification);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // আপনার কোড এখানে

        AccessibilityNodeInfo nodeInfo = event.getSource();

        if (nodeInfo == null) return;

        String text = event.getText().toString();

        if (event.getClassName().equals("android.app.AlertDialog")) {

            AccessibilityNodeInfo nodeinput = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);

            if (nodeinput != null && currentindex < numList.size() ) {

                String numberToDial = String.valueOf(numList.get(currentindex));

                Bundle bundle = new Bundle();
                bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, numberToDial);
                nodeinput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,bundle);
                nodeinput.refresh();

                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("Send");
                for (AccessibilityNodeInfo node : list) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }

                currentindex++;


            }

        }




    }

    @Override
    public void onInterrupt() {
        // আপনার কোড এখানে

        numList = new ArrayList<>();
        numList.add("9");
        numList.add("1");
        numList.add("12345");

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentindex = 0;
        return START_STICKY;
    }
}
