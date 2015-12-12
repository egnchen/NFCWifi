package com.ek.nfcwifi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by eyeKill on 15/10/16.
 */

public class WifiFragment extends WriteContentFragment{
    Spinner spinner;
    EditText etPwd;
    ImageView wifi;
    Button btnConfirm;

    RadioButton rbNone,rbWEP,rbWPA2;

    String SSID_chosen=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);

        wifi=(ImageView)view.findViewById(R.id.writeTag_image);
        spinner=(Spinner)view.findViewById(R.id.writeTag_spinner);
        etPwd=(EditText)view.findViewById(R.id.writeTag_etPwd);
        btnConfirm=(Button)view.findViewById(R.id.writeTag_btn);

        rbNone=(RadioButton)view.findViewById(R.id.writeTag_rbNone);
        rbWEP=(RadioButton)view.findViewById(R.id.writeTag_rbWEP);
        rbWPA2=(RadioButton)view.findViewById(R.id.writeTag_rbWPA2);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiAdmin.openWifi();
                wifiAdmin.scanWifi();
                //适配器
                ArrayAdapter<String> arr_adapter=
                        new ArrayAdapter<String>(dad, android.R.layout.simple_spinner_item, wifiAdmin.getWifiNames());
                //设置样式
                arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(arr_adapter);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SSID_chosen=(String)parent.getAdapter().getItem(position);
                updateUi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SSID_chosen=null;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SSID_chosen!=null){
                    if(etPwd.getText().length()>=8)
                        //dad.setToWrite(wifiAdmin.createConf(SSID_chosen,etPwd.getText().toString()));
                        submit(nfcAdmin.getRecord_wifi(wifiAdmin.createConf(SSID_chosen,etPwd.getText().toString())));
                    else
                        Toast.makeText(dad, "Password at least 8 chars.",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        switch(wifiAdmin.getWifiStatus()){
            case WifiManager.WIFI_STATE_DISABLED:
            case WifiManager.WIFI_STATE_UNKNOWN:
                wifi.setBackground(getResources().getDrawable(R.mipmap.wifi_not_enabled));
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                wifi.setBackground(getResources().getDrawable(R.mipmap.wifi_enabled));
                break;
        }
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        wifi.performClick();
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUi(){
        if(SSID_chosen!=null){
            String sec=wifiAdmin.getSecurity(SSID_chosen);
            switch (sec){
                case "WPA/WPA2_PSK": rbWPA2.setChecked(true);break;
                case "WEP": rbWEP.setChecked(true);break;
                case "none": rbNone.setChecked(true);break;
                default:break;
            }
        }
    }
}
