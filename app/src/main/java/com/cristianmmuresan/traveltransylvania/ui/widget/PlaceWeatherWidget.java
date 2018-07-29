package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.widget.RemoteViews;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.ui.place.PlaceActivity;
import com.example.android.networkmodule.WeatherConstants;
import com.example.android.networkmodule.model.Weather;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.ApiInterface;
import com.example.android.networkmodule.network.CallbackInterface;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link PlacesWidgetConfigureActivity PlacesWidgetConfigureActivity}
 */
public class PlaceWeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.place_weather_widget);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);


        float latitude = PlacesWidgetConfigureActivity.loadLatitude(context, appWidgetId);
        float longitude = PlacesWidgetConfigureActivity.loadLongitude(context, appWidgetId);
        int placeId = PlacesWidgetConfigureActivity.loadPlaceId(context, appWidgetId);
        final String name = PlacesWidgetConfigureActivity.loadNamePref(context, appWidgetId);
        Log.d(TAG, "updateAppWidget: " + placeId);
        setupPendingIntent(context, views, placeId);

        ApiInterface apiInterface = new ApiImpl();
        apiInterface.getWeather(latitude, longitude, new CallbackInterface<Weather>() {
            @Override
            public void success(Weather response) {
                Log.d(TAG, "success: ");
                if (response == null) return;
                if (response.getMain() == null) return;
                if (response.getWeather() == null) return;
                if (response.getWeather().size() == 0) return;


                views.setTextViewText(R.id.place_name, name);
                views.setTextViewText(R.id.tempMax, String.valueOf(response.getMain().getTempMax()));
                views.setTextViewText(R.id.tempMin, String.valueOf(response.getMain().getTempMin()));
                String iconUrl = String.format(Locale.US, WeatherConstants.WEATHER_ICON_URL, response.getWeather().get(0).getIcon());
                Picasso.get().load(Uri.parse(iconUrl)).resize(100, 100).into(views, R.id.weather_icon, new int[]{appWidgetId});

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void failure(String errorMessage, String errorCode) {
                Log.d(TAG, "failure: ");
                String message = context.getString(R.string.failed_fetchin_weather_data);
                views.setTextViewText(R.id.place_name, message);
                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });
    }

    private static void setupPendingIntent(Context context, RemoteViews views, int placeId) {
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(PlaceActivity.PLACE_ID_KEY, placeId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, placeId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Intent intent = new Intent(context, PlaceWeatherService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(intent);
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

