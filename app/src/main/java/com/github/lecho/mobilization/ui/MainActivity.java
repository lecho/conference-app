package com.github.lecho.mobilization.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.view.KeyEvent;
import android.view.MenuItem;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.VenuesFragment;
import com.github.lecho.mobilization.ui.loader.EventViewDataLoader;
import com.github.lecho.mobilization.ui.navigation.NavViewController;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.EventViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Optional<EventViewModel>>,
        MyAgendaFragment.OpenDrawerCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ARG_CHECKED_NAV_ITEM_ID = "checked-nav-item-id";
    private static int DRAWER_GRAVITY = GravityCompat.START;
    private static final int LOADER_ID = 0;
    private NavViewController navViewController;
    private int checkedNavItemId;

    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navViewController = new NavViewController(navigationView, new MainActivityNavItemListener());
        navViewController.bindHeaderImage(getApplicationContext());
        navViewController.bindMenu();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            replaceFragment(MyAgendaFragment.newInstance());
            Utils.upgradeSchema(getApplicationContext());
            checkedNavItemId = 0;
        } else {
            checkedNavItemId = savedInstanceState.getInt(ARG_CHECKED_NAV_ITEM_ID);
        }
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CHECKED_NAV_ITEM_ID, checkedNavItemId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Workaround:/ https://code.google.com/p/android/issues/detail?id=183334
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
                drawerLayout.closeDrawer(DRAWER_GRAVITY);
            } else {
                onBackPressed();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
                drawerLayout.closeDrawer(DRAWER_GRAVITY);
            } else {
                drawerLayout.openDrawer(DRAWER_GRAVITY);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(DRAWER_GRAVITY);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (fragment instanceof VenuesFragment) {
                appBar.setElevation(0);
            } else {
                appBar.setElevation(getResources().getDimension(R.dimen.appbar_elevation));
            }
        }
    }

    @Override
    public Loader<Optional<EventViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return EventViewDataLoader.getLoader(getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<EventViewModel>> loader, Optional<EventViewModel> eventViewModelOptional) {
        if (loader.getId() == LOADER_ID) {
            if (eventViewModelOptional.isPresent()) {
                navViewController.bindHeader(getApplicationContext(), eventViewModelOptional.get());
            }
            navigationView.setCheckedItem(checkedNavItemId);
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<EventViewModel>> loader) {
    }

    @Override
    public void onOpenDrawer() {
        drawerLayout.openDrawer(DRAWER_GRAVITY);
    }

    private class MainActivityNavItemListener implements NavViewController.MenuItemListener {

        @Override
        public void onItemClick(@NonNull MenuItem item, @NonNull Fragment fragment) {
            checkedNavItemId = item.getItemId();
            drawerLayout.closeDrawer(DRAWER_GRAVITY);
            replaceFragment(fragment);
        }
    }
}
