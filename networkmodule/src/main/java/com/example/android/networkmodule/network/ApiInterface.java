package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.Weather;

public interface ApiInterface {
    void getWeather(double lat, double lon, CallbackInterface<Weather> result);
}
