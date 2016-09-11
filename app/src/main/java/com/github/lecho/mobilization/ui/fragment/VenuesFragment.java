package com.github.lecho.mobilization.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.controller.VenueViewController;
import com.github.lecho.mobilization.ui.loader.VenuesViewDataLoader;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VenuesFragment extends Fragment implements Scrollable, LoaderManager
        .LoaderCallbacks<List<VenueViewModel>> {

    public static final String TAG = VenuesFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private VenuePagerAdapter pagerAdapter;
    private Unbinder unbinder;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venues, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<VenueViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return VenuesViewDataLoader.getLoader(getActivity().getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<VenueViewModel>> loader, List<VenueViewModel> venuesViewModel) {
        if (loader.getId() == LOADER_ID) {
            pagerAdapter = new VenuePagerAdapter(venuesViewModel);
            viewPager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<VenueViewModel>> loader) {
    }

    @Override
    public void scrollToTop() {
        if (viewPager == null || pagerAdapter == null) {
            Log.w(TAG, "scrollToTop: viewPager or adapter is not yet initialized");
            return;
        }
        final int currentPosition = viewPager.getCurrentItem();
        final Optional<VenueViewController> venueViewController = pagerAdapter.getVenueViewController(currentPosition);
        if (venueViewController.isPresent()) {
            venueViewController.get().scrollToTop();
        }
    }

    private class VenuePagerAdapter extends PagerAdapter {

        private List<VenueViewModel> venues;
        private VenueViewController[] controllers;

        public VenuePagerAdapter(List<VenueViewModel> venueViewModels) {
            this.venues = venueViewModels;
            this.controllers = new VenueViewController[venues.size()];
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
            controllers[position] = null;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.recycler_view, collection, false);
            VenueViewController controller = new VenueViewController(getActivity(), getLoaderManager(), new
                    LinearLayoutManager(getContext()), venues.get(position), view, position);
            controller.bindView();
            collection.addView(view);
            controllers[position] = controller;
            return view;
        }

        public Optional<VenueViewController> getVenueViewController(int position) {
            return Optional.of(controllers[position]);
        }
    }
}
