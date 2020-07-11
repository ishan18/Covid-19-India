package com.example.android.covid_19india;

import java.util.ArrayList;

public class StateReport {

    String stateName;
    String upadtedTime;
    ArrayList<Integer> data;
    boolean isDataVisible;

    StateReport(String name,String time,ArrayList<Integer> data){
        this.stateName=name;
        this.upadtedTime=time;
        this.data=data;
        isDataVisible=false;
    }

    public String getStateName() {
        return stateName;
    }

    public String getUpadtedTime() {
        return upadtedTime;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setDataVisible(boolean dataVisible) {
        isDataVisible = dataVisible;
    }

    public boolean isDataVisible() {
        return this.isDataVisible;
    }
}
