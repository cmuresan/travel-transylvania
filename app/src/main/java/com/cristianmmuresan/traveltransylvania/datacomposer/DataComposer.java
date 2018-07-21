package com.cristianmmuresan.traveltransylvania.datacomposer;

import android.content.Context;
import android.content.res.Resources;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.AppExecutors;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;

import java.util.Arrays;
import java.util.List;

public class DataComposer {

    private final Context context;
    private AppDatabase appDatabase;

    public DataComposer(Context context) {
        this.context = context;
        initDatabase();
    }

    private void initDatabase() {
        appDatabase = AppDatabase.getInstance(context);
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
            PlaceEntry placeEntry = new PlaceEntry();
            placeEntry.setName(names.get(i));
            placeEntry.setDescription(descriptions.get(i));
            placeEntry.setReadMoreLink(readMoreLinks.get(i));
            placeEntry.setLatitude(Double.parseDouble(latitudes.get(i)));
            placeEntry.setLongitude(Double.parseDouble(longitudes.get(i)));
            placeEntry.setZoom(Float.parseFloat(zoomLevels.get(i)));
            placeEntry.setFavorite(false);
            placeEntry.setImageResources(resources.getIdentifier(images.get(i), "drawable", packageName));
            insertPlaceEntry(placeEntry);
        }
    }

    private void insertPlaceEntry(final PlaceEntry placeEntry) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.placeDao().insertPlace(placeEntry);
            }
        });
    }
}
