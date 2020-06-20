package com.example.myfirstapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;



import javax.net.ssl.HttpsURLConnection;


public class Network{
    private final static String WEATHERDB_BASE_URL=
            "https://www.metaweather.com/api/location";

    public String location = "523920";

    public URL buildUrlForWeather() {
        Uri builtUri = Uri.parse(WEATHERDB_BASE_URL + "/" + this.location + "/").buildUpon()
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    public ArrayList<WeatherInfo> getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        String responseText = null;

        try {
            InputStream in  = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                responseText =  scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

        // Convert String to json object

        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
        try {

            JSONObject full_resp = new JSONObject(responseText);

            JSONArray jsonArray = new JSONArray(full_resp.get("consolidated_weather").toString());

            if (jsonArray.length() != 0) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(new JSONObject(jsonArray.get(i).toString()));
                }
            }

        } catch (JSONException e) {
            Log.d("DATA", responseText);
            Log.d("FAIL", "FAILED TO PARSE RESPONSE");
            e.printStackTrace();
        }

        ArrayList<WeatherInfo> all_info = new ArrayList<>();

        for (JSONObject record : list){
            try {
                String weather_state_name = record.getString("weather_state_name");
                String weather_state_abbr = record.getString("weather_state_abbr");
                String applicable_date = record.getString("applicable_date");
                Double min_temp = record.getDouble("min_temp");
                Double max_temp = record.getDouble("max_temp");
                Double wind_speed = record.getDouble("wind_speed");
                Integer predictability = record.getInt("predictability");

                all_info.add(new WeatherInfo(weather_state_name, weather_state_abbr, applicable_date, min_temp, max_temp, wind_speed, predictability));
            } catch (JSONException e) {
            Log.d("Json object", record.toString());
            Log.d("FAIL", "FAILED TO GET VALUE");
            e.printStackTrace();
        }
        }

        Log.d("JsonObject",
                list.get(0).toString());



        return all_info;
    }

}