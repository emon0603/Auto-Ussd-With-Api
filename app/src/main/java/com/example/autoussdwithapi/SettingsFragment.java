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
    private static final String RADIO_SELECTION_KEY = "radio_selection";

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

        //-----------------------------------------------------------------------------------------
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int savedRadioButtonId = sharedPreferences.getInt(RADIO_SELECTION_KEY, -1);
        if (savedRadioButtonId != -1) {
            radioGroup.check(savedRadioButtonId);  // Set the saved selection
        }

        String api = sharedPreferences.getString("api", null);
        String bkash = String.valueOf((sharedPreferences.getInt("bkash",0 )));
        String nagad = String.valueOf(sharedPreferences.getInt("nagad", 0));
        String apikey = sharedPreferences.getString("apikey", null);
        String number = String.valueOf(sharedPreferences.getInt("number", 0));


        edapi.setText(api);
        edbkash.setText(bkash);
        ednagad.setText(nagad);
        edapikey.setText(apikey);
        edsendernumber.setText(number);


        //-----------------------------------------------------------------------------------------

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioagent) {

                } else if (checkedId == R.id.radiopersonal) {

                }
            }
        });

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("api", edapi.getText().toString());
                editor.putInt("bkash", Integer.parseInt(edbkash.getText().toString()));
                editor.putInt("nagad", Integer.parseInt(ednagad.getText().toString()));
                editor.putString("apikey", edapikey.getText().toString());
                editor.putInt("number", Integer.parseInt(edsendernumber.getText().toString()));
                editor.apply();

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(RADIO_SELECTION_KEY, selectedRadioButtonId);
                    editor.apply();  // Save data asynchronously
                }

                Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();



            }
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