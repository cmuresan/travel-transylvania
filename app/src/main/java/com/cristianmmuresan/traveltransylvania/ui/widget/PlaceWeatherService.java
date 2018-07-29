package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class PlaceWeatherService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        if (allWidgetIds == null) return super.onStartCommand(intent, flags, startId);
        for (int appWidgetId : allWidgetIds) {
            PlaceWeatherWidget.updateAppWidget(this, appWidgetManager, appWidgetId);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
