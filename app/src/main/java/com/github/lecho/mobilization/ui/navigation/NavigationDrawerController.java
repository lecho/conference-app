package com.github.lecho.mobilization.ui.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.rx.RxBus;
import com.github.lecho.mobilization.ui.dialog.JsonUpdateDialogFragment;
import com.github.lecho.mobilization.ui.fragment.AboutFragment;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.SpeakersFragment;
import com.github.lecho.mobilization.ui.fragment.SponsorsFragment;
import com.github.lecho.mobilization.ui.fragment.VenuesFragment;
import com.github.lecho.mobilization.ui.loader.EventViewDataLoader;
import com.github.lecho.mobilization.ui.snackbar.SnackbarOfflineEvent;
import com.github.lecho.mobilization.ui.snackbar.SnackbarUpToDate;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.EventViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 2015-10-10.
 */
public class NavigationDrawerController implements NavigationController,
        LoaderManager.LoaderCallbacks<Optional<EventViewModel>> {

    private static final String ARG_CHECKED_NAV_ITEM_ID = "checked-nav-item-id";
    private static final int LOADER_ID = 0;
    private final FragmentActivity activity;
    private int checkedNavItemId;
    private final NavHeaderController navHeaderController;
    private final NavMenuController navMenuController;
    private final FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    public NavigationDrawerController(FragmentActivity activity, View mainContainer, NavigationItemListener navigationItemListener) {
        ButterKnife.bind(this, mainContainer);
        this.activity = activity;
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(activity.getApplicationContext());
        this.navHeaderController = new NavHeaderController(navigationView);
        this.navMenuController = new NavMenuController(navigationView, navigationItemListener, firebaseAnalytics);
    }

    @Override
    public void start(Bundle savedInstanceState) {
        bindHeaderImage(activity.getApplicationContext());
        bindMenu();
        if (savedInstanceState == null) {
            checkedNavItemId = 0;
        } else {
            checkedNavItemId = savedInstanceState.getInt(ARG_CHECKED_NAV_ITEM_ID);
        }
        activity.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void show() {
    }

    @Override
    public void openItem(int position) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_CHECKED_NAV_ITEM_ID, checkedNavItemId);
    }

    private void bindHeader(@NonNull Context context, @NonNull EventViewModel eventViewModel) {
        navHeaderController.bind(context, eventViewModel);
    }

    private void bindHeaderImage(@NonNull Context context) {
        navHeaderController.bindHeaderImage(context);
    }

    private void bindMenu() {
        navMenuController.bind();
    }

    @Override
    public Loader<Optional<EventViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return EventViewDataLoader.getLoader(activity.getApplicationContext());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<EventViewModel>> loader, Optional<EventViewModel> eventViewModelOptional) {
        if (loader.getId() == LOADER_ID) {
            if (eventViewModelOptional.isPresent()) {
                bindHeader(activity.getApplicationContext(), eventViewModelOptional.get());
            }
            navigationView.setCheckedItem(checkedNavItemId);
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<EventViewModel>> loader) {
    }

    static class NavHeaderController {

        private static final String NAVIGATION_HEADER_IMAGE = "map.jpg";

        @BindView(R.id.image_map)
        ImageView headerView;

        @BindView(R.id.text_event_date)
        TextView eventDateView;

        @BindView(R.id.text_event_place)
        TextView eventPlaceView;

        @BindView(R.id.button_map)
        ImageButton mapButton;

        @BindView(R.id.button_sync)
        ImageButton syncButton;

        public NavHeaderController(@NonNull NavigationView navigationView) {
            ButterKnife.bind(this, navigationView.getHeaderView(0));
        }

        public void bind(@NonNull Context context, @NonNull EventViewModel event) {
            eventDateView.setText(event.getDate());
            eventPlaceView.setText(event.getPlace());
            mapButton.setOnClickListener(view -> Utils.launchGMaps(context, event.latitude, event
                    .longitude, event.getAddress()));
            syncButton.setOnClickListener(view -> {
                if (!Utils.isOnline(context.getApplicationContext())) {
                    RxBus.post(new SnackbarOfflineEvent());
                    return;
                }
                if (Utils.checkIfJsonUpdateNeeded(context.getApplicationContext())) {
                    JsonUpdateDialogFragment.show((AppCompatActivity) context);
                } else {
                    RxBus.post(new SnackbarUpToDate());
                }
            });
        }

        public void bindHeaderImage(@NonNull Context context) {
            Utils.loadHeaderImage(context.getApplicationContext(), NAVIGATION_HEADER_IMAGE, headerView);
        }
    }

    private static class NavMenuController {

        private final NavigationView navigationView;
        private final NavigationItemListener listener;
        private final FirebaseAnalytics firebaseAnalytics;

        public NavMenuController(@NonNull NavigationView navigationView, @NonNull NavigationItemListener listener,
                                 @NonNull FirebaseAnalytics firebaseAnalytics) {
            this.navigationView = navigationView;
            this.listener = listener;
            this.firebaseAnalytics = firebaseAnalytics;
        }

        public void bind() {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    final Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.nav_my_agenda:
                            fragment = MyAgendaFragment.newInstance();
                            break;
                        case R.id.nav_venues:
                            fragment = VenuesFragment.newInstance();
                            break;
                        case R.id.nav_speakers:
                            fragment = SpeakersFragment.newInstance();
                            break;
                        case R.id.nav_sponsors:
                            fragment = SponsorsFragment.newInstance();
                            break;
                        case R.id.nav_about:
                            fragment = AboutFragment.newInstance();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid navigation item: " + item);
                    }
                    listener.onItemClick(item.getItemId(), fragment);
                    AnalyticsReporter.logNavigationEvent(firebaseAnalytics, item.getTitle().toString());
                    return true;
                }
            });
        }
    }
}
