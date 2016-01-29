package com.ek.nfcwifi;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by eyeKill on 15/12/10.
 */

public class UserActivity extends FlowActivity{
    NfcAdmin nfcadmin;
    WifiAdmin wifiAdmin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //相关初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        content=findViewById(R.id.user_Content);
        menu=findViewById(R.id.user_Menu);
        wifiAdmin=new WifiAdmin(this);
        initValues();
        content.setOnTouchListener(this);
        if (MainActivity.myInstance!=null) nfcadmin=MainActivity.myInstance.getNfcAdmin();
        else {Toast.makeText(this,"Wops, no instance found.",Toast.LENGTH_SHORT).show();return;}
        //menu初始化
        for(NfcAdmin.myNfcRecord i:nfcadmin.getRecordList_get()){
            Button btn=new Button(this);
            btn.setText(i.toString());
            btn.setTextColor(Color.WHITE);
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(i.getmsgType()==i.EKNFC_TYPE_WIFI){
                wifiAdmin.openWifi();
                wifiAdmin.connectWifi(wifiAdmin.addWifiConf(i.ssid,i.pwd));
                Toast.makeText(this,"Trying to connect to "+i.value,Toast.LENGTH_SHORT).show();
                btn.setText("√"+btn.getText().toString());
            }
            btn.setTag(i.toString());
            if(i.getmsgType()== NfcAdmin.myNfcRecord.EKNFC_TYPE_URL) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse((String) v.getTag());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        //wv.loadUrl(uri.toString());
                    }
                });
            }
            ((LinearLayout)menu).addView(btn);
        }
        initValues();

    }
}
