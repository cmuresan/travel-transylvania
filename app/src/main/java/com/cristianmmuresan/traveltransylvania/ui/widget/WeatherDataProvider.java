package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.content.Context;
import android.util.Log;

import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.AppExecutors;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.example.android.networkmodule.model.Weather;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.ApiInterface;
import com.example.android.networkmodule.network.CallbackInterface;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class WeatherDataProvider {
    private final AppDatabase database;
    private final List<PlaceEntry> places;
    private final ApiInterface apiInterface;

    public WeatherDataProvider(Context context, List<PlaceEntry> places) {
        database = AppDatabase.getInstance(context);
        this.places = places;
        apiInterface = new ApiImpl();
    }

    public void updateWeatherData() {
        for (final PlaceEntry placeEntry : places) {
            apiInterface.getWeather(placeEntry.getLatitude(), placeEntry.getLongitude(), new CallbackInterface<Weather>() {
                @Override
                public void success(Weather response) {
                    Log.d(TAG, "success: ");
                    if (response == null) return;
                    if (response.getMain() == null) return;
                    if (response.getWeather() == null) return;
                    if (response.getWeather().size() == 0) return;

                    placeEntry.setMaxTemp(response.getMain().getTempMax());
                    placeEntry.setMinTemp(response.getMain().getTempMin());
                    placeEntry.setIconId(response.getWeather().get(0).getIcon());

                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.placeDao().updatePlace(placeEntry);
                        }
                    });
                }

                @Override
                public void failure(String errorMessage, String errorCode) {
                    Log.d(TAG, "failure: ");
                }
            });
        }
    }
}
