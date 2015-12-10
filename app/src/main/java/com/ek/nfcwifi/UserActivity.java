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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //相关初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        content=findViewById(R.id.user_Content);
        menu=findViewById(R.id.user_Menu);
        initValues();
        content.setOnTouchListener(this);
        if (MainActivity.myInstance!=null) nfcadmin=MainActivity.myInstance.getNfcAdmin();
        else {Toast.makeText(this,"Wops, no instance found.",Toast.LENGTH_SHORT).show();return;}
        //menu初始化
        Toast.makeText(this,"begin",Toast.LENGTH_SHORT).show();
        for(NfcAdmin.myNfcRecord i:nfcadmin.getRecordList_get()){
            TextView tv=new TextView(this);
            tv.setText(i.toString());
            tv.setTextColor(Color.WHITE);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Toast.makeText(this,"get:"+i.toString(),Toast.LENGTH_SHORT).show();
            ((LinearLayout)menu).addView(tv);
        }
        initValues();
    }
}
