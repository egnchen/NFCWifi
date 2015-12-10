package com.ek.nfcwifi;

import android.nfc.NdefRecord;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eyeKill on 15/10/31.
 */
public class WriteContentFragment extends Fragment{
    MainActivity dad;
    SubmitFragment submitFragment;
    WifiAdmin wifiAdmin;
    NfcAdmin nfcAdmin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dad=(MainActivity)getActivity();
        wifiAdmin=dad.getWifiAdmin();
        nfcAdmin=dad.getNfcAdmin();
        submitFragment=dad.getSubmitFragment();
    }


    void submit(NfcAdmin.myNfcRecord record){
        submitFragment.addContent(record);
        submitFragment.list.postInvalidate();
    }
}
