package com.codedroid.splashscreen;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

public class WifiService extends Service {
    public WifiService() { }

    private final static String TAG = WifiService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        boolean wifiConnected = intent.getBooleanExtra()

        final WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Need to wait a bit for the SSID to get picked up;
        // if done immediately all we'll get is null
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WifiInfo info = wifiManager.getConnectionInfo();
                String mac = info.getMacAddress();
                String ssid = info.getSSID();
                boolean isWifi = false;
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "The SSID & MAC are " + ssid + " " + mac);
                }

                createNotification(ssid, mac);
//                new MyAsyncTask().execute(5);
//                stopSelf();
            }
        }, 5000);//5000
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Creates a notification displaying the SSID & MAC addr
     */
    private void createNotification(String ssid, String mac) {

            Notification n = new Notification.Builder(this)
                    .setContentTitle("Wifi Connection")
                    .setContentText("Connected to " + ssid)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText("You're connected to " + ssid + " at " + mac))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .notify(0, n);

            Intent intent = new Intent("android.net.conn.CONNECTIVITY_CHANGE");
            intent.putExtra("connection", false);

    }

//    class MyAsyncTask extends AsyncTask<Integer, Void, Void> {
//
//        private final String TAG = MyAsyncTask.class.getSimpleName();
//
//
//        @Override
//        protected Void doInBackground(Integer... integers) {
//
//            int sleepTime = integers[0];
//
//            int ctr = 1;
//
//            // Dummy Long Operation
//            while(ctr <= sleepTime) {
//
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                ctr++;
//
//            }
//
//            startService(new Intent(getApplicationContext(), WifiService.class));
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            stopSelf();
//        }
//    }
}
