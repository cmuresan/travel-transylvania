package com.cristianmmuresan.traveltransylvania.database;

import android.os.AsyncTask;

public class DatabaseUpdateOperations extends AsyncTask<PlaceEntry, Void, Void> {
    private final AppDatabase appDatabase;

    public DatabaseUpdateOperations(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    protected Void doInBackground(PlaceEntry... placeEntries) {
        appDatabase.placeDao().updatePlace(placeEntries[0]);
        return null;
    }
}
