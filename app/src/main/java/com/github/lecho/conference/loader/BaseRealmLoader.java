package com.github.lecho.conference.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-09-03.
 */
public abstract class BaseRealmLoader<T> extends AsyncTaskLoader<T> {

    private static final String TAG = BaseRealmLoader.class.getSimpleName();
    protected T data;
    protected final RealmFacade realmFacade;
    protected final boolean shouldHaveObserver;
    protected ContentChangeObserver contentChangeObserver;

    protected BaseRealmLoader(Context context, boolean shouldObserveContent) {
        super(context);
        this.realmFacade = new RealmFacade(context);
        this.shouldHaveObserver = shouldObserveContent;
    }

    @Override
    public abstract T loadInBackground();

    @Override
    public void deliverResult(T newData) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Delivering data");
        }

        if (isReset()) {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (newData != null) {
                onReleaseResources(newData);
            }
        }

        T oldData = data;
        data = newData;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(newData);
        }

        // At this point we can release the resources associated with
        // 'oldData' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldData != null && oldData != newData) {
            onReleaseResources(oldData);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        // Start watching for changes in the app data.
        registerObserver();

        if (null == data || takeContentChanged()) {
            forceLoad();
        } else if (data != null) {
            // If we currently have a result available, deliver it immediately.
            deliverResult(data);
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
    public void onCanceled(T newData) {
        super.onCanceled(newData);
        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(newData);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources
        // if needed.
        if (data != null) {
            onReleaseResources(data);
            data = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        unregisterObserver();
    }

    /**
     * Helper function to take care of releasing resources associated with an actively loaded data set.
     */
    protected void onReleaseResources(T oldData) {
        oldData = null;
    }

    private void registerObserver() {
        if (shouldHaveObserver && contentChangeObserver == null) {
            contentChangeObserver = new ContentChangeObserver(this);
            contentChangeObserver.register();
        }
    }

    private void unregisterObserver() {
        if (contentChangeObserver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(contentChangeObserver);
            contentChangeObserver = null;
        }
    }

}