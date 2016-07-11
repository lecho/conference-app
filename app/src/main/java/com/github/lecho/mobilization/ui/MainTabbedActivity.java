package com.github.lecho.mobilization.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.VenueAgendaFragment;
import com.github.lecho.mobilization.ui.loader.NavigationViewDataLoader;
import com.github.lecho.mobilization.viewmodel.NavigationViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainTabbedActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<NavigationViewModel> {

    private static final String TAG = MainTabbedActivity.class.getSimpleName();
    private static final String ARG_CHECKED_TAB_INDEX = "checked-tab-index";
    private static final int LOADER_ID = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private VenuePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
//        pagerAdapter = new VenuePagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<NavigationViewModel> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return NavigationViewDataLoader.getLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<NavigationViewModel> loader, NavigationViewModel navigationViewModel) {
        if (loader.getId() == LOADER_ID) {
            pagerAdapter = new VenuePagerAdapter(getSupportFragmentManager(), navigationViewModel);
            viewPager.setAdapter(pagerAdapter);
//            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onLoaderReset(Loader<NavigationViewModel> loader) {
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class VenuePagerAdapter extends FragmentPagerAdapter {

        private NavigationViewModel navigationViewModel;

        public VenuePagerAdapter(FragmentManager fragmentManager, NavigationViewModel navigationViewModel) {
            super(fragmentManager);
            this.navigationViewModel = navigationViewModel;
        }

        @Override
        public Fragment getItem(int position) {
            VenueViewModel venueViewModel = navigationViewModel.venueViewModels.get(position);
            String venueKey = venueViewModel.key;
            String venueTitle = venueViewModel.title;
            return VenueAgendaFragment.newInstance(venueKey, venueTitle);
        }

        @Override
        public int getCount() {
            return navigationViewModel.venueViewModels.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            VenueViewModel venueViewModel = navigationViewModel.venueViewModels.get(position);
            return venueViewModel.title;
        }
    }
}
