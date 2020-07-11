package com.example.android.covid_19india;

import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public static Report extractTodayReport(String url1) {

        String jsonResponse=init(url1);

        if(jsonResponse==null || jsonResponse.isEmpty())
            return null;

        ArrayList<Integer> report=new ArrayList<>();
        Report data=null;
        JSONObject root=null;
        try {
            root=new JSONObject(jsonResponse);
            JSONArray dailyData=root.getJSONArray("cases_time_series");
            JSONObject todayData=dailyData.getJSONObject(dailyData.length()-1);
            report.add(todayData.getInt("dailyconfirmed"));
            report.add((todayData.getInt("dailydeceased")));
            report.add(todayData.getInt("dailyrecovered"));
            report.add(todayData.getInt("totalconfirmed"));
            report.add(todayData.getInt("totaldeceased"));
            report.add(todayData.getInt("totalrecovered"));
            String date=todayData.getString("date");
            data=new Report(date,report);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<StateReport> extractStateWiseReport(String url1){

        String jsonResponse=init(url1);

        if(jsonResponse==null || jsonResponse.isEmpty())
            return null;

        JSONObject root=null;

        ArrayList<StateReport> stateReportArrayList=new ArrayList<>();
        try {
            root=new JSONObject(jsonResponse);
            JSONArray stateWiseReportArray=root.getJSONArray("statewise");
            for(int i=1;i<stateWiseReportArray.length();i++){
                StateReport sample=extractStateReport(stateWiseReportArray.getJSONObject(i));
                if(sample!=null)
                    stateReportArrayList.add(sample);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stateReportArrayList;
    }

    private static StateReport extractStateReport(JSONObject jsonObject) {

        String name;
        String updatedOn;
        ArrayList<Integer> integers=new ArrayList<>();
        StateReport stateReport=null;
        try {
            integers.add(jsonObject.getInt("active"));
            integers.add(jsonObject.getInt("confirmed"));
            integers.add(jsonObject.getInt("deaths"));
            integers.add(jsonObject.getInt("recovered"));

            name=jsonObject.getString("state");
            updatedOn=jsonObject.getString("lastupdatedtime");

            stateReport=new StateReport(name,updatedOn,integers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stateReport;
    }

    private static String init(String url1){
        if(url1==null)
            return null;
        URL url=null;
        try {
            url=new URL(url1);
        }catch (MalformedURLException e){}

        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        String jsonResponse="";

        if(url==null)
            return null;
        try{
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(1000);
            urlConnection.setReadTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream=urlConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

            StringBuilder output=new StringBuilder();
            String line=bufferedReader.readLine();
            while (line!=null)
            {
                output.append(line);
                line=bufferedReader.readLine();
            }
            jsonResponse=output.toString();
        }catch (IOException e){}
        finally {
            if(urlConnection!=null)
                urlConnection.disconnect();
            if(inputStream!=null)
                try {
                    inputStream.close();
                }catch (IOException e){}
        }

        return jsonResponse;
    }
}
