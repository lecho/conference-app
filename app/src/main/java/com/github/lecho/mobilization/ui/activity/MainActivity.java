package com.github.lecho.mobilization.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.Scrollable;
import com.github.lecho.mobilization.ui.navigation.BottomNavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationDrawerController;
import com.github.lecho.mobilization.ui.navigation.NavigationItemListener;
import com.github.lecho.mobilization.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private NavigationController navigationController;

    @BindView(R.id.main_container)
    View mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
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
