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
import com.github.lecho.mobilization.ui.adapter.SponsorsAdapter;
import com.github.lecho.mobilization.ui.loader.SponsorsLoader;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SponsorsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<SponsorViewModel>> {

    public static final String TAG = SponsorsFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private SponsorsAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static SponsorsFragment newInstance() {
        SponsorsFragment fragment = new SponsorsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_sponsors);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_speakers, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SponsorsAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public Loader<List<SponsorViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SponsorsLoader.getLoader(getActivity().getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<SponsorViewModel>> loader, List<SponsorViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            adapter.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<SponsorViewModel>> loader) {
    }
}
