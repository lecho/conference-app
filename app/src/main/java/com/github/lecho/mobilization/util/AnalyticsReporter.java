package com.github.lecho.mobilization.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Leszek on 12.09.2016.
 */
public class AnalyticsReporter {

    private static final int MAX_VALUE_LENGTH = 32;
    private static final String TYPE_NAVIGATION = "navigation";
    private static final String TYPE_ADD_TALK = "add_talk";
    private static final String TYPE_REMOVE_TALK = "remove_talk";
    private static final String TYPE_SELECT_TALK = "select_talk";
    private static final String TYPE_SELECT_SLOT = "select_slot";
    private static final String TYPE_SELECT_SPEAKER = "select_speaker";
    private static final String TYPE_JSON_DATA_DOWNLOADED = "json_downloaded";
    private static final String TYPE_JSON_DATA_FAILED = "json_data_failed";

    public static void logNavigationEvent(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_NAVIGATION);
    }

    public static void logTalkAdded(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_ADD_TALK);
        logCustomEvent(firebaseAnalytics, TYPE_ADD_TALK, itemId);
    }

    public static void logTalkRemoved(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_REMOVE_TALK);
        logCustomEvent(firebaseAnalytics, TYPE_REMOVE_TALK, itemId);
    }

    public static void logTalkSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_SELECT_TALK);
        logCustomEvent(firebaseAnalytics, TYPE_SELECT_TALK, itemId);
    }

    public static void logEmptySlotSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_SELECT_SLOT);
        logCustomEvent(firebaseAnalytics, TYPE_SELECT_SLOT, itemId);
    }

    public static void logSpeakerSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logSelectContent(firebaseAnalytics, itemId, TYPE_SELECT_SPEAKER);
        logCustomEvent(firebaseAnalytics, TYPE_SELECT_SPEAKER, itemId);
    }

    private static void logSelectContent(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId,
                                         @NonNull String contentType) {
        Bundle bundle = new EventBuilder()
                .withContentType(contentType)
                .withItemId(itemId).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void logJsonDownloaded(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logCustomEvent(firebaseAnalytics, TYPE_JSON_DATA_DOWNLOADED, itemId);
    }

    public static void logJsonDownloadFailed(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String itemId) {
        logCustomEvent(firebaseAnalytics, TYPE_JSON_DATA_FAILED, itemId);
    }

    private static void logCustomEvent(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String eventType,
                                       @NonNull String itemId) {
        Bundle bundle = new EventBuilder()
                .withItemId(itemId).build();
        firebaseAnalytics.logEvent(eventType, bundle);
    }

    private static class EventBuilder {
        private Bundle bundle;

        public EventBuilder() {
            bundle = new Bundle();
        }

        public EventBuilder withContentType(@NonNull String contentType) {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
            return this;
        }

        public EventBuilder withItemId(@NonNull String itemId) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, substringTo32Chars(itemId));
            return this;
        }

        public EventBuilder withValue(@NonNull double value) {
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, value);
            return this;
        }

        public Bundle build() {
            return bundle;
        }

        private static String substringTo32Chars(@NonNull String value) {
            if (value.length() > MAX_VALUE_LENGTH) {
                return value.substring(0, MAX_VALUE_LENGTH);
            }
            return value;
        }
    }
}
