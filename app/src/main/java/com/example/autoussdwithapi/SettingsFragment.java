package com.example.autoussdwithapi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class SettingsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View SettingView = inflater.inflate(R.layout.fragment_settings, container, false);
        RadioGroup radioGroup = SettingView.findViewById(R.id.radiogroup);
        RadioButton radioagent = SettingView.findViewById(R.id.radioagent);
        RadioButton radiopersonal = SettingView.findViewById(R.id.radiopersonal);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioagent) {

                } else if (checkedId == R.id.radiopersonal) {

                }
            }
        });



        return SettingView;
    }
}