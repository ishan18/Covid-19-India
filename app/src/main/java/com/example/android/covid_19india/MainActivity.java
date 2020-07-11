package com.example.android.covid_19india;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView deathText,confirmedText,recoveredText;
    TextView totalDeathText,totalConfirmedText,totalRecoveredText,totalActiveText;
    ConstraintLayout constraintLayout;
    TextView date;
    FrameLayout frameLayout1,frameLayout;
    Button stateButton;
    ImageView doctorImage;
    final String todayUrl="https://api.covid19india.org/data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.getSupportActionBar().hide();
        }catch (NullPointerException ignored){}

        stateButton=(Button) findViewById(R.id.state_data);
        date=(TextView)findViewById(R.id.date);

        doctorImage=(ImageView)findViewById(R.id.image_doctor);

        deathText=(TextView)findViewById(R.id.death_count);
        confirmedText=(TextView)findViewById(R.id.confirmed_count);
        recoveredText=(TextView)findViewById(R.id.recovered_count);

        totalConfirmedText=(TextView)findViewById(R.id.total_confirmed);
        totalDeathText=(TextView)findViewById(R.id.total_death);
        totalRecoveredText=(TextView)findViewById(R.id.total_recovered);
        totalActiveText=(TextView)findViewById(R.id.total_active);

        constraintLayout=(ConstraintLayout)findViewById(R.id.loading);
        frameLayout1=(FrameLayout)findViewById(R.id.current_data);
        frameLayout=(FrameLayout)findViewById(R.id.no_connection);

        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,StateWiseActivity.class);
                startActivity(intent);
            }
        });

        AsyncCheckInternet asyncCheckInternet=new AsyncCheckInternet();
        asyncCheckInternet.execute();
    }

    private class AsyncTodayReport extends AsyncTask<String,Void, Report>{

        @Override
        protected void onPostExecute(Report report) {
            super.onPostExecute(report);

            if(report==null)
                return;

            ArrayList<Integer> integers=report.getData();

            Typeface regularRoboto=Typeface.createFromAsset(MainActivity.this.getAssets(),"Roboto-Regular.ttf");

            date.setText(report.getDate());
            date.setTypeface(regularRoboto);

            confirmedText.setText(String.valueOf(integers.get(0)));
            deathText.setText(String.valueOf(integers.get(1)));
            recoveredText.setText(String.valueOf(integers.get(2)));

            totalConfirmedText.setText(String.valueOf(integers.get(3)));
            totalDeathText.setText(String.valueOf(integers.get(4)));
            totalRecoveredText.setText(String.valueOf(integers.get(5)));
            totalActiveText.setText(String.valueOf(integers.get(3)-integers.get(4)-integers.get(5)));

            confirmedText.setTypeface(regularRoboto);
            deathText.setTypeface(regularRoboto);
            recoveredText.setTypeface(regularRoboto);
            totalRecoveredText.setTypeface(regularRoboto);
            totalDeathText.setTypeface(regularRoboto);
            totalConfirmedText.setTypeface(regularRoboto);
            totalActiveText.setTypeface(regularRoboto);

            frameLayout1.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }

        @Override
        protected Report doInBackground(String... strings) {

            return QueryUtils.extractTodayReport(strings[0]);
        }
    }

    private class AsyncCheckInternet extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                URLConnection urlConnection=new URL("https://www.google.com/").openConnection();
                urlConnection.setConnectTimeout(200);
                urlConnection.connect();
                return true;
            }catch (Exception e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){
                frameLayout1.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                AsyncTodayReport asyncTodayReport=new AsyncTodayReport();
                asyncTodayReport.execute(todayUrl);
            }else {
                constraintLayout.setVisibility(View.GONE);
                frameLayout1.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
