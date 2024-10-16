package com.example.autoussdwithapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.autoussdwithapi.Service.Forground;
import com.example.autoussdwithapi.Service.MyAccessibilityService;

public class HomeFragment extends Fragment {

    TextView stutustv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View HomeView = inflater.inflate(R.layout.fragment_home, container, false);
        stutustv = HomeView.findViewById(R.id.stutustv);
        Switch switchAccessibility = HomeView.findViewById(R.id.switch1);



        switchAccessibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isAccessibilityServiceEnabled(MyAccessibilityService.class)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                        //stutustv.setText("Accessibility Service is Enabled");
                    } else {


                    }
                } else {


                }
            }
        });


        return HomeView;

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





}