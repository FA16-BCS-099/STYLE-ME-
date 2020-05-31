package com.ba.styleme.ui;

import android.app.Application;
import android.util.Log;

import com.ba.styleme.R;
import com.kwabenaberko.openweathermaplib.constants.Lang;
import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;
import com.preference.PowerPreference;

public class StyleMe  extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        PowerPreference.init(this);

    }


}
