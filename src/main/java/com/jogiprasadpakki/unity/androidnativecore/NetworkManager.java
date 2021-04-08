package com.jogiprasadpakki.unity.androidnativecore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.unity3d.player.UnityPlayer;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Jogi Prasad Pakki on 15-May-18.
 * No one allowed to use or modify this script.
 * If you any questions mail me to jogiprasadpakki@gmail.com
 * Copy right 2018 All reserved by Jogi Prasad Pakki.
 */


public class NetworkManager {

    private android.net.wifi.WifiManager wifiManager;
    private ConnectivityManager connectivityManager;
    private WifiInfo wifiInfo;
    private TelephonyManager telephonyManager;

    @SuppressLint("MissingPermission")
    public NetworkManager(String key) {
        Context context = UnityPlayer.currentActivity.getApplicationContext();
        wifiManager = (android.net.wifi.WifiManager) context.getSystemService(context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
    }

    //wifi
    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }
    @SuppressLint("MissingPermission")
    public boolean isWifiConnected() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getNetworkId() == -1) {
            return false;
        } else {
            return true;
        }
    }
    @SuppressLint("MissingPermission")
    public void setWifiEnabled(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }
    @SuppressLint("MissingPermission")
    public String getSsid() {
        String ssid = null;
         NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
    public String getMac() {
        return wifiInfo.getMacAddress();
    }
    public String getIp() {
        String ipaddress = null;
        byte[] ip = BigInteger.valueOf(wifiInfo.getIpAddress()).toByteArray();
        try {
            InetAddress inetAddress = InetAddress.getByAddress(ip);
            ipaddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipaddress;
    }
    public int getLinkSpeed() {
        return wifiInfo.getLinkSpeed();
    }
    public String getBssd() {
        return wifiInfo.getBSSID();
    }
    public int getNetworkID() {
        return wifiInfo.getNetworkId();
    }

    @SuppressLint("MissingPermission")
    public String getTelephoneId() {
        return telephonyManager.getDeviceId();

    }
    @SuppressLint("MissingPermission")
    public String getSimSerialNumber(){
       return telephonyManager.getSimSerialNumber();
    }
    public boolean isMobileDataEnabled(){
        boolean isEnabled = false;
        try {
            Class clas = Class.forName(connectivityManager.getClass().getName());
            Method method = clas.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            isEnabled = (boolean) method.invoke(connectivityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isEnabled;
    }

    //MobileData

}
