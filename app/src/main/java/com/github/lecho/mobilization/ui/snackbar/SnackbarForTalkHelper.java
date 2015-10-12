package com.github.lecho.mobilization.ui.snackbar;

import android.content.Context;
import android.view.View;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SnackbarForTalkHelper {

    private final Context context;
    private final SnackbarForTalkObserver snackbarForTalkObserver;

    public SnackbarForTalkHelper(Context context, View snackbarView) {
        this.context = context;
        SnackbarForTalkReceiveListener listener = new SnackbarForTalkReceiveListener(context, snackbarView);
        snackbarForTalkObserver = new SnackbarForTalkObserver(context, listener);
    }

    public void onResume() {
        snackbarForTalkObserver.register();
    }

    public void onPause() {
        snackbarForTalkObserver.unregister();
    }

}
