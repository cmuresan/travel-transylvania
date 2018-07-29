package com.cristianmmuresan.traveltransylvania.analytics;

import android.content.Context;

public class AnalyticsEventsFactory {
    private static AnalyticsEventImpl analyticsEvent = null;

    public static AnalyticsEvents getInstance() throws InstantiationException {
        if(analyticsEvent ==null){
            throw new InstantiationException("AnalyticsEvents is not initialized");
        }
        return analyticsEvent;
    }

    public static void init(Context context){
        analyticsEvent = new AnalyticsEventImpl();
        analyticsEvent.initAnalytics(context);
    }
}
