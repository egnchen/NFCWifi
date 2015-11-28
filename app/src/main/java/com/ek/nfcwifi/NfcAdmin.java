package com.ek.nfcwifi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eyeKill on 15/9/27.
 */
public class NfcAdmin {
    NfcAdapter nfcAdapter;
    Context context;
    
    private ArrayList<myNfcRecord> RecordList=new ArrayList<myNfcRecord>();

    private ArrayList<myNfcRecord> RecordList_get=new ArrayList<myNfcRecord>();
    
    NfcAdmin(Context context){
        this.context=context;
        //检测是否支持nfc
        nfcAdapter= NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter==null) Toast.makeText(context, "nfc not supported.", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context,"nfc supported.",Toast.LENGTH_SHORT).show();
    }

    public NfcAdapter getNfcAdapter(){return nfcAdapter;}

    ArrayList<myNfcRecord> readTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage)rawArray[0];
        NdefRecord[] mNdefRecord = mNdefMsg.getRecords();
        RecordList_get=new ArrayList<myNfcRecord>();
        for(NdefRecord n:mNdefRecord) {
            Log.i(context.getPackageName(), n.toString());
            if (n != null && n.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
                Uri uri = n.toUri();
                Log.i(context.getPackageName(), uri.toString());
                if (uri.getScheme().equals("eknfc")) {
                    if (uri.getHost().equals("wifi")) {
                        try {
                            JSONObject j=new JSONObject(uri.toString().substring(uri.getScheme().length() + uri.getHost().length() + 4));
                            RecordList_get.add(getRecord_wifi(j));
                        } catch (JSONException e) {
                            Log.e(context.getPackageName(),uri.toString().substring(uri.getScheme().length() + uri.getHost().length() + 4));
                            e.printStackTrace();
                        }
                    }
                }
                else RecordList_get.add(getRecord_url(uri.toString())); //url message
            }
        }
        return null;
    }

    void appendRecord(myNfcRecord a){
        RecordList.add(a);
    }

    void clearRecords(){
        RecordList.clear();
    }


    void writeTag(Intent intent) throws IOException {
        Tag tag=(Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef=Ndef.get(tag);
        try {
            ndef.connect();
            NdefRecord[] records=new NdefRecord[RecordList.size()];
            for(int i=0;i<RecordList.size();i++) records[i]=RecordList.get(i).getNdefRecord();
            NdefMessage ndefMessage=new NdefMessage(records);
            ndef.writeNdefMessage(ndefMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        Toast.makeText(context,"Writing completed",Toast.LENGTH_SHORT).show();
    }

    public myNfcRecord getRecord_wifi(WifiConfiguration conf){return new myNfcRecord(conf);}
    public myNfcRecord getRecord_wifi(JSONObject j){return getRecord_wifi(WifiAdmin.getWifiConfFromJson(j));}

    public myNfcRecord getRecord_url(String url){return new myNfcRecord(url);}
    public class myNfcRecord{
        NdefRecord ndefRecord;
        int msgType;
        String value;

        public static final int EKNFC_TYPE_WIFI=1;
        public static final int EKNFC_TYPE_URL=2;
        public static final int EKNFC_TYPE_STARTAPP=3;

        public myNfcRecord(WifiConfiguration conf){
            String url_scheme="eknfc";
            String url_host="wifi";
            String url_path=WifiAdmin.createWifiJson(conf.SSID,conf.preSharedKey).toString();
            msgType=EKNFC_TYPE_WIFI;
            ndefRecord=NdefRecord.createUri(url_scheme+"://"+url_host+"/"+url_path);
            value=conf.SSID;
        }

        public myNfcRecord(String url){
            ndefRecord=NdefRecord.createUri(url);
            msgType=EKNFC_TYPE_URL;
            value=url;
        }

        public myNfcRecord(String packageName,int how){
            String url_scheme="eknfc";
            String url_host="app";
            String url_path=packageName.replace('.','-');
            msgType=EKNFC_TYPE_STARTAPP;
            ndefRecord=NdefRecord.createUri(url_scheme+"://"+url_host+"/"+url_path);
            value=packageName;
        }


        public NdefRecord getNdefRecord(){
            return ndefRecord;
        }

        @Override
        public String toString() {
            return value;
        }
    }


}
