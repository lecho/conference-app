package com.github.lecho.conference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.VenuesLoader;
import com.github.lecho.conference.service.ContentUpdateService;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<VenueViewDto>> {

    private static final int LOADER_ID = 0;
    private NavigationViewController navigationViewController;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationViewController = new NavigationViewController(navigationView);
        navigationViewController.bindStaticItems();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (null == savedInstanceState) {
            replaceFragment(MyAgendaFragment.newInstance());
            updateContent();
        }

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void updateContent() {
        Intent serviceIntent = new Intent(getApplicationContext(), ContentUpdateService.class);
        startService(serviceIntent);
    }

    public void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<VenueViewDto>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return VenuesLoader.getVenuesLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<VenueViewDto>> loader, List<VenueViewDto> venueViewDtos) {
        if (loader.getId() == LOADER_ID) {
            navigationViewController.bindVenuesItems(venueViewDtos);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<VenueViewDto>> loader) {
    }

    private class NavigationViewController {

        private NavigationView navigationView;
        private Menu navigationMenu;
        private SubMenu venuesSubMenu;
        private final int groupId = 0;

        public NavigationViewController(NavigationView navigationView) {
            this.navigationView = navigationView;
            navigationMenu = navigationView.getMenu();
        }

        public void bindStaticItems() {
            int itemId = 0;
            int order = 0;
            navigationMenu = navigationView.getMenu();
            navigationMenu.setGroupCheckable(groupId, true, true);
            //MyAgenda
            navigationMenu.add(groupId, itemId++, order++, R.string.navigation_my_agenda).setCheckable(true)
                    .setOnMenuItemClickListener(new NavigationMenuClickListener(MyAgendaFragment.newInstance()));
            //Tracks
            venuesSubMenu = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_venues);
            //More
            SubMenu subMenuMore = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_more);
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_speakers).setCheckable(true);
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_sponsors).setCheckable(true)
                    .setOnMenuItemClickListener(new NavigationMenuClickListener(SponsorsFragment.newInstance()));
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_about).setCheckable(true);
        }

        public void bindVenuesItems(@NonNull List<VenueViewDto> venueViewDtos) {
            int itemId = 0;
            int order = 0;
            venuesSubMenu.clear();
            for (VenueViewDto venueViewDto : venueViewDtos) {
                MenuItem venueItem = venuesSubMenu.add(groupId, itemId++, order++, venueViewDto.title).
                        setCheckable(true);
                VenueAgendaFragment fragment = VenueAgendaFragment.newInstance(venueViewDto.key, venueViewDto.title);
                venueItem.setOnMenuItemClickListener(new NavigationMenuClickListener(fragment));
            }
        }
    }

    private class NavigationMenuClickListener implements MenuItem.OnMenuItemClickListener {

        final Fragment fragment;

        public NavigationMenuClickListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            replaceFragment(fragment);
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
    }
}
