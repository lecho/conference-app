package com.github.lecho.mobilization.util;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Leszek on 12.09.2016.
 */
public class AnalyticsReporter {

    private static final int MAX_VALUE_LENGHT = 32;
    private static final String CONTENT_TYPE_NAVIGATION = "navigation";
    private static final String CONTENT_TYPE_ADD_TALK = "add_talk_to_agenda";
    private static final String CONTENT_TYPE_REMOVE_TALK = "remove_talk_from_agenda";

    private static final String ITEM_CATEGORY_TALK = "talk";
    private static final String ITEM_CATEGORY_SLOT = "slot";
    private static final String ITEM_CATEGORY_SPEAKER = "speaker";

    /**
     * Logs event related to navigation
     *
     * @param firebaseAnalytics - instance of firebase
     * @param id                - navigation item unique id
     */
    public static void logNavigationEvent(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withCotentType(CONTENT_TYPE_NAVIGATION)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void logTalkAdded(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withCotentType(CONTENT_TYPE_ADD_TALK)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void logTalkRemoved(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withCotentType(CONTENT_TYPE_REMOVE_TALK)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void logTalkSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withItemCategory(ITEM_CATEGORY_TALK)
                .withItemName(id)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    public static void logEmptySlotSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withItemCategory(ITEM_CATEGORY_SLOT)
                .withItemName(id)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    public static void logSpeakerSelected(@NonNull FirebaseAnalytics firebaseAnalytics, @NonNull String id) {
        Bundle bundle = new EventBuilder()
                .withItemCategory(ITEM_CATEGORY_SPEAKER)
                .withItemName(id)
                .withItemId(id).build();
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private static class EventBuilder {
        private Bundle bundle;

        public EventBuilder() {
            bundle = new Bundle();
        }

        public EventBuilder withCotentType(@NonNull String contentType) {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
            return this;
        }

        public EventBuilder withItemId(@NonNull String itemId) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, substringTo24Chars(itemId));
            return this;
        }

        public EventBuilder withItemName(@NonNull String itemName) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, substringTo24Chars(itemName));
            return this;
        }

        public EventBuilder withItemCategory(@NonNull String itemCategory) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, substringTo24Chars(itemCategory));
            return this;
        }

        public Bundle build() {
            return bundle;
        }

        private static String substringTo24Chars(@NonNull String value) {
            if (value.length() > MAX_VALUE_LENGHT) {
                return value.substring(0, MAX_VALUE_LENGHT);
            }
            return value;
        }
    }
}
