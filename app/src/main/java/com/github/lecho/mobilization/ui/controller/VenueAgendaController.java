package com.github.lecho.mobilization.ui.controller;

import android.os.Bundle;
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
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.AgendaViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 12.07.2016.
 */
public class VenueAgendaController {

    public static final String TAG = VenueAgendaController.class.getSimpleName();
    private static final int LOADER_ID = 5;
    private static final String ARG_VENUE_KEY = "venue-key";
    private static final String ARG_VENUE_NAME = "venue-name";
    private final AppCompatActivity context;
    private final String venueKey;
    private final String venueTitle;
    private int position;
    private AgendaAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public VenueAgendaController(AppCompatActivity context, String venueKey, String venueTitle) {
        this.context = context;
        this.venueKey = venueKey;
        this.venueTitle = venueTitle;
    }

    public View getView(LayoutInflater inflater, ViewGroup container, int id){
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, rootView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AgendaAdapter(context, new StarTalkClickListener(), null);
        recyclerView.setAdapter(adapter);
        position = id;
        context.getSupportLoaderManager().initLoader(LOADER_ID + position, null, new LoaderCallback());
        return rootView;
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<AgendaViewModel> {
        @Override
        public Loader<AgendaViewModel> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID + position) {
                return AgendaLoader.getVenueAgendaLoader(context, venueKey);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<AgendaViewModel> loader, AgendaViewModel agendaViewModel) {
            if (loader.getId() == LOADER_ID + position) {
                adapter.setData(agendaViewModel.agendaItems);
            }
        }

        @Override
        public void onLoaderReset(Loader<AgendaViewModel> loader) {
            if (loader.getId() == LOADER_ID + position) {
                adapter.setData(Collections.<AgendaItemViewModel>emptyList());
            }
        }
    }

    private class StarTalkClickListener implements AgendaAdapter.AgendaItemClickListener {

        @Override
        public void onItemClick(int position, AgendaItemViewModel agendaItem, View view) {
            TalkViewModel talkViewModel = agendaItem.talk;
            if (talkViewModel.isInMyAgenda) {
                talkViewModel.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(context.getApplicationContext(), talkViewModel.key);
            } else {
                if (Utils.checkSlotConflict((AppCompatActivity) context, talkViewModel.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(context.getApplicationContext(), talkViewModel.key);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
