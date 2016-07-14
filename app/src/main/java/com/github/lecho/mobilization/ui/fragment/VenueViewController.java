package com.github.lecho.mobilization.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.adapter.AgendaAdapter;
import com.github.lecho.mobilization.ui.loader.AgendaLoader;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.AgendaViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 14.07.2016.
 */
class VenueViewController {

    private static final int BASE_LOADER_ID = 10;
    private final Context context;
    private final int venuePosition;
    private final VenueViewModel venueViewModel;
    private final LoaderManager loaderManager;
    private AgendaAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public VenueViewController(Context context, LoaderManager loaderManager, RecyclerView.LayoutManager
            layoutManager, View view, VenueViewModel venueViewModel, int venuePosition) {
        ButterKnife.bind(this, view);
        this.context = context;
        this.loaderManager = loaderManager;
        this.venueViewModel = venueViewModel;
        this.venuePosition = venuePosition;
        adapter = new AgendaAdapter((AppCompatActivity) context, new StarTalkClickListener(), null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void bindView() {
        final int loaderId = BASE_LOADER_ID + venuePosition;
        VenueLoaderCallbacks venueLoaderCallbacks = new VenueLoaderCallbacks(context, loaderId, venueViewModel.key);
        loaderManager.initLoader(loaderId, null, venueLoaderCallbacks);
    }

    private class VenueLoaderCallbacks implements LoaderManager.LoaderCallbacks<AgendaViewModel> {

        private final Context context;
        private final int loaderId;
        private final String venueKey;

        public VenueLoaderCallbacks(Context context, int loaderId, String venueKey) {
            this.context = context;
            this.loaderId = loaderId;
            this.venueKey = venueKey;
        }

        @Override
        public Loader<AgendaViewModel> onCreateLoader(int id, Bundle args) {
            if (id == loaderId) {
                return AgendaLoader.getVenueAgendaLoader(context, venueKey);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<AgendaViewModel> loader, AgendaViewModel agendaViewModel) {
            if (loader.getId() == loaderId) {
                adapter.setData(agendaViewModel.agendaItems);
            }
        }

        @Override
        public void onLoaderReset(Loader<AgendaViewModel> loader) {
            if (loader.getId() == loaderId) {
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
                    Log.d(VenuesTabbedFragment.TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(context.getApplicationContext(), talkViewModel.key);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
