package com.github.lecho.mobilization.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.controller.VenueAgendaController;
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

    private VenuePagerAdapter2 pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
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
            pagerAdapter = new VenuePagerAdapter2(navigationViewModel);
            viewPager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<NavigationViewModel> loader) {
    }

    private class VenuePagerAdapter2 extends PagerAdapter {

        private NavigationViewModel navigationViewModel;

        public VenuePagerAdapter2(NavigationViewModel navigationViewModel) {
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
            VenueAgendaController venueAgendaController = new VenueAgendaController(MainTabbedActivity.this,
                    venueKey, venueTitle);
            View view = venueAgendaController.getView(LayoutInflater.from(getApplicationContext()), collection, position);
            collection.addView(view);
            return view;
        }
    }

    private static class VenuePagerAdapter extends FragmentPagerAdapter {

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
