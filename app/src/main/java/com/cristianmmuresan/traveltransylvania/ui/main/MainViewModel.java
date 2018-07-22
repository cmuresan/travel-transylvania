package com.cristianmmuresan.traveltransylvania.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<PlaceEntry>> places;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        places = database.placeDao().loadAllPlaces();
    }

    public LiveData<List<PlaceEntry>> getPlaces() {
        return places;
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
}
