package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Sqlitedatabase extends SQLiteOpenHelper {
    private String DATABASE_MARKSTABLE = "Weather";
    private String w_name = "weather_state_name";
    private String w_abbr = "weather_state_abbr";
    private String w_ap_date ="applicable_date";
    private String w_min_temp ="min_temp";
    private String w_max_temp ="max_temp";
    private String w_wind_speed ="wind_speed";
    private String w_pred ="predictability";

    public Sqlitedatabase(Context context) {
        super(context, "WeatherDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE " + DATABASE_MARKSTABLE + " (" +
                w_ap_date + " TEXT PRIMARY KEY, " +
                w_name + " TEXT, " +
                w_abbr + " TEXT, " +
                w_min_temp + " REAL, " +
                w_max_temp + " REAL, " +
                w_wind_speed + " REAL, " +
                w_pred + " INTEGER);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertData(ArrayList<WeatherInfo> infos) {
        SQLiteDatabase db = this.getWritableDatabase();



        for (WeatherInfo record : infos){
            ContentValues values = new ContentValues();
            values.put(w_ap_date, record.getApplicable_date());
            values.put(w_name, record.getWeather_state_name());
            values.put(w_abbr, record.getWeather_state_abbr());
            values.put(w_min_temp, record.getMin_temp());
            values.put(w_max_temp, record.getMax_temp());
            values.put(w_wind_speed, record.getWind_speed());
            values.put(w_pred, record.getPredictability());

            // Inserting Row
            db.insert(DATABASE_MARKSTABLE, null, values);
        }

        db.close(); // Closing database connection
    }


    public ArrayList<WeatherInfo> getAllData() {
        ArrayList<WeatherInfo> contactList = new ArrayList<WeatherInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_MARKSTABLE;

        Log.d("DBQ", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WeatherInfo info = new WeatherInfo();
                info.setApplicable_date(cursor.getString(cursor.getColumnIndex(w_ap_date)));
                info.setWeather_state_name(cursor.getString(1));
                info.setWeather_state_abbr(cursor.getString(2));
                info.setMin_temp(cursor.getDouble(3));
                info.setMax_temp(cursor.getDouble(4));
                info.setWind_speed(cursor.getDouble(5));
                info.setPredictability(cursor.getInt(6));
                // Adding record to list
                contactList.add(info);
            } while (cursor.moveToNext());
        }


        // return contact list
        return contactList;
    }
}
