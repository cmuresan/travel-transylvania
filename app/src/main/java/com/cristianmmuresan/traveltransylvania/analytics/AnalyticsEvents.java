package com.cristianmmuresan.traveltransylvania.analytics;

import android.app.Activity;

public interface AnalyticsEvents {
    void setScreenName(Activity activity, String screenName);

    void setFavorite(String placeName);

    void setGenericEvent(String event);
}
