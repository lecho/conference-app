package com.github.lecho.mobilization.ui.fragment;

import android.content.Context;
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

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.SameSlotActivity;
import com.github.lecho.mobilization.ui.adapter.AgendaAdapter;
import com.github.lecho.mobilization.ui.adapter.MyAgendaAdapter;
import com.github.lecho.mobilization.ui.loader.AgendaLoader;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.AgendaViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Leszek on 2015-07-08.
 */
public class MyAgendaFragment extends Fragment implements Scrollable, LoaderManager.LoaderCallbacks<AgendaViewModel> {

    public static final String TAG = MyAgendaFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private AgendaAdapter adapter;
    private Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    public static MyAgendaFragment newInstance() {
        return new MyAgendaFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //TODO Grid for tablet layout, adjust margins and paddings in layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyAgendaAdapter((AppCompatActivity) getActivity(), new StarTalkClickListener(),
                new EmptySlotClickListener());
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AgendaItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void removeTalk(int position) {
        AgendaItemViewModel agendaItemViewModel = adapter.getItem(position);
        TalkAsyncHelper.removeTalkSilent(agendaItemViewModel.talk.key);
        adapter.removeTalk(position);
    }

    @Override
    public Loader<AgendaViewModel> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return AgendaLoader.getMyAgendaLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<AgendaViewModel> loader, AgendaViewModel agendaViewModel) {
        if (loader.getId() == LOADER_ID) {
            adapter.setData(agendaViewModel.agendaItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<AgendaViewModel> loader) {
        if (loader.getId() == LOADER_ID) {
            adapter.setData(Collections.<AgendaItemViewModel>emptyList());
        }
    }

    @Override
    public void scrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    private class AgendaItemTouchCallback extends ItemTouchHelper.SimpleCallback {

        public AgendaItemTouchCallback() {
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
            removeTalk(position);
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (AgendaAdapter.ITEM_TYPE_TALK == viewHolder.getItemViewType()) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
            return 0;
        }
    }

    private class EmptySlotClickListener implements AgendaAdapter.AgendaItemClickListener {

        @Override
        public void onItemClick(int position, AgendaItemViewModel agendaItem, View view) {
            SameSlotActivity.startActivity(getActivity(), agendaItem.slot.key);
        }
    }

    private class StarTalkClickListener implements AgendaAdapter.AgendaItemClickListener {

        @Override
        public void onItemClick(int position, AgendaItemViewModel agendaItem, View view) {
            TalkViewModel talkViewModel = agendaItem.talk;
            if (talkViewModel.isInMyAgenda) {
                talkViewModel.isInMyAgenda = false;
                removeTalk(position);
            }
        }
    }
}
