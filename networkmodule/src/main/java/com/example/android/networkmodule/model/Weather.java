package com.example.android.networkmodule.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather implements Parcelable {
    @SerializedName("coord")
    private Coordinates coord;
    @SerializedName("weather")
    private List<WeatherCondition> weather;
    @SerializedName("main")
    private WeatherInfo main;

    Weather(Parcel in) {
        weather = in.createTypedArrayList(WeatherCondition.CREATOR);
        main = in.readParcelable(WeatherInfo.class.getClassLoader());
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(weather);
        dest.writeParcelable(main, flags);
    }

    public List<WeatherCondition> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherCondition> weather) {
        this.weather = weather;
    }

    public WeatherInfo getMain() {
        return main;
    }

    public void setMain(WeatherInfo main) {
        this.main = main;
    }
}
