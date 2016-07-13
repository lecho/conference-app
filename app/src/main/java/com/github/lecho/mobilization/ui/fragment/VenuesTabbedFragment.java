package com.github.lecho.mobilization.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.github.lecho.mobilization.ui.loader.NavigationViewDataLoader;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.AgendaViewModel;
import com.github.lecho.mobilization.viewmodel.NavigationViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuesTabbedFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<NavigationViewModel> {

    public static final String TAG = VenuesTabbedFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private VenuePagerAdapter pagerAdapter;

    public static VenuesTabbedFragment newInstance() {
        VenuesTabbedFragment fragment = new VenuesTabbedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venues_tabbed, container, false);
        ButterKnife.bind(this, rootView);
        tabLayout.setupWithViewPager(viewPager);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public Loader<NavigationViewModel> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return NavigationViewDataLoader.getLoader(getActivity().getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<NavigationViewModel> loader, NavigationViewModel navigationViewModel) {
        if (loader.getId() == LOADER_ID) {
            pagerAdapter = new VenuePagerAdapter(navigationViewModel);
            viewPager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<NavigationViewModel> loader) {
    }

    private class VenuePagerAdapter extends PagerAdapter {

        private NavigationViewModel navigationViewModel;

        public VenuePagerAdapter(NavigationViewModel navigationViewModel) {
            this.navigationViewModel = navigationViewModel;
        }

        @Override
        public int getCount() {
            return navigationViewModel.venueViewModels.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            VenueViewModel venueViewModel = navigationViewModel.venueViewModels.get(position);
            return venueViewModel.title;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            VenueViewModel venueViewModel = navigationViewModel.venueViewModels.get(position);
            String venueKey = venueViewModel.key;
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_agenda, collection, false);
            VenueViewController controller = new VenueViewController(getActivity(), new LinearLayoutManager(getContext()
            ), view, venueKey, position);
            controller.bindView(getLoaderManager());
            collection.addView(view);
            return view;
        }
    }

    class VenueViewController {

        private static final int BASE_LOADER_ID = 10;
        private final Context context;
        private final int venuePosition;
        private final String venueKey;
        private AgendaAdapter adapter;
        @BindView(R.id.recycler_view)
        RecyclerView recyclerView;

        public VenueViewController(Context context, RecyclerView.LayoutManager layoutManager, View view, String venueKey,
                               int venuePosition){
            ButterKnife.bind(this, view);
            this.context = context;
            this.venuePosition = venuePosition;
            this.venueKey = venueKey;
            adapter = new AgendaAdapter((AppCompatActivity)context, new StarTalkClickListener(), null);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
        }

        public void bindView(LoaderManager loaderManager){
            final int loaderId = BASE_LOADER_ID + venuePosition;
            VenueLoaderCallbacks venueLoaderCallbacks = new VenueLoaderCallbacks(context, loaderId, venueKey);
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

}
