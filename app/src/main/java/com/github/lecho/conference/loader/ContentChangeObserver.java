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
public class ContentChangeObserver extends BroadcastReceiver {

    private static final String TAG = ContentChangeObserver.class.getSimpleName();
    private static final String CONTENT_CHANGE_BROADCAST_ACTION = "content-change-broadcast-action";
    private BaseRealmLoader loader;

    public ContentChangeObserver(BaseRealmLoader loader) {
        this.loader = loader;
    }

    public static void emitBroadcast(Context context) {
        Intent broadcastIntent = new Intent(CONTENT_CHANGE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(CONTENT_CHANGE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(loader.getContext()).registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Received content change broadcast");
        }
        loader.onContentChanged();
    }
}
