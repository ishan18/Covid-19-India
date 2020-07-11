package com.example.android.covid_19india;

import java.util.ArrayList;

public class Report {
    String date;
    ArrayList<Integer> data;

    public Report(String date,ArrayList<Integer> data){
        this.data=data;
        this.date=date;
    }

    public String getDate() {
        return date;
    }
    public ArrayList<Integer> getData(){
        return data;
    }
}
