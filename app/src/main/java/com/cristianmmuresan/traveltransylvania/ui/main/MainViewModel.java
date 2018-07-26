package com.cristianmmuresan.traveltransylvania.ui.main;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceActivity;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<PlaceEntry>> places;
    private LiveData<List<PlaceEntry>> placesBySearch;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private final AppDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        places = database.placeDao().loadAllPlaces();
        placesBySearch = Transformations.switchMap(filterLiveData, new Function<String, LiveData<List<PlaceEntry>>>() {
            @Override
            public LiveData<List<PlaceEntry>> apply(String input) {
                String query = input.toLowerCase() + "%";
                return database.placeDao().search(query);
            }
        });
    }

    public LiveData<List<PlaceEntry>> getPlaces() {
        return places;
    }

    public LiveData<List<PlaceEntry>> getPlacesBySearch() {
        return placesBySearch;
    }

    public void setQuery(String query) {
        filterLiveData.setValue(query);
    }

    public List<Object> getListWithHeaders(@NonNull List<PlaceEntry> placeEntries) {
        List<Object> allPlaces = new ArrayList<>();
        List<PlaceEntry> favorites = new ArrayList<>();
        List<PlaceEntry> cities = new ArrayList<>();
        List<PlaceEntry> villages = new ArrayList<>();
        List<PlaceEntry> castles = new ArrayList<>();
        Resources resources = this.getApplication().getResources();

        for (PlaceEntry place : placeEntries) {
            if (place.isFavorite()) {
                favorites.add(place);
            }

            if (place.isCity()) {
                cities.add(place);
            } else if (place.isVillage()) {
                villages.add(place);
            } else if (place.isCastle()) {
                castles.add(place);
            }
        }

        if (favorites.size() > 0) {
            allPlaces.add(resources.getString(R.string.favorites));
            allPlaces.addAll(favorites);
        }
        if (cities.size() > 0) {
            allPlaces.add(resources.getString(R.string.cities));
            allPlaces.addAll(cities);
        }
        if (villages.size() > 0) {
            allPlaces.add(resources.getString(R.string.villages));
            allPlaces.addAll(villages);
        }
        if (castles.size() > 0) {
            allPlaces.add(resources.getString(R.string.castles));
            allPlaces.addAll(castles);
        }
        return allPlaces;
    }

    public void startPlaceActivity(int placeId) {
        Intent placeActivityIntent = new Intent(this.getApplication(), PlaceActivity.class);
        placeActivityIntent.putExtra(PlaceActivity.PLACE_ID_KEY, placeId);
        this.getApplication().startActivity(placeActivityIntent);
    }
}
