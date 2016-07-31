package com.github.lecho.mobilization.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.loader.EventViewDataLoader;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Optional<EventViewModel>> {

    public static final String TAG = SpeakersFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;

    public static SpeakersFragment newInstance() {
        SpeakersFragment fragment = new SpeakersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_speakers);
    }

    @Override
    public Loader<Optional<EventViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return EventViewDataLoader.getLoader(getActivity().getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<EventViewModel>> loader, Optional<EventViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            //TODO
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<EventViewModel>> loader) {
    }
}
