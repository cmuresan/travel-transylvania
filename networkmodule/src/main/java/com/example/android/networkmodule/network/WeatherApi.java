package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface WeatherApi {
    @GET("weather")
    Call<Weather> getWeather(@Query("lat") double lat, @Query("lon") double lon,
                                   @Query("appid") String apiAppid, @Query("units") String units);
}
