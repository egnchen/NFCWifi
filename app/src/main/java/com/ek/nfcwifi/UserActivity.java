package com.ek.nfcwifi;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
            TextView tv=new TextView(this);
            tv.setText(i.toString());
            tv.setTextColor(Color.WHITE);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if(i.getmsgType()==i.EKNFC_TYPE_WIFI){
                wifiAdmin.openWifi();

            }
            ((LinearLayout)menu).addView(tv);
        }
        initValues();
    }
}
