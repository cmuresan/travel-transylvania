package com.cristianmmuresan.traveltransylvania.datacomposer;

import android.content.Context;
import android.content.res.Resources;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.DatabaseInsertOperations;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;

import java.util.Arrays;
import java.util.List;

public class DataComposer {

    private final Context context;
    private AppDatabase appDatabase;
    private boolean isCity = true;
    private boolean isVillage = false;
    private boolean isCastle = false;
    private DatabaseInsertOperations databaseInsertOperations;

    public DataComposer(Context context) {
        this.context = context;
        initDatabase();
    }

    private void initDatabase() {
        appDatabase = AppDatabase.getInstance(context);
        databaseInsertOperations = new DatabaseInsertOperations(appDatabase);
    }

    public void loadDataIntoDatabase() {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        List<String> names = Arrays.asList(resources.getStringArray(R.array.names));
        List<String> descriptions = Arrays.asList(resources.getStringArray(R.array.descriptions));
        List<String> readMoreLinks = Arrays.asList(resources.getStringArray(R.array.read_more));
        List<String> latitudes = Arrays.asList(resources.getStringArray(R.array.latitude));
        List<String> longitudes = Arrays.asList(resources.getStringArray(R.array.longitude));
        List<String> zoomLevels = Arrays.asList(resources.getStringArray(R.array.zoom));
        List<String> images = Arrays.asList(resources.getStringArray(R.array.images));

        //We assume that the newly create lists are equal in size
        int size = names.size();
        for (int i = 0; i < size; i++) {
            handleCategoryChange(names.get(i));
            PlaceEntry placeEntry = new PlaceEntry();
            placeEntry.setName(names.get(i));
            placeEntry.setDescription(descriptions.get(i));
            placeEntry.setReadMoreLink(readMoreLinks.get(i));
            placeEntry.setLatitude(Double.parseDouble(latitudes.get(i)));
            placeEntry.setLongitude(Double.parseDouble(longitudes.get(i)));
            placeEntry.setZoom(Float.parseFloat(zoomLevels.get(i)));
            placeEntry.setFavorite(false);
            placeEntry.setImageResources(resources.getIdentifier(images.get(i), "drawable", packageName));
            placeEntry.setCity(isCity);
            placeEntry.setVillage(isVillage);
            placeEntry.setCastle(isCastle);
            insertPlaceEntry(placeEntry);
        }
    }

    private void handleCategoryChange(String name) {
        if ("Biertan".equals(name)) {
            isCity = false;
            isVillage = true;
            isCastle = false;
        }
        if ("Alba Iulia Citadel".equals(name)) {
            isCity = false;
            isVillage = false;
            isCastle = true;
        }
    }

    private void insertPlaceEntry(final PlaceEntry placeEntry) {
        databaseInsertOperations.execute(placeEntry);
    }
}
