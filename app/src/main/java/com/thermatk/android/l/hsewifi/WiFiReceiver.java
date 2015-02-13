package com.thermatk.android.l.hsewifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WiFiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("HSEWIFI", "1. RECEIVED");
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = intent.getParcelableExtra(wifiMan.EXTRA_NETWORK_INFO);

        if (intent.getAction().equals(wifiMan.NETWORK_STATE_CHANGED_ACTION) && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            Log.i("HSEWIFI", "2. CONNECTED");
            WifiInfo wifiInfo = intent.getParcelableExtra(wifiMan.EXTRA_WIFI_INFO);
            if (wifiInfo == null) {
                Log.i("HSEWIFI", "2.E wifiInfo==null");
                return;
            }
            String wifiName = wifiInfo.getSSID();
            Log.i("HSEWIFI", "2. " + wifiName + " BSSID: " + wifiInfo.getBSSID() + " STRENGTH: " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4));
            if (wifiName.equals("\"HSE\"") || wifiName.equals("HSE")) {
                Log.i("HSEWIFI", "3.A HSE");
                Toast.makeText(context, context.getString(R.string.connectedto) + " " + "HSE", Toast.LENGTH_SHORT).show();
                context.startService(new Intent(context, HSEConnect.class));
            }  else if(wifiName.equals("\"MosMetro_Free\"") || wifiName.equals("MosMetro_Free")) {
                Log.i("HSEWIFI", "3.B MOSMETRO");
                Toast.makeText(context, context.getString(R.string.connectedto) + " " + "MosMetro", Toast.LENGTH_SHORT).show();
                context.startService(new Intent(context, MosMetro.class));
            }
            Log.i("HSEWIFI", "3.C WRONG NETWORK, END");
        }
    }
}
