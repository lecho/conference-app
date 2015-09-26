package com.github.lecho.conference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.github.lecho.conference.R;
import com.github.lecho.conference.apimodel.ApiData;
import com.github.lecho.conference.apimodel.ApiFacade;
import com.github.lecho.conference.realmmodel.RealmFacade;
import com.github.lecho.conference.service.ContentUpdateService;
import com.github.lecho.conference.viewmodel.AgendaViewDto;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    private Menu navigationViewMenu;

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

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (null == savedInstanceState) {
            replaceFragment(MyAgendaFragment.newInstance());
            updateContent();
        }

        List<VenueViewDto> venueViewDtos = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            VenueViewDto venueViewDto = new VenueViewDto();
            venueViewDto.key = "venue-1";
            venueViewDto.title = "Venue" + i;
            venueViewDtos.add(venueViewDto);
        }

        setUpNavigationView(venueViewDtos);
    }

    private void updateContent() {
        Intent serviceIntent = new Intent(getApplicationContext(), ContentUpdateService.class);
        startService(serviceIntent);
    }

    private void setUpNavigationView(@NonNull List<VenueViewDto> venueViewDtos) {
        final int groupId = 0;
        int itemId = 0;
        int order = 0;
        navigationViewMenu = navigationView.getMenu();
        navigationViewMenu.setGroupCheckable(groupId, true, true);

        navigationViewMenu.add(groupId, itemId++, order++, R.string.navigation_my_agenda).setCheckable(true)
                .setOnMenuItemClickListener(new NavigationMenuClickListener(MyAgendaFragment.newInstance()));

        SubMenu subMenuVenues = navigationViewMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_venues);
        String trackPostfix = getString(R.string.navigation_track);
        for (VenueViewDto venueViewDto : venueViewDtos) {
            String venueName = new StringBuilder(venueViewDto.title).append(" ").append(trackPostfix).toString();
            MenuItem venueItem = subMenuVenues.add(groupId, itemId++, order++, venueName).setCheckable(true);
            VenueAgendaFragment fragment = VenueAgendaFragment.newInstance(venueViewDto.key);
            venueItem.setOnMenuItemClickListener(new NavigationMenuClickListener(fragment));
        }

        SubMenu subMenuMore = navigationViewMenu.addSubMenu(groupId, itemId++, order++, R.string.navigation_more);
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_speakers).setCheckable(true);
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_sponsors).setCheckable(true)
                .setOnMenuItemClickListener(new NavigationMenuClickListener(SponsorsFragment.newInstance()));
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_about).setCheckable(true);
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
