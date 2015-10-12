package com.github.lecho.mobilization.ui.snackbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;

/**
 * Created by Leszek on 2015-09-26.
 * TODO: try to use RxJAva:)
 */
public class SnackbarForTalkObserver extends BroadcastReceiver {

    private static final String TAG = SnackbarForTalkObserver.class.getSimpleName();
    private static final String SNACKBAR_BROADCAST_ACTION = "snackbar-broadcast-action";
    private static final String SNACKBAR_TALK_KEY = "snackbar-talk-key";
    private static final String SNACKBAR_ACTION_TYPE = "snackbar-broadcast-action-type";
    private final Context context;
    private final OnReceiveListener onReceiveListener;

    public SnackbarForTalkObserver(Context context, OnReceiveListener onReceiveListener) {
        this.context = context;
        this.onReceiveListener = onReceiveListener;
    }

    public static void emitBroadcast(Context context, String talkKey, SnackbarActionType snackbarActionType) {
        Intent broadcastIntent = new Intent(SNACKBAR_BROADCAST_ACTION);
        broadcastIntent.putExtra(SNACKBAR_TALK_KEY, talkKey);
        broadcastIntent.putExtra(SNACKBAR_ACTION_TYPE, snackbarActionType);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(SNACKBAR_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
    }

    public void unregister() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Received snackbar broadcast");
        }
        String talkKey = intent.getStringExtra(SNACKBAR_TALK_KEY);
        SnackbarActionType snackbarActionType = (SnackbarActionType) intent.getSerializableExtra(SNACKBAR_ACTION_TYPE);
        onReceiveListener.onReceive(talkKey, snackbarActionType);
    }

    public enum SnackbarActionType {
        TALK_ADDED, TALK_REMOVED
    }

    public interface OnReceiveListener {

        void onReceive(String talkKey, SnackbarActionType snackbarActionType);
    }
}
