package com.github.lecho.mobilization.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.controller.VenueAgendaController;
import com.github.lecho.mobilization.ui.loader.NavigationViewDataLoader;
import com.github.lecho.mobilization.viewmodel.NavigationViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuesTabbedFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<NavigationViewModel> {

    private static final int LOADER_ID = 0;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private VenuePagerAdapter pagerAdapter;

    public VenuesTabbedFragment() {
        // Required empty public constructor
    }

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
            String venueTitle = venueViewModel.title;
            VenueAgendaController venueAgendaController = new VenueAgendaController((AppCompatActivity) getActivity(),
                    venueKey, venueTitle);
            View view = venueAgendaController.getView(LayoutInflater.from(getActivity()), collection, position);
            collection.addView(view);
            return view;
        }
    }
}
