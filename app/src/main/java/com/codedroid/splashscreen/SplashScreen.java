package com.codedroid.splashscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    Handler handler;
    // WifiReceiver
    private WifiReceiver wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        wifi = new WifiReceiver();

        handler = new Handler();

//        CoordinatorLayout layout = (CoordinatorLayout)findViewById(R.id.splash_screen);
//        AnimationDrawable animation = (AnimationDrawable)layout.getBackground();
//        animation.setEnterFadeDuration(2000);
//        animation.setExitFadeDuration(4000);
//        animation.start();


        progressBar = (ProgressBar)findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.getProgressDrawable().setColorFilter(
//                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

        // Start lengthy operation in a background thread

        StartThread();
    }

    private void StartThread(){

        new Thread(new Runnable() {
            public void run() {
                doWork();
                if (Check.isConnected(getApplicationContext())) {
                    startApp();
                    finish();
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            buildDialog(SplashScreen.this).show();
                            Toast.makeText(getApplicationContext(), "no internet!", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        }).start();
    }

//    public void Test() {
//        doWork();
//        if (isConnected(getApplicationContext())) {
//            startApp();
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), "no internet!", Toast.LENGTH_LONG).show();
//        }
//    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=1) {
            try {
                Thread.sleep(50);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
//                Timber.e(e.getMessage());
            }
        }
    }


    /**
     *  Test Code
     */

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    /**
     *   End Test Code
     */
    private void startApp() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wifi, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifi);
        stopService(new Intent(this, WifiService.class));

    }

}