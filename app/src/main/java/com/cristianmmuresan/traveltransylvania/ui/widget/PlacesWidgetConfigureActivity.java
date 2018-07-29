package com.cristianmmuresan.traveltransylvania.ui.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cristianmmuresan.traveltransylvania.R;
import com.cristianmmuresan.traveltransylvania.analytics.AnalyticsConstants;
import com.cristianmmuresan.traveltransylvania.analytics.AnalyticsEventsFactory;
import com.cristianmmuresan.traveltransylvania.database.PlaceEntry;
import com.cristianmmuresan.traveltransylvania.databinding.PlacesWidgetConfigureBinding;
import com.cristianmmuresan.traveltransylvania.ui.main.MainViewModel;

import java.util.List;

/**
 * The configuration screen for the {@link PlaceWeatherWidget PlaceWeatherWidget} AppWidget.
 */
public class PlacesWidgetConfigureActivity extends AppCompatActivity implements OnPlaceItemClickListener {

    private static final String PREFS_NAME = "com.cristianmmuresan.traveltransylvania.ui.widget.PlaceWeatherWidget";
    private static final String LATITUDE_PREFIX_KEY = "max_temp_appwidget_";
    private static final String LONGITUDE_PREFIX_KEY = "min_temp_appwidget_";
    private static final String PLACE_ID_PREFIX_KEY = "place_id_appwidget_";
    private static final String NAME_PREFIX_KEY = "name_appwidget_";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private WidgetPlacesAdapter widgetPlacesAdapter;
    private List<PlaceEntry> places;
    private MainViewModel viewModel;

    public PlacesWidgetConfigureActivity() {
        super();
    }

    private static void saveFloatPref(Context context, String prefixKey, int appWidgetId, float value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putFloat(prefixKey + appWidgetId, value);
        prefs.apply();
    }

    private static void saveIntPref(Context context, String prefixKey, int appWidgetId, int value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(prefixKey + appWidgetId, value);
        prefs.apply();
    }

    private static void saveStringPref(Context context, String prefixKey, int appWidgetId, String value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(prefixKey + appWidgetId, value);
        prefs.apply();
    }

    static float loadLatitude(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getFloat(LATITUDE_PREFIX_KEY + appWidgetId, 0f);
    }

    static float loadLongitude(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getFloat(LONGITUDE_PREFIX_KEY + appWidgetId, 0f);
    }

    static int loadPlaceId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PLACE_ID_PREFIX_KEY + appWidgetId, 0);
    }

    static String loadNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String contentValue = prefs.getString(NAME_PREFIX_KEY + appWidgetId, null);
        if (contentValue != null) {
            return contentValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deletePrefs(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(LATITUDE_PREFIX_KEY + appWidgetId);
        prefs.remove(LONGITUDE_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    private PlacesWidgetConfigureBinding binding;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.places_widget_configure);

        binding = DataBindingUtil.setContentView(this, R.layout.places_widget_configure);

        setupViewModel();
        initRecyclerView();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        widgetPlacesAdapter.setClickListener(this);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getPlaces().observe(this, new Observer<List<PlaceEntry>>() {
            @Override
            public void onChanged(@Nullable List<PlaceEntry> placeEntries) {
                places = placeEntries;
                widgetPlacesAdapter.setPlaces(placeEntries);
            }
        });
    }

    private void initRecyclerView() {
        widgetPlacesAdapter = new WidgetPlacesAdapter(this);

        binding.widgetPlacesRecyclerView.setHasFixedSize(true);
        binding.widgetPlacesRecyclerView.setAdapter(widgetPlacesAdapter);
    }

    @Override
    public void onItemClick(int position) {
        final Context context = PlacesWidgetConfigureActivity.this;

        if (places == null || places.size() == 0) return;

        PlaceEntry placeEntry = places.get(position);
        double maxTemp = placeEntry.getLatitude();
        double minTemp = placeEntry.getLongitude();
        String name = placeEntry.getName();
        int placeId = placeEntry.getId();

        // When the button is clicked, store the string locally
        saveFloatPref(context, LATITUDE_PREFIX_KEY, mAppWidgetId, (float) maxTemp);
        saveFloatPref(context, LONGITUDE_PREFIX_KEY, mAppWidgetId, (float) minTemp);
        saveIntPref(context, PLACE_ID_PREFIX_KEY, mAppWidgetId, placeId);
        saveStringPref(context, NAME_PREFIX_KEY, mAppWidgetId, name);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        PlaceWeatherWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AnalyticsEventsFactory.getInstance().setScreenName(this, AnalyticsConstants.SCREEN_WIDGET_CONFIGURATION);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

