package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.widget.RemoteViews;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceActivity;
import com.example.android.networkmodule.WeatherConstants;

import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link PlacesWidgetConfigureActivity PlacesWidgetConfigureActivity}
 */
public class PlaceWeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.place_weather_widget);

        Intent intent = new Intent(context, PlaceActivity.class);
        //TODO get place id
//        intent.putExtra(PlaceActivity.PLACE_ID_KEY, placeId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);


        float maxTemp = PlacesWidgetConfigureActivity.loadMaxTemp(context, appWidgetId);
        float minTemp = PlacesWidgetConfigureActivity.loadMinTemp(context, appWidgetId);
        String iconId = PlacesWidgetConfigureActivity.loadIconId(context, appWidgetId);
        final String name = PlacesWidgetConfigureActivity.loadNamePref(context, appWidgetId);


        views.setTextViewText(R.id.place_name, name);
        views.setTextViewText(R.id.tempMax, String.valueOf(maxTemp));
        views.setTextViewText(R.id.tempMin, String.valueOf(minTemp));

        String icon = String.format(Locale.US, WeatherConstants.WEATHER_ICON_URL, iconId);
        views.setImageViewUri(R.id.weather_icon, Uri.parse(icon));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            PlacesWidgetConfigureActivity.deletePrefs(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

