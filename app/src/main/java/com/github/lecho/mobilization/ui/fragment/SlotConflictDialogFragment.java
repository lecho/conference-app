package com.github.lecho.mobilization.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Leszek on 2015-10-04.
 */
public class SlotConflictDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = SlotConflictDialogFragment.class.getSimpleName();
    private static final String ARG_OLD_TALK_KEY = "arg-old-talk-key";
    private static final String ARG_OLD_TALK_TITLE = "arg-old-talk-title";
    private static final String ARG_NEW_TALK_KEY = "arg-new-talk-key";
    private String oldTalkKey;
    private String newTalkKey;
    private String oldTalkTitle;
    private Unbinder unbinder;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.text_slot_conflict_old_talk_title)
    TextView oldTalkTitleView;

    public static void show(AppCompatActivity activity, String oldTalkKey, String oldTalkTitle, String newTalkKey) {
        newInstance(oldTalkKey, oldTalkTitle, newTalkKey).show(activity.getSupportFragmentManager(), TAG);
    }

    private static SlotConflictDialogFragment newInstance(String oldTalkKey, String oldTalkName, String newTalkKey) {
        SlotConflictDialogFragment fragment = new SlotConflictDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OLD_TALK_KEY, oldTalkKey);
        args.putString(ARG_OLD_TALK_TITLE, oldTalkName);
        args.putString(ARG_NEW_TALK_KEY, newTalkKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        oldTalkKey = getArguments().getString(ARG_OLD_TALK_KEY);
        oldTalkTitle = getArguments().getString(ARG_OLD_TALK_TITLE);
        newTalkKey = getArguments().getString(ARG_NEW_TALK_KEY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_fragment_slot_conflict, null, false);
        unbinder = ButterKnife.bind(this, view);
        oldTalkTitleView.setText(oldTalkTitle);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(R.string.dialog_slot_conflict_title)
                .setPositiveButton(R.string.dialog_slot_conflict_button_positive, new PositiveClickListener())
                .setNegativeButton(R.string.dialog_slot_conflict_button_negative, new NegativeClickListener())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TalkAsyncHelper.replaceTalk(oldTalkKey, newTalkKey);
            AnalyticsReporter.logTalkAdded(firebaseAnalytics, newTalkKey, newTalkKey);
            AnalyticsReporter.logTalkRemoved(firebaseAnalytics, oldTalkKey, oldTalkKey);
        }
    }

    private class NegativeClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
        }
    }
}
