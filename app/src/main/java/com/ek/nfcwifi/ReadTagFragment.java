package com.ek.nfcwifi;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by eyeKill on 15/10/16.
 */
public class ReadTagFragment extends Fragment {
    MainActivity dad;
    NfcAdmin nfcAdmin;
    NfcAdapter nfcAdapter;
    ImageView img_nfc;
    TextView tvMsg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_readtag,container,false);
        dad=(MainActivity)this.getActivity();
        nfcAdmin=dad.getNfcAdmin();
        img_nfc=(ImageView)view.findViewById(R.id.readTag_nfc);
        tvMsg=(TextView)view.findViewById(R.id.readTag_tvMsg);
        nfcAdapter=nfcAdmin.getNfcAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(nfcAdapter==null){
            img_nfc.setBackground(getResources().getDrawable(R.mipmap.nfc_not_enabled));
            tvMsg.setText("Nfc not supported.\nWhy on earth did you install me?");
            tvMsg.setOnClickListener(null);
        }
        else if(!nfcAdapter.isEnabled()){//nfc disabled
            img_nfc.setBackground(getResources().getDrawable(R.mipmap.nfc_not_enabled));
            tvMsg.setText("Nfc Disabled.\nClick me to enable!");
            tvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                }
            });
        }
        else{
            img_nfc.setBackground(getResources().getDrawable(R.mipmap.nfc_enabled));
            tvMsg.setText("READY TO SCAN");
            tvMsg.setOnClickListener(null);
        }
    }
}
