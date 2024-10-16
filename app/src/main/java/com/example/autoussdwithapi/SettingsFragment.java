package com.example.autoussdwithapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsFragment extends Fragment {

    EditText edapi,edbkash,ednagad,edapikey,edsendernumber;
    Button clearbt,savebt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View SettingView = inflater.inflate(R.layout.fragment_settings, container, false);
        RadioGroup radioGroup = SettingView.findViewById(R.id.radiogroup);
        RadioButton radioagent = SettingView.findViewById(R.id.radioagent);
        RadioButton radiopersonal = SettingView.findViewById(R.id.radiopersonal);
        clearbt = SettingView.findViewById(R.id.clearbt);
        savebt = SettingView.findViewById(R.id.savebt);
        edapi = SettingView.findViewById(R.id.editapi);
        edbkash = SettingView.findViewById(R.id.editbkash);
        ednagad = SettingView.findViewById(R.id.editnagad);
        edapikey = SettingView.findViewById(R.id.editapikey);
        edsendernumber = SettingView.findViewById(R.id.edsendernumber);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioagent) {
                    Toast.makeText(getContext(), "Agent", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radiopersonal) {
                    Toast.makeText(getContext(), "Personal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        savebt.setOnClickListener(View->{
            editor.putString("api", edapi.toString());
            editor.putString("bkash", edbkash.toString());
            editor.putString("nagad", ednagad.toString());
            editor.putString("apikey", edapikey.toString());
            editor.putString("number", edsendernumber.toString());
            sharedPreferences.edit();
        });


        clearbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edapi.setText(null);
                edbkash.setText(null);
                ednagad.setText(null);
                edapikey.setText(null);
                edsendernumber.setText(null);


                radiopersonal.setChecked(false);
                radioagent.setChecked(false);


            }
        });



        return SettingView;
    }
}