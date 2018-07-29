package com.cristianmmuresan.traveltransylvania;

import android.app.Application;

import com.cristianmmuresan.traveltransylvania.analytics.AnalyticsEventsFactory;
import com.cristianmmuresan.traveltransylvania.datacomposer.DataComposer;
import com.cristianmmuresan.traveltransylvania.datacomposer.DataComposerPersister;

public class TravelTransylvaniaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Create database on first run
        DataComposerPersister dataComposerPersister = new DataComposerPersister(getApplicationContext());
        if (dataComposerPersister.getIsFirstRun()) {
            DataComposer dataComposer = new DataComposer(getApplicationContext());
            dataComposer.loadDataIntoDatabase();
        }

        AnalyticsEventsFactory.init(this);
    }
}
