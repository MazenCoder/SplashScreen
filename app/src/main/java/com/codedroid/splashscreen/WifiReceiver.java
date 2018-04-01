package com.codedroid.splashscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

    String TAG = WifiReceiver.class.getSimpleName();

    private Context mContext;
//    private boolean connected = true;



//    @Override
//    public void onReceive(final Context context, final Intent intent) {
//
//        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
//                && WifiManager.WIFI_STATE_ENABLED == wifiState) {
//            if (Log.isLoggable(TAG, Log.VERBOSE)) {
//                Log.v(TAG, "Wifi is now enabled");
//            }
//            context.startService(new Intent(context, WifiService.class));
//        }
//    }
//}


//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//        if(info != null && info.isConnected()) {
//            // Do your work.
//
////            Toast.makeText(context, "is connected", Toast.LENGTH_LONG).show();
//
//            // e.g. To check the Network Name or other info:
//            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            String ssid = wifiInfo.getSSID();
//
//            Toast.makeText(context, ssid, Toast.LENGTH_LONG).show();
//        }
//    }
//}



    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
//        connected = intent.getBooleanExtra("connection",true);

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    networkInfo.isConnected()) {

                // Wifi is connected
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();

                Toast.makeText(context, "Wifi Connected "+ssid, Toast.LENGTH_LONG).show();

                Log.e(TAG, " -- Wifi connected --- " + " SSID " + ssid );

                // Stop Service
                context.stopService(new Intent(context, WifiService.class));
                

            }else {

                Toast.makeText(context, "Wifi Disconnected", Toast.LENGTH_LONG).show();
                Check.buildDialog(context).show();



//                ResultReceiver resultWifi = new ResultWifi(null);
//                // Start Service
//                Intent intentService = new Intent(context, WifiService.class);
//                intent.putExtra("resultWifi", resultWifi);
//                context.startService(intentService);
                context.startService(new Intent(context, WifiService.class));

            }
        }

        else if (intent.getAction().equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            if (wifiState == WifiManager.WIFI_STATE_DISABLED)
            {
                Log.e(TAG, " ----- Wifi  Disconnected ----- ");

                Toast.makeText(context, "Wifi Disconnected", Toast.LENGTH_LONG).show();
            }

        }
    }

}
