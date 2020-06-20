package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Integer current_position = 0;
    private BroadcastReceiver receiver;
    private boolean airmode_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.airmode_status = isAirplaneModeOn(getApplicationContext());

        Log.d("AIRMODE", "Airmode is " + this.airmode_status);

        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().intern() == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
                    Log.d("AirplaneMode", "Service state changed");
                    boolean airplane_active = isAirplaneModeOn(context);
                    Log.d("AIRMODE", "Airplane mode is " + airplane_active);
                }
            }
        };

        registerReceiver(receiver, filter);



        if (this.airmode_status){
            Log.d("AIRMODE", "Airmode is on");

            TextView airmodeMessage = findViewById(R.id.airmodeMessage);
            airmodeMessage.setText("AirMode ON");
            try{
                Sqlitedatabase db = new Sqlitedatabase(getApplicationContext());
                ArrayList<WeatherInfo> recieved_infos = db.getAllData();
                Log.d("DB", "Read from database " + recieved_infos);

                this.updateCurrentView(recieved_infos.get(0));

            }catch (IndexOutOfBoundsException e){
                Log.e("DB", "Cant get info from DB - AIRMODE ON");
                Intent goToNextActivity = new Intent(getApplicationContext(), AirmodeOnScreen.class);
                startActivity(goToNextActivity);

            }
            return;
        }

        Thread api_info = new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                Network net = new Network();

                try {
                    ArrayList<WeatherInfo> all_infos = net.getResponseFromHttpUrl(net.buildUrlForWeather());
                    Log.d("APIOUT", all_infos.get(0).getMax_temp().toString());
                    Sqlitedatabase db = new Sqlitedatabase(getApplicationContext());
                    db.insertData(all_infos);
                    Log.d("DB", "Inserted to database");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            }
        );
        api_info.start();
        try {
            api_info.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // String temperature = net.doInBackground();

        Sqlitedatabase db = new Sqlitedatabase(getApplicationContext());
        ArrayList<WeatherInfo> recieved_infos = db.getAllData();
        Log.d("DB", "Read from database " + recieved_infos);

        this.updateCurrentView(recieved_infos.get(0));


        System.out.print("END");
    }

    void updateCurrentView(WeatherInfo currentWeather){
        try{
            TextView currentViewDate = findViewById(R.id.currentViewDate);
            currentViewDate.setText(currentWeather.getApplicable_date());

            TextView minTemp = findViewById(R.id.minTemp);
            minTemp.setText(String.format("%.2f", currentWeather.getMin_temp()));

            TextView maxTemp = findViewById(R.id.maxTemp);
            maxTemp.setText(String.format("%.2f", currentWeather.getMax_temp()));

            TextView windSpeed = findViewById(R.id.windSpeed);
            windSpeed.setText(String.format("%.2f", currentWeather.getWind_speed()) + " km/h");

            TextView predictability = findViewById(R.id.predictability);
            predictability.setText(String.valueOf(currentWeather.getPredictability() + "%"));

            TextView weatherState = findViewById(R.id.weatherStateName);
            weatherState.setText(currentWeather.getWeather_state_name());

            ImageView img= (ImageView) findViewById(R.id.icon);
            int resId = getResId(currentWeather.getWeather_state_abbr());

            // W przypadku braku opisu pogody/innemu opisowi ikonka pozostaje bez zmian.
            if(resId != -1){img.setImageResource(resId);};

        }catch (IndexOutOfBoundsException e){
            Log.e("ERR", "Could not find date in recieved infos");
        }
    }

    private int getResId(String weather_state_abbr) {
        if(weather_state_abbr.equals("s")){
            return R.drawable.ic_s;
        }
        if(weather_state_abbr.equals("c")){
            return R.drawable.ic_c;
        }
        if(weather_state_abbr.equals("h")){
            return R.drawable.ic_h;
        }
        if(weather_state_abbr.equals("hc")){
            return R.drawable.ic_hc;
        }
        if(weather_state_abbr.equals("hr")){
            return R.drawable.ic_hr;
        }
        if(weather_state_abbr.equals("lc")){
            return R.drawable.ic_lc;
        }
        if(weather_state_abbr.equals("lr")){
            return R.drawable.ic_lr;

        }if(weather_state_abbr.equals("sl")){
            return R.drawable.ic_sl;

        }if(weather_state_abbr.equals("sn")){
            return R.drawable.ic_sn;

        }if(weather_state_abbr.equals("t")){
            return R.drawable.ic_t;
        }
        return -1;
    }

    public void nextButtonClick(View v){
        Sqlitedatabase db = new Sqlitedatabase(getApplicationContext());
        ArrayList<WeatherInfo> recieved_infos = db.getAllData();
        Log.d("DB", "Read from database " + recieved_infos);


        try{
            WeatherInfo currentWeather = recieved_infos.get(this.current_position+1);
            this.updateCurrentView(currentWeather);
            this.current_position += 1;
        }catch (IndexOutOfBoundsException e){
            Log.d("Days", "This is last record");
        }
    }


    public void prevButtonClick(View v){
        Sqlitedatabase db = new Sqlitedatabase(getApplicationContext());
        ArrayList<WeatherInfo> recieved_infos = db.getAllData();
        Log.d("DB", "Read from database " + recieved_infos);

        if (this.current_position == 0){
            Log.d("DB", "You are viewing today's wather");
            return;
        }
        WeatherInfo currentWeather = recieved_infos.get(this.current_position-1);
        this.updateCurrentView(currentWeather);
        this.current_position -= 1;
    }


    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

}
