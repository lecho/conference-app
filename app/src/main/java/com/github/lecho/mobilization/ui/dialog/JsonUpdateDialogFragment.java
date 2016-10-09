package com.github.lecho.mobilization.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.JsonDownloadService;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Leszek on 2015-10-04.
 */
public class JsonUpdateDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = JsonUpdateDialogFragment.class.getSimpleName();
    private FirebaseAnalytics firebaseAnalytics;

    public static void show(AppCompatActivity activity) {
        newInstance().show(activity.getSupportFragmentManager(), TAG);
    }

    private static JsonUpdateDialogFragment newInstance() {
        JsonUpdateDialogFragment fragment = new JsonUpdateDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_fragment_json_update, null, false);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(R.string.dialog_json_data_update)
                .setPositiveButton(R.string.dialog_button_positive, new PositiveClickListener())
                .setNegativeButton(R.string.dialog_button_negative, new NegativeClickListener())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            JsonDownloadService.startDownload(getContext().getApplicationContext());
        }
    }

    private class NegativeClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
        }
    }
}
