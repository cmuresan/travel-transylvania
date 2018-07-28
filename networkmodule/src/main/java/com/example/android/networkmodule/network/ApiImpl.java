package com.example.android.networkmodule.network;

import com.example.android.networkmodule.BuildConfig;
import com.example.android.networkmodule.WeatherConstants;
import com.example.android.networkmodule.model.Weather;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImpl implements ApiInterface {

    /**
     * API URL
     */
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/";
    private static final long TIME_TO_CONNECT = 30;
    private final WeatherApi apiService;

    public ApiImpl() {
        Retrofit retrofit = buildRetrofit();
        apiService = retrofit.create(WeatherApi.class);
    }

    private Retrofit buildRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .writeTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Override
    public void getWeather(double lat, double lon, CallbackInterface<Weather> callback) {
        Call<Weather> call = apiService.getWeather(lat, lon,
                BuildConfig.API_APPID, WeatherConstants.UNIT_METRIC);
        call.enqueue(new RestCallbackImpl<>(callback));
    }
}
