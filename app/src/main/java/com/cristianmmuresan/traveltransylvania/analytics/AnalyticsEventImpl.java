package com.cristianmmuresan.traveltransylvania.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsEventImpl implements AnalyticsEvents {

    private FirebaseAnalytics firebaseAnalytics;

    public void initAnalytics(@NonNull Context context) {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            firebaseAnalytics.setMinimumSessionDuration(0);
        }
    }

    @Override
    public void setScreenName(@NonNull Activity activity, @NonNull String screenName) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, null);
    }

    @Override
    public void setFavorite(String placeName) {
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsConstants.PARAM_PLACE_NAME, placeName);
        firebaseAnalytics.logEvent(AnalyticsConstants.FAVORITE, bundle);
    }

    @Override
    public void setGenericEvent(String event) {
        firebaseAnalytics.logEvent(event, null);
    }
}
