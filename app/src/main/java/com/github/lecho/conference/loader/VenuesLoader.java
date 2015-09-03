package com.github.lecho.conference.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.realmmodel.RealmFacade;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class VenuesLoader extends AsyncTaskLoader<List<VenueViewDto>> {

    private static final String TAG = VenuesLoader.class.getSimpleName();
    private List<VenueViewDto> venuesData;
    private final RealmFacade realmFacade;

    public static VenuesLoader getVenuesLoader(Context context) {
        return new VenuesLoader(context);
    }

    private VenuesLoader(Context context) {
        super(context);
        this.realmFacade = new RealmFacade(context);
    }

    @Override
    public List<VenueViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading venues data");
        }
        List<VenueViewDto> venuesData = realmFacade.loadAllVenues();
        return venuesData;
    }

    @Override
    public void deliverResult(List<VenueViewDto> newVenuesData) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Delivering agenda data");
        }
        if (isReset()) {
            if (newVenuesData != null) {
                onReleaseResources(newVenuesData);
            }
        }
        List<VenueViewDto> oldVenuesData = venuesData;
        venuesData = newVenuesData;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(newVenuesData);
        }

        if (oldVenuesData != null && oldVenuesData != newVenuesData) {
            onReleaseResources(oldVenuesData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (venuesData != null) {
            deliverResult(venuesData);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(List<VenueViewDto> newVenuesData) {
        super.onCanceled(newVenuesData);
        onReleaseResources(newVenuesData);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (venuesData != null) {
            onReleaseResources(venuesData);
            venuesData = null;
        }
    }

    protected void onReleaseResources(List<VenueViewDto> oldVenuesData) {
        //do nothing
    }
}