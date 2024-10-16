package com.example.autoussdwithapi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.autoussdwithapi.Service.MyAccessibilityService;

public class HomeFragment extends Fragment {

    private static final String CHANNEL_ID = "accessibility_channel";
    private NotificationManager notificationManager;
    TextView stutustv;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        stutustv = HomeView.findViewById(R.id.stutustv);
        Switch switchAccessibility = HomeView.findViewById(R.id.switch1);
        button = HomeView.findViewById(R.id.button3);

        // Notification Manager
        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        //Api_Request();

        switchAccessibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isAccessibilityServiceEnabled(MyAccessibilityService.class)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                        Button_Click();

                    } else {
                        // Start the foreground service
                        startForegroundService();
                        Button_Click();

                    }
                } else {
                    // Stop the foreground service
                    stopForegroundService();
                }
            }
        });

        return HomeView;
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(requireContext(), MyAccessibilityService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(serviceIntent);
        }
        showNotification();
    }

    private void stopForegroundService() {
        Intent serviceIntent = new Intent(requireContext(), MyAccessibilityService.class);
        requireContext().stopService(serviceIntent);
        removeNotification();
    }

    private void showNotification() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle("Accessibility Service Enabled")
                .setContentText("Click to manage accessibility settings")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(true) // This makes the notification ongoing (non-dismissible)
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
            String description = "Now You Can Enjoying!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void Button_Click(){


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                runUssd();
                Intent intent = new Intent(getActivity(), MyAccessibilityService.class);
                requireActivity().startService(intent);

            }
        });


    }

    private void runUssd(){
        String ussdCode = "*121#";
        //String ussdCode = ussdEditText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(ussdToCallableUri(ussdCode));
        try{
            startActivity(intent);
        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private Uri ussdToCallableUri(String ussd) {
        String uriString = "";
        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {
            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    private void Api_Request(){

        String url = "http://10.10.10.5/USSD/test.php?n=0175575 && t=b && b=100";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                String s = response.toString();
                Log.d("server", s);
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);





    }




}



