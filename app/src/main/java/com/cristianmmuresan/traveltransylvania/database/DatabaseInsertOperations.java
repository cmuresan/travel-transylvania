package com.cristianmmuresan.traveltransylvania.database;

import android.os.AsyncTask;

public class DatabaseInsertOperations extends AsyncTask<PlaceEntry, Void, Void> {
    private final AppDatabase appDatabase;

    public DatabaseInsertOperations(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    protected Void doInBackground(PlaceEntry... placeEntries) {
        appDatabase.placeDao().insertPlace(placeEntries[0]);
        return null;
    }
}
