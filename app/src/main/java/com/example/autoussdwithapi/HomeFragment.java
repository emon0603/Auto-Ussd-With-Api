package com.example.autoussdwithapi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.autoussdwithapi.Service.Forground;
import com.example.autoussdwithapi.Service.MyAccessibilityService;
import com.example.autoussdwithapi.Service.NotificationForground;

public class HomeFragment extends Fragment {

    private static final String CHANNEL_ID = "accessibility_channel";
    private NotificationManager notificationManager;
    TextView stutustv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        stutustv = HomeView.findViewById(R.id.stutustv);
        Switch switchAccessibility = HomeView.findViewById(R.id.switch1);

        // Notification Manager
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();



        switchAccessibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isAccessibilityServiceEnabled(MyAccessibilityService.class)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);

                    } else {
                        // Start the foreground service
                        /*Intent serviceIntent = new Intent(requireContext(), NotificationForground.class);
                        requireContext().startService(serviceIntent);
                         */
                        showNotification();
                    }
                } else {
                    // Stop the foreground service
                    Intent serviceIntent = new Intent(requireContext(), NotificationForground.class);
                    requireContext().stopService(serviceIntent);
                }
            }
        });

        return HomeView;
    }

    private void showNotification() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle("Accessibility Service Enabled")
                .setContentText("Click to manage accessibility settings")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build();

        notificationManager.notify(1, notification);
    }

    private void removeNotification() {
        notificationManager.cancel(1);
    }

    private boolean isAccessibilityServiceEnabled(Class<?> accessibilityServiceClass) {
        int accessibilityEnabled = 0;
        final String service = requireContext().getPackageName() + "/" + accessibilityServiceClass.getCanonicalName();

        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    requireContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED
            );
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            );
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String componentName = colonSplitter.next();
                    if (componentName.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Accessibility Service Channel";
            String description = "Channel for Accessibility Service notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
           // NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
