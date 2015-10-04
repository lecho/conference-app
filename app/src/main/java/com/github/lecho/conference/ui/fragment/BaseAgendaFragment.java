package com.github.lecho.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.conference.ui.snackbar.SnackbarForTalkHelper;

/**
 * Created by Leszek on 2015-10-04.
 */
public abstract class BaseAgendaFragment extends Fragment {

    private SnackbarForTalkHelper snackbarForTalkHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        snackbarForTalkHelper = new SnackbarForTalkHelper(getActivity().getApplicationContext(), container);
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        snackbarForTalkHelper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        snackbarForTalkHelper.onResume();
    }
}
