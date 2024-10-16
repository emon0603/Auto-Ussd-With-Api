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
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.autoussdwithapi.R;

import java.util.ArrayList;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private NotificationManager notificationManager;
    public static String TAG = "USSD";
    private List<String> numberList;
    private int currentIndex = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        showPersistentNotification();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");

        try {

            AccessibilityNodeInfo nodeInfo = event.getSource();

            if (nodeInfo == null) return;

            String text = event.getText().toString();
            if (event.getClassName().equals("android.app.AlertDialog")) {
                Log.d(TAG, text);
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();

                AccessibilityNodeInfo nodeInput = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
                if (nodeInput != null && currentIndex < numberList.size()) {

                    String numberToDial = numberList.get(currentIndex);

                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, numberToDial);
                    nodeInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
                    nodeInput.refresh();


                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("Send");
                    for (AccessibilityNodeInfo node : list) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                    currentIndex++;
                    Log.d("index", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");

        try {

            numberList = new ArrayList<>();
            numberList.add("1");
            numberList.add("1");

            AccessibilityServiceInfo info = new AccessibilityServiceInfo();
            info.flags = AccessibilityServiceInfo.DEFAULT;
            info.packageNames = new String[]{"com.android.phone"};
            info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
            setServiceInfo(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("repete", "Service Started");
        currentIndex = 0;
        return START_STICKY;
    }


    //----------------------------------------------------------------------------


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
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Accessibility Service Enabled")
                    .setContentText("Click to access accessibility settings")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .build();
        }
        notificationManager.notify(1, notification);
    }

}


