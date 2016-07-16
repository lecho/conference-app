package com.github.lecho.mobilization.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.controller.VenueViewController;
import com.github.lecho.mobilization.ui.loader.NavigationViewDataLoader;
import com.github.lecho.mobilization.viewmodel.NavigationViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuesFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<NavigationViewModel> {

    public static final String TAG = VenuesFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private VenuePagerAdapter pagerAdapter;

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venues, container, false);
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
            pagerAdapter = new VenuePagerAdapter(navigationViewModel.venueViewModels);
            viewPager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<NavigationViewModel> loader) {
    }

    private class VenuePagerAdapter extends PagerAdapter {

        private List<VenueViewModel> venues;

        public VenuePagerAdapter(List<VenueViewModel> venueViewModels) {
            this.venues = venueViewModels;
        }

        @Override
        public int getCount() {
            return venues.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            VenueViewModel venueViewModel = venues.get(position);
            return venueViewModel.title;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_agenda, collection, false);
            VenueViewController controller = new VenueViewController(getActivity(), getLoaderManager(), new
                    LinearLayoutManager(getContext()), venues.get(position), view, position);
            controller.bindView();
            collection.addView(view);
            return view;
        }
    }

}
