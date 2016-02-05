package com.example.muditi.deligoo;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by muditi on 04-02-2016.
 */
public class Google_analytics_class extends Application{

    public static final String TAG = Google_analytics_class.class
            .getSimpleName();

    private static Google_analytics_class mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Analytics_tracker.initialize(this);
        Analytics_tracker.getInstance().get(Analytics_tracker.Target.APP);
    }

    public static synchronized Google_analytics_class getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        Analytics_tracker analyticsTrackers = Analytics_tracker.getInstance();
        return analyticsTrackers.get(Analytics_tracker.Target.APP);
    }

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();


        t.setScreenName(screenName);


        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }


    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

}
