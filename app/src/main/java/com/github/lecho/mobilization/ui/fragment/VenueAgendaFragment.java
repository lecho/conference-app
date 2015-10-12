package com.github.lecho.mobilization.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.adapter.AgendaAdapter;
import com.github.lecho.mobilization.ui.loader.AgendaLoader;
import com.github.lecho.mobilization.ui.snackbar.SnackbarForTalkHelper;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewDto;
import com.github.lecho.mobilization.viewmodel.AgendaViewDto;
import com.github.lecho.mobilization.viewmodel.TalkViewDto;

import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VenueAgendaFragment extends Fragment implements LoaderManager.LoaderCallbacks<AgendaViewDto> {

    public static final String TAG = VenueAgendaFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private static final String ARG_VENUE_KEY = "venue-key";
    private static final String ARG_VENUE_NAME = "venue-name";
    private SnackbarForTalkHelper snackbarForTalkHelper;
    private AgendaAdapter adapter;
    private String venueKey;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static VenueAgendaFragment newInstance(@NonNull String venueKey, @NonNull String venueName) {
        VenueAgendaFragment fragment = new VenueAgendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_VENUE_KEY, venueKey);
        args.putString(ARG_VENUE_NAME, venueName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        venueKey = getArguments().getString(ARG_VENUE_KEY);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        String venueName = getArguments().getString(ARG_VENUE_NAME);
        StringBuilder title = new StringBuilder()
                .append(venueName)
                .append(" ")
                .append(getString(R.string.title_activity_track));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title.toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, rootView);
        snackbarForTalkHelper = new SnackbarForTalkHelper(getActivity().getApplicationContext(), container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AgendaAdapter((AppCompatActivity) getActivity(), new StarTalkClickListener(),
                new EmptySlotClickListener());
        recyclerView.setAdapter(adapter);
        return rootView;

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
        if (loader.getId() == LOADER_ID) {
            adapter.setData(Collections.<AgendaItemViewDto>emptyList());
        }
    }

    private class EmptySlotClickListener implements AgendaAdapter.AgendaItemClickListener {

        @Override
        public void onItemClick(int position, AgendaItemViewDto agendaItem, View view) {
            //Do nothing, there should be no empty slot visible for venue agenda
        }
    }

    private class StarTalkClickListener implements AgendaAdapter.AgendaItemClickListener {

        @Override
        public void onItemClick(int position, AgendaItemViewDto agendaItem, View view) {
            TalkViewDto talkViewDto = agendaItem.talk;
            if (talkViewDto.isInMyAgenda) {
                talkViewDto.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(getActivity().getApplicationContext(), talkViewDto.key);
            } else {
                if (Utils.checkSlotConflict((AppCompatActivity) getActivity(), talkViewDto.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewDto.key);
                    return;
                }
                talkViewDto.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(getActivity().getApplicationContext(), talkViewDto.key);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
