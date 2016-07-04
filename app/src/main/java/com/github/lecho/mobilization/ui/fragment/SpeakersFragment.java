package com.github.lecho.mobilization.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.loader.SpeakersLoader;
import com.github.lecho.mobilization.ui.adapter.SpeakersAdapter;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakersFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<SpeakerViewModel>> {

    public static final String TAG = SpeakersFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private SpeakersAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static SpeakersFragment newInstance() {
        SpeakersFragment fragment = new SpeakersFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_speakers);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_speakers, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SpeakersAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public Loader<List<SpeakerViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SpeakersLoader.getLoader(getActivity().getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<SpeakerViewModel>> loader, List<SpeakerViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SpeakerViewModel>> loader) {
    }
}
