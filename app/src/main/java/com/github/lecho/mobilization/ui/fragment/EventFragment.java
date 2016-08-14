package com.github.lecho.mobilization.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.SpeakersActivity;
import com.github.lecho.mobilization.ui.SponsorsActivity;
import com.github.lecho.mobilization.ui.loader.EventViewDataLoader;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Optional<EventViewModel>> {

    public static final String TAG = SpeakersFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private Unbinder unbinder;

    @BindView(R.id.button_map)
    Button mapButton;

    @BindView(R.id.button_sponsors)
    Button sponsorsButton;

    @BindView(R.id.button_speakers)
    Button speakersButton;

    @BindView(R.id.button_mobile_app)
    Button aboutAppButton;

    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        sponsorsButton.setOnClickListener(view -> startActivity(SponsorsActivity.class));
        speakersButton.setOnClickListener(view -> startActivity(SpeakersActivity.class));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void startActivity(Class activityClass){
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
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
