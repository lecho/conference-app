package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

/**
 * Created by Leszek on 2015-09-03.
 */
public class EventViewDataLoader extends BaseRealmLoader<Optional<EventViewModel>> {

    private static final String TAG = EventViewDataLoader.class.getSimpleName();

    public static EventViewDataLoader getLoader(Context context) {
        return new EventViewDataLoader(context);
    }

    private EventViewDataLoader(Context context) {
        super(context);
    }

    @Override
    public Optional<EventViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading event view data");
        }
        Optional<EventViewModel> newData = realmFacade.loadEvent();
        return newData;
    }
}