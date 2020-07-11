package com.example.android.covid_19india;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.net.URL;
import java.net.URLConnection;

public class InternetReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

//        boolean internetAvailable;
//        try {
//            URLConnection urlConnection=new URL("https://www.google.com/").openConnection();
//            urlConnection.setConnectTimeout(200);
//            urlConnection.connect();
//            internetAvailable= true;
//        } catch (Exception e) {
//            internetAvailable= false;
//        }
//        if(internetAvailable)
        context.sendBroadcast(new Intent(), AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
