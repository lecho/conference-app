package com.github.lecho.mobilization.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Leszek on 12.09.2016.
 */
public class AnalyticsReporter {

    private static final String CONTENT_TYPE_NAVIGATION = "navigation";

    /**
     * Logs event related to navigation
     * @param firebaseAnalytics - instance of firebase
     * @param itemId - navigation item unique id
     * @param itemName - item name
     */
    public static void logNavigationEvent(@NonNull FirebaseAnalytics firebaseAnalytics, String itemId, String
            itemName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, CONTENT_TYPE_NAVIGATION);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
