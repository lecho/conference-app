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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.NavigationViewDataLoader;
import com.github.lecho.conference.service.ContentUpdateService;
import com.github.lecho.conference.util.Utils;
import com.github.lecho.conference.viewmodel.EventViewDto;
import com.github.lecho.conference.viewmodel.NavigationViewDto;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<NavigationViewDto> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private NavigationMenuController navigationMenuController;
    private NavigationHeaderController navigationHeaderController;

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
        navigationMenuController = new NavigationMenuController(navigationView);
        navigationMenuController.bindStaticMenuItems();
        navigationHeaderController = new NavigationHeaderController(navigationView);

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
    public Loader<NavigationViewDto> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return NavigationViewDataLoader.getLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<NavigationViewDto> loader, NavigationViewDto navigationViewDto) {
        if (loader.getId() == LOADER_ID) {
            navigationMenuController.bindVenuesMenuItems(navigationViewDto.venueViewDtos);
            navigationHeaderController.bind(navigationViewDto.eventViewDto);
        }
    }

    @Override
    public void onLoaderReset(Loader<NavigationViewDto> loader) {
    }

    protected class NavigationMenuController {

        private NavigationView navigationView;
        private Menu navigationMenu;
        private SubMenu venuesSubMenu;
        private final int groupId = 0;

        public NavigationMenuController(NavigationView navigationView) {
            this.navigationView = navigationView;
            navigationMenu = navigationView.getMenu();
        }

        public void bindStaticMenuItems() {
            int itemId = 0;
            int order = 0;
            navigationMenu = navigationView.getMenu();
            navigationMenu.setGroupCheckable(groupId, true, true);
            //MyAgenda
            navigationMenu.add(groupId, itemId++, order++, R.string.navigation_my_agenda).setCheckable(true).setIcon
                    (R.drawable.ic_nav_my_agenda)
                    .setOnMenuItemClickListener(new NavigationMenuClickListener(MyAgendaFragment.newInstance()));
            //Tracks
            venuesSubMenu = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_venues);
            //More
            SubMenu subMenuMore = navigationMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_more);
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_speakers).setCheckable(true).setIcon(R
                    .drawable.ic_nav_speakers);
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_sponsors).setCheckable(true).setIcon(R
                    .drawable.ic_nav_sponsors)
                    .setOnMenuItemClickListener(new NavigationMenuClickListener(SponsorsFragment.newInstance()));
            subMenuMore.add(groupId, itemId++, order++, R.string.navigation_about).setCheckable(true).setIcon(R
                    .drawable.ic_nav_about);
        }

        public void bindVenuesMenuItems(@NonNull List<VenueViewDto> venueViewDtos) {
            int itemId = 0;
            int order = 0;
            venuesSubMenu.clear();
            for (VenueViewDto venueViewDto : venueViewDtos) {
                MenuItem venueItem = venuesSubMenu.add(groupId, itemId++, order++, venueViewDto.title).
                        setCheckable(true).setIcon(R.drawable.ic_nav_agenda);
                VenueAgendaFragment fragment = VenueAgendaFragment.newInstance(venueViewDto.key, venueViewDto.title);
                venueItem.setOnMenuItemClickListener(new NavigationMenuClickListener(fragment));
            }
        }
    }

    protected class NavigationHeaderController {

        @Bind(R.id.text_event_title)
        TextView eventTitleView;

        @Bind(R.id.text_event_date)
        TextView eventDateView;

        @Bind(R.id.text_event_place)
        TextView eventPlaceView;

        @Bind(R.id.map_button)
        ImageButton mapButton;

        public NavigationHeaderController(NavigationView navigationView) {
            ButterKnife.bind(this, navigationView);
        }

        public void bind(EventViewDto eventViewDto) {
            if (null == eventViewDto) {
                Log.w(TAG, "Null event data");
                return;
            }
            eventTitleView.setText(eventViewDto.title);
            StringBuilder eventDateText = new StringBuilder().append(eventViewDto.date).append(", ").append
                    (eventViewDto.time);
            eventDateView.setText(eventDateText.toString());
            StringBuilder eventPlaceText = new StringBuilder().append(eventViewDto.place).append("\n").append
                    (eventViewDto.street).append(" ").append(eventViewDto.city);
            eventPlaceView.setText(eventPlaceText.toString());
            mapButton.setOnClickListener(new MapButtonClickListener(eventViewDto.latitude, eventViewDto.longitude));
        }
    }

    private class MapButtonClickListener implements View.OnClickListener {

        private final double latitude;
        private final double longitude;

        public MapButtonClickListener(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void onClick(View v) {
            Utils.launchGMaps(MainActivity.this, latitude, longitude);
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
