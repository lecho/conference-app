package com.github.lecho.conference.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;

/**
 * Created by Leszek on 2015-09-26.
 */
public class TimeZoneChangeObserver extends BroadcastReceiver {

    private static final String TAG = TimeZoneChangeObserver.class.getSimpleName();
    private BaseRealmLoader loader;

    public TimeZoneChangeObserver(BaseRealmLoader loader) {
        this.loader = loader;
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
        LocalBroadcastManager.getInstance(loader.getContext()).registerReceiver(this, intentFilter);
    }

    public void unregister() {
        LocalBroadcastManager.getInstance(loader.getContext()).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Received time zone change broadcast");
        }
        loader.onContentChanged();
    }
}
