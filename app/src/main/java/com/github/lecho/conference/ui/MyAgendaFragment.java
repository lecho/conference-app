package com.github.lecho.conference.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.AgendaLoader;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.AgendaViewDto;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 2015-07-08.
 */
public class MyAgendaFragment extends Fragment implements LoaderManager.LoaderCallbacks<AgendaViewDto> {

    public static final String TAG = "MyAgendaFragment";
    private static final int LOADER_ID = 0;
    private AgendaAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public static MyAgendaFragment newInstance() {
        MyAgendaFragment fragment = new MyAgendaFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_activity_my_agenda);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, rootView);

        //TODO Grid for tablet layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AgendaAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(new MyAgendaItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Only to refresh current item indicator when time have changed, good enough without timer
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader<AgendaViewDto> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return AgendaLoader.getMyAgendaLoader(getActivity());
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

    private class MyAgendaItemTouchCallback extends ItemTouchHelper.SimpleCallback {

        public MyAgendaItemTouchCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            final int position = viewHolder.getAdapterPosition();
            AgendaItemViewDto agendaItemViewDto = adapter.getItem(position);
            TalkFavoriteTask.removeFromMyAgenda(getActivity().getApplicationContext(), agendaItemViewDto.talk.key,
                    false);
            adapter.removeItemFromAdapter(position);
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (AgendaAdapter.ITEM_TYPE_BREAK == viewHolder.getItemViewType()) {
                return 0;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }
    }
}
