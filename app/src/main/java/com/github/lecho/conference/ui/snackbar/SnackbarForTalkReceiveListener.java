package com.github.lecho.conference.ui.snackbar;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.lecho.conference.R;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SnackbarForTalkReceiveListener implements SnackbarForTalkObserver.OnReceiveListener {

    private Context context;
    private View parentView;

    public SnackbarForTalkReceiveListener(Context context, View snackbarView) {
        this.context = context;
        this.parentView = snackbarView;
    }

    @Override
    public void onReceive(String talkKey, SnackbarForTalkObserver.SnackbarActionType snackbarActionType) {
        Snackbar snackbar;
        switch (snackbarActionType) {
            case TALK_ADDED:
                snackbar = Snackbar.make(parentView, R.string.talk_added_to_myagenda, Snackbar.LENGTH_SHORT);
                break;
            case TALK_REMOVED:
                snackbar = Snackbar.make(parentView, R.string.talk_removed_to_myagenda, Snackbar.LENGTH_SHORT);
                break;
            default:
                throw new IllegalArgumentException("Invalid snackbar type: " + snackbarActionType);
        }
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.primary_dark);
        snackbar.show();
    }

    private static class UndoAddTalkListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private static class UndoRemoveTalkListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
