package com.example.android.covid_19india;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ValueIterator;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class CovidStatWidget extends AppWidgetProvider {

    final static String todayUrl="https://api.covid19india.org/data.json";
    static Context mContext;
    static AppWidgetManager mAppWidgetManager;
    static int mAppWidgetId;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.covid_stats_widget);

            ComponentName componentName=new ComponentName(context,CovidStatWidget.class);

            AppWidgetManager.getInstance(context).updateAppWidget(componentName,remoteViews);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        mContext=context;
        mAppWidgetId=appWidgetId;
        mAppWidgetManager=appWidgetManager;

        AsyncCheckInternet asyncCheckInternet=new AsyncCheckInternet();
        asyncCheckInternet.execute();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static class AsyncTodayReport extends AsyncTask<String,Void,Report> {
        @Override
        protected void onPostExecute(Report report) {
            super.onPostExecute(report);

            ArrayList<Integer> integers=report.getData();
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.covid_stats_widget);

            views.setTextViewText(R.id.date_text,mContext.getResources().getString(R.string.today_data,report.getDate()));
            views.setTextViewText(R.id.confirmed_count,String.valueOf(integers.get(0)));
            views.setTextViewText(R.id.death_count,String.valueOf(integers.get(1)));
            views.setTextViewText(R.id.recovered_count,String.valueOf(integers.get(2)));
            views.setTextViewText(R.id.total_confirmed,String.valueOf(integers.get(3)));
            views.setTextViewText(R.id.total_death,String.valueOf(integers.get(4)));
            views.setTextViewText(R.id.total_recovered,String.valueOf(integers.get(5)));

            views.setViewVisibility(R.id.loading,View.GONE);
            views.setViewVisibility(R.id.data,View.VISIBLE);
            views.setViewVisibility(R.id.no_connection,View.GONE);

//            Intent updateIntent=new Intent(mContext,CovidStatWidget.class);
//            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,new int[]{mAppWidgetId});
//            PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,mAppWidgetId,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//            views.setOnClickPendingIntent(R.id.reload,pendingIntent);

            mAppWidgetManager.updateAppWidget(mAppWidgetId, views);
        }

        @Override
        protected Report doInBackground(String... strings) {

            return QueryUtils.extractTodayReport(strings[0]);
        }
    }

    private static class AsyncCheckInternet extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            RemoteViews views=new RemoteViews(mContext.getPackageName(),R.layout.covid_stats_widget);
            views.setViewVisibility(R.id.loading,View.GONE);

            if(aBoolean){
                views.setViewVisibility(R.id.loading,View.VISIBLE);
                views.setViewVisibility(R.id.data, View.GONE);
                views.setViewVisibility(R.id.no_connection,View.GONE);
                AsyncTodayReport asyncTodayReport=new AsyncTodayReport();
                asyncTodayReport.execute(todayUrl);
            }
            else {
                views.setViewVisibility(R.id.loading,View.GONE);
                views.setViewVisibility(R.id.data,View.GONE);
                views.setViewVisibility(R.id.no_connection,View.VISIBLE);
                mAppWidgetManager.updateAppWidget(mAppWidgetId, views);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URLConnection urlConnection=new URL("https://www.google.com/").openConnection();
                urlConnection.setConnectTimeout(200);
                urlConnection.connect();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}

