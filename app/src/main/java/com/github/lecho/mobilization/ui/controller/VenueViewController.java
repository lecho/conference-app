package com.github.lecho.mobilization.ui.controller;

import android.app.Activity;
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
import com.github.lecho.mobilization.ui.fragment.VenuesFragment;
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
public class VenueViewController {

    private static final int BASE_LOADER_ID = 10;
    private final Activity activity;
    private final VenueViewModel venueViewModel;
    private final LoaderManager loaderManager;
    private final AgendaAdapter adapter;
    private final int position;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public VenueViewController(Activity activity, LoaderManager loaderManager, RecyclerView.LayoutManager
            layoutManager, VenueViewModel venueViewModel, View view, int position) {
        ButterKnife.bind(this, view);
        this.activity = activity;
        this.loaderManager = loaderManager;
        this.venueViewModel = venueViewModel;
        this.position = position;
        adapter = new AgendaAdapter(activity, new StarTalkClickListener(), null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void bindView() {
        final int loaderId = BASE_LOADER_ID + position;
        VenueLoaderCallbacks venueLoaderCallbacks = new VenueLoaderCallbacks(activity, loaderId, venueViewModel.key);
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
                TalkAsyncHelper.removeTalk(talkViewModel.key);
            } else {
                if (Utils.checkSlotConflict((AppCompatActivity) activity, talkViewModel.key)) {
                    Log.d(VenuesFragment.TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(talkViewModel.key);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
