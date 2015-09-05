package com.github.lecho.conference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.github.lecho.conference.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    Menu navigationViewMenu;

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
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_container, MyAgendaFragment.newInstance()).commit();
        }

        //ApiData apiData = new ApiFacade().parseJsonFilesFromAssets(this, "test-data");
        //RealmFacade facade = new RealmFacade(this);
        //facade.saveData(apiData);
        //AgendaViewDto agendaViewDto = facade.loadWholeAgenda();
        //Log.e("TAG", "Agenda: " + agendaViewDto.toString());
        setUpNavigationView();
    }

    private void setUpNavigationView(){
        final int groupId = 0;
        int itemId = 0;
        int order = 0;
        navigationViewMenu = navigationView.getMenu();
        navigationViewMenu.setGroupCheckable(groupId, true, true);
        navigationViewMenu.add(groupId, itemId++, order++, R.string.navigation_my_agenda).setCheckable(true);
        SubMenu subMenuTracks = navigationViewMenu.addSubMenu(groupId, itemId++, order++, "Tracks");
        subMenuTracks.add(groupId, itemId++, order++, "Mobica Track").setCheckable(true);
        subMenuTracks.add(groupId, itemId++, order++, "TomTom Track").setCheckable(true);
        subMenuTracks.add(groupId, itemId++, order++, "Seamless Track").setCheckable(true);
        subMenuTracks.add(groupId, itemId++, order++, "Harman Track").setCheckable(true);
        subMenuTracks.add(groupId, itemId++, order++, "HP Track").setCheckable(true);
        SubMenu subMenuMore = navigationViewMenu.addSubMenu(groupId, itemId++, order++, "More");
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_speakers).setCheckable(true);
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_sponsors).setCheckable(true);
        subMenuMore.add(groupId, itemId++, order++, R.string.navigation_about).setCheckable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_settings: {
                Intent intent = new Intent(this, TalkActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings2: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_container, VenueAgendaFragment.newInstance())
                        .commit();
                return true;
            }
            case R.id.action_settings3: {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_container, SponsorsFragment.newInstance())
                        .commit();
                return true;
            }
            case R.id.action_settings4: {
                Intent intent = new Intent(this, SpeakerActivity.class);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
