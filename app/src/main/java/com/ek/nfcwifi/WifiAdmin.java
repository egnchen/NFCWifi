package com.ek.nfcwifi;

/**
 * Created by eyeKill on 15/9/13.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WifiAdmin {
    private Context dad;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfigurations;
    public WifiAdmin(Context context){
        //取得WifiManager对象
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo对象
        mWifiInfo=mWifiManager.getConnectionInfo();
        dad=context;
    }

    WifiManager getmWifiManager(){return mWifiManager;}
    //打开wifi
    public void openWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }
    //关闭wifi
    public void closeWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }
    //切换wifi状态（好像和上面两个没什么区别哈哈哈）
    public void switchWifiStatus(){
        mWifiManager.setWifiEnabled(!mWifiManager.isWifiEnabled());
    }

    public int getWifiStatus(){return mWifiManager.getWifiState();}

    public WifiInfo getWifiInfo(){mWifiInfo=mWifiManager.getConnectionInfo();return mWifiInfo;}

    //扫描当前所有的wifi
    public void scanWifi(){
        mWifiManager.startScan();
    }

    //获取wifi扫描结果
    public List<ScanResult> getScanList(){
        mWifiList=mWifiManager.getScanResults();
        return mWifiList;
    }

    //获取已经预先配置好的wifi
    public List<WifiConfiguration> getConfs(){
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();
        return mWifiConfigurations;
    }

    public List<String> getWifiNames(){
        getScanList();
        ArrayList<String> r=new ArrayList<String>();
        for(ScanResult s:mWifiList) r.add(s.SSID);
        return r;
    }


    //返回是否指定ssid的wifi已经配置好
    /**@return 网络configuration */
    public WifiConfiguration isConf(String SSID){
        for(int i = 0; i < mWifiConfigurations.size(); i++){
            if(mWifiConfigurations.get(i).SSID.equals(SSID)){//地址相同
                return mWifiConfigurations.get(i);
            }
        }
        return null;
    }

    //添加wificonfiguration 包含指定的ssid及密码
    /**@return 添加后的wifi ID */
    public int addWifiConf(String SSID,String pwd){
        int id=-1;
        getScanList();
        for(ScanResult r:mWifiList){
            if(r.SSID.equals(SSID)){
                WifiConfiguration conf=new WifiConfiguration();
                conf.SSID='\"'+SSID+'\"';
                conf.preSharedKey='\"'+pwd+'\"';
                conf.hiddenSSID=false;
                conf.status=WifiConfiguration.Status.ENABLED;
                id=mWifiManager.addNetwork(conf);
                if(id!=-1) return id;
            }
        }
        return id;
    }

    public int addWifiConf(JSONObject j){
        try{return addWifiConf(j.getString("SSID"),j.getString("pwd"));}
        catch(JSONException e){e.printStackTrace();}
        return -1;
    }

    //连接wifi
    /** @return 是否成功 */
    public boolean connectWifi(int id){
        if(mWifiManager.isWifiEnabled()) {
            getConfs();//获取新的wificonfiguration列表
            for (WifiConfiguration c : mWifiConfigurations) {
                if (c.networkId == id) {
                    while (!mWifiManager.enableNetwork(id, true)) ;//连接指定wifi
                    Log.i("NFCWifi", "Wifi connected id=" + id + " SSID=" + c.SSID);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public String getSecurity(String SSID){
        for(ScanResult x:mWifiList){
            if(x.SSID.equals(SSID)){
                if(x.capabilities.contains("WPA2-PSK-CCMP")) return "WPA/WPA2_PSK";
                else if(x.capabilities.contains("WEP")) return "WEP";
                else if(x.capabilities.contains("ESS")) return "none";
            }
        }
        return null;
    }


    static WifiConfiguration createConf(String SSID,String pwd){
        WifiConfiguration c=new WifiConfiguration();
        c.SSID=SSID;
        c.preSharedKey=pwd;
        c.hiddenSSID=false;
        return c;
    }

    static JSONObject createWifiJson(String SSID,String pwd){
        JSONObject m=new JSONObject();
        try{
            m.put("SSID",SSID);
            m.put("pwd",pwd);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return m;
    }

    static WifiConfiguration getWifiConfFromJson(JSONObject j){
        WifiConfiguration c=new WifiConfiguration();
        try{
            c.SSID=j.getString("SSID");
            c.preSharedKey=j.getString("pwd");
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }
        return c;
    }


    private ProgressDialog dialog;
    private String ssid;
    private Handler handler;
    public void waitUntilConnection(final ProgressDialog DIALOG,String SSID,Handler HANDLER){
        dialog=DIALOG;ssid=SSID;handler=HANDLER;
        openWifi();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i++ < 10) {//timeout >10s
                    if (getWifiInfo().getSSID()==ssid) {
                        try {
                            Thread.currentThread().sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        handler.sendEmptyMessage(1);//succeed
                        return;
                    }
                }
                handler.sendEmptyMessage(0);//failed
            }
        }).start();
    }
}