package com.github.lecho.conference.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;

import com.github.lecho.conference.R;
import com.github.lecho.conference.async.TalkFavoriteTask;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SlotConflictDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = SlotConflictDialogFragment.class.getSimpleName();
    private static final String ARG_TALK_KEY = "arg-talk-key";
    private String talkKey;

    public static void show(AppCompatActivity activity, String talkKey) {
        newInstance(talkKey).show(activity.getSupportFragmentManager(), TAG);
    }

    private static SlotConflictDialogFragment newInstance(String talkKey) {
        SlotConflictDialogFragment fragment = new SlotConflictDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TALK_KEY, talkKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        talkKey = getArguments().getString(ARG_TALK_KEY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_slot_conflict_title)
                .setMessage(R.string.dialog_slot_conflict_message)
                .setPositiveButton(R.string.dialog_slot_conflict_button_positive, new PositiveClickListener())
                .setNegativeButton(R.string.dialog_slot_conflict_button_negative, new NegativeClickListener())
                .create();
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TalkFavoriteTask.addToMyAgenda(getContext().getApplicationContext(), talkKey, true);
        }
    }

    private class NegativeClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
        }
    }
}
