package com.github.lecho.mobilization.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.rx.DatabaseUpdatedEvent;
import com.github.lecho.mobilization.ui.dialog.PlayServicesErrorDialogFragment;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.Scrollable;
import com.github.lecho.mobilization.ui.navigation.BottomNavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationDrawerController;
import com.github.lecho.mobilization.ui.navigation.NavigationItemListener;
import com.github.lecho.mobilization.ui.snackbar.SnackbarHelper;
import com.github.lecho.mobilization.ui.snackbar.SnackbarOfflineEvent;
import com.github.lecho.mobilization.ui.snackbar.SnackbarUpToDate;
import com.github.lecho.mobilization.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseMainActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1;
    private NavigationController navigationController;
    private SnackbarHelper snackbarHelper;

    @BindView(R.id.main_container)
    View mainContainer;

    @BindView(R.id.coordinator_layout)
    View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        snackbarHelper = new SnackbarHelper(this, coordinatorLayout);

        if (savedInstanceState == null) {
            checkPlayServices();
            replaceFragment(MyAgendaFragment.newInstance());
            Utils.upgradeSchema(getApplicationContext());
        }
        setUpNavigation(savedInstanceState);
    }

    private void setUpNavigation(Bundle savedInstanceState) {
        final boolean isNavigationDrawerEnabled = getResources().getBoolean(R.bool.is_navigation_drawer_enabled);
        if (isNavigationDrawerEnabled) {
            navigationController = new NavigationDrawerController(this, mainContainer, new MainActivityNavItemListener());
        } else {
            navigationController = new BottomNavigationController(this, mainContainer, new MainActivityNavItemListener());
        }
        navigationController.start(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigationController.onSaveInstanceState(outState);
    }

    public void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.content_container, fragment).commit();
    }

    private void checkPlayServices() {
        final GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int playServicesStatus = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (playServicesStatus == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Play Services status SUCCESS");
            return;
        }

        if (googleApiAvailability.isUserResolvableError(playServicesStatus)) {
            Log.i(TAG, "Play Services user recoverable - proceed by calling error dialog");
            DialogFragment dialog = PlayServicesErrorDialogFragment.newInstance(playServicesStatus,
                    REQUEST_CODE_RECOVER_PLAY_SERVICES);
            FragmentManager fm = getSupportFragmentManager();
            dialog.show(fm, "play-services-dialog");
        } else {
            Log.i(TAG, "Play Services not user recoverable - finishing app");
            Toast.makeText(getApplicationContext(), R.string.play_services_missing, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        snackbarHelper.subscribe(SnackbarOfflineEvent.class, R.string.text_offline);
        snackbarHelper.subscribe(SnackbarUpToDate.class, R.string.text_up_to_date);
        snackbarHelper.subscribe(DatabaseUpdatedEvent.class, R.string.text_agenda_updated);
    }


    @Override
    protected void onPause() {
        super.onPause();
        snackbarHelper.unsubscribe(SnackbarOfflineEvent.class);
        snackbarHelper.unsubscribe(SnackbarUpToDate.class);
        snackbarHelper.unsubscribe(DatabaseUpdatedEvent.class);
    }

    private class MainActivityNavItemListener implements NavigationItemListener {

        @Override
        public void onItemClick(int itemId, @NonNull Fragment fragment) {
            replaceFragment(fragment);
        }

        @Override
        public void onItemReselected(int itemId) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_container);
            if (fragment instanceof Scrollable) {
                ((Scrollable) fragment).scrollToTop();
            }
        }
    }
}
