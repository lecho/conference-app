package com.github.lecho.mobilization.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.viewmodel.AgendaData;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaLoader extends AsyncTaskLoader<AgendaData> {

    private static final String TAG = AgendaLoader.class.getSimpleName();
    private AgendaData agendaData;
    private AgendaLoaderType type;

    public AgendaLoader(Context context, AgendaLoaderType type) {
        super(context);
    }

    @Override
    public AgendaData loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading agenda data");
        }
        //TODO: Load data
        AgendaData agendaData = new AgendaData();
        return agendaData;
    }

    @Override
    public void deliverResult(AgendaData newAgendaData) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Delivering agenda data");
        }
        if (isReset()) {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (newAgendaData != null) {
                onReleaseResources(newAgendaData);
            }
        }
        AgendaData oldAgendaData = agendaData;
        agendaData = newAgendaData;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(newAgendaData);
        }

        // At this point we can release the resources associated with
        // 'oldData' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldAgendaData != null && oldAgendaData != newAgendaData) {
            onReleaseResources(oldAgendaData);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (agendaData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(agendaData);
        } else {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(AgendaData newAgendaData) {
        super.onCanceled(newAgendaData);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(newAgendaData);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (agendaData != null) {
            onReleaseResources(agendaData);
            agendaData = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated with an actively loaded data set.
     */
    protected void onReleaseResources(AgendaData oldAgendaData) {
        //do nothing
    }

    public enum AgendaLoaderType {
        MY_AGENDA, VENUE_AGENDA, WHOLE_AGENDA
    }
}