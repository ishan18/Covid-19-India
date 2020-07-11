package com.example.android.covid_19india;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.InputQueue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class StateWiseActivity extends AppCompatActivity {

    RecyclerView stateList;
    RecyclerView.LayoutManager layoutManager;
    DataAdapter dataAdapter;
    ArrayList<StateReport> stateReportArrayList;
    FrameLayout frameLayout;
    ConstraintLayout constraintLayout;
    final String stateUrl="https://api.covid19india.org/data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise);

        try {
            this.getSupportActionBar().setTitle("State-wise Report");
            this.getSupportActionBar().setIcon(R.drawable.icon);
        }catch (NullPointerException ignored){}

        stateList=(RecyclerView) findViewById(R.id.state_list);
        layoutManager=new LinearLayoutManager(this);
        stateList.setLayoutManager(layoutManager);
        stateList.scrollToPosition(3);
        constraintLayout=(ConstraintLayout)findViewById(R.id.loading);
        frameLayout=(FrameLayout)findViewById(R.id.no_connection);

        AsyncCheckInternet asyncCheckInternet=new AsyncCheckInternet();
        asyncCheckInternet.execute();
    }

    private class AsyncStateWiseReport extends AsyncTask<String,Void, ArrayList<StateReport>>{

        @Override
        protected void onPostExecute(final ArrayList<StateReport> stateReports) {
            super.onPostExecute(stateReports);

            stateReportArrayList=stateReports;
            dataAdapter=new DataAdapter(StateWiseActivity.this,stateReportArrayList);
            stateList.setAdapter(dataAdapter);

            constraintLayout.setVisibility(View.GONE);
            stateList.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

//            final ItemTouchHelper helper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN|ItemTouchHelper.LEFT
//                    |ItemTouchHelper.RIGHT|ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
//                @Override
//                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                    int from=viewHolder.getAdapterPosition();
//                    int to=target.getAdapterPosition();
//
//                    Collections.swap(stateReports,from,to);
//                    dataAdapter.notifyItemMoved(from,to);
//                    return true;
//                }
//
//                @Override
//                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                    stateReports.remove(viewHolder.getAdapterPosition());
//                    dataAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                }
//            });
//
//            helper.attachToRecyclerView(stateList);
        }

        @Override
        protected ArrayList<StateReport> doInBackground(String... strings) {

            return QueryUtils.extractStateWiseReport(strings[0]);
        }
    }

    private class AsyncCheckInternet extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                stateList.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                AsyncStateWiseReport asyncStateWiseReport=new AsyncStateWiseReport();
                asyncStateWiseReport.execute(stateUrl);
            }else {
                stateList.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        }

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
    }
}
