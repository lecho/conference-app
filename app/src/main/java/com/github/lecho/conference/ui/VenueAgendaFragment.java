package com.github.lecho.conference.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.AgendaLoader;
import com.github.lecho.conference.viewmodel.AgendaViewDto;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VenueAgendaFragment extends Fragment implements LoaderManager.LoaderCallbacks<AgendaViewDto> {

    public static final String TAG = "VenueAgendaFragment";
    private static final int LOADER_ID = 0;
    private static final String ARG_VENUE_KEY = "venue-key";
    private VenueAgendaAdapter adapter;
    private String venueKey;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static VenueAgendaFragment newInstance(@NonNull String venueKey) {
        VenueAgendaFragment fragment = new VenueAgendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VENUE_KEY, venueKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        venueKey = getArguments().getString(ARG_VENUE_KEY);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VenueAgendaAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public Loader<AgendaViewDto> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return AgendaLoader.getVenueAgendaLoader(getActivity(), venueKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<AgendaViewDto> loader, AgendaViewDto agendaViewDto) {
        if (loader.getId() == LOADER_ID) {
            adapter.setData(agendaViewDto.agendaItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<AgendaViewDto> loader) {
    }
}
