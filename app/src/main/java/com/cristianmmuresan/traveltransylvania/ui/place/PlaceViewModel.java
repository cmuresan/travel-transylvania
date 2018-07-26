package com.cristianmmuresan.traveltransylvania.ui.place;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.database.AppDatabase;
import com.cristianmmuresan.traveltransylvania.database.AppExecutors;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.ui.map.MapsActivity;

import java.util.Locale;

public class PlaceViewModel extends AndroidViewModel {
    private static final String TAG = PlaceViewModel.class.getSimpleName();
    private LiveData<PlaceEntry> place;
    private final AppDatabase database;

    PlaceViewModel(@NonNull Application application, int placeId) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        place = database.placeDao().loadPlaceById(placeId);
    }

    public LiveData<PlaceEntry> getPlace() {
        return place;
    }

    public void readMore(String readMoreLink) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(readMoreLink));
        this.getApplication().startActivity(browserIntent);
    }

    public void checkMap(double placeId) {
        Intent mapIntent = new Intent(this.getApplication(), MapsActivity.class);
        mapIntent.putExtra(PlaceActivity.PLACE_ID_KEY, placeId);
        this.getApplication().startActivity(mapIntent);
    }

    public void favorite(PlaceEntry placeEntry) {
        placeEntry.setFavorite(!placeEntry.isFavorite());

        final PlaceEntry place = placeEntry;
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.placeDao().updatePlace(place);
            }
        });
    }

    public void share(String shareMessage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        shareIntent.setType("text/plain");
        this.getApplication().startActivity(Intent.createChooser(shareIntent,
                this.getApplication().getString(R.string.share_via)));
    }

    public void getDirections(double latitude, double longitude) {
        String directionsUriScheme = String.format(Locale.US, "http://maps.google.com/maps?daddr=%s,%s", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(directionsUriScheme));

        try {
            this.getApplication().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            try {
                intent = getDirectionsIntent(latitude, longitude);
                this.getApplication().startActivity(Intent.createChooser(intent,
                        this.getApplication().getString(R.string.get_directions)));
            } catch (ActivityNotFoundException e1) {
                Log.d(TAG, "getDirections: ");
            }
        }
    }

    private Intent getDirectionsIntent(double latitude, double longitude) {
        String uri = String.format(Locale.US, "geo:%f,%f",
                latitude, longitude);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW);
        directionsIntent.setData(Uri.parse(uri));
        return directionsIntent;
    }
}
