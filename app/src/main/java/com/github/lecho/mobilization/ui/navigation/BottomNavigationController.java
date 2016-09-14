package com.github.lecho.mobilization.ui.navigation;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.EventFragment;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.VenuesFragment;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 29.07.2016.
 */
public class BottomNavigationController implements NavigationController {

    private final FragmentActivity activity;
    private final NavigationItemListener navigationItemListener;
    private final FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    public BottomNavigationController(FragmentActivity activity, View mainContainer, NavigationItemListener navigationItemListener) {
        ButterKnife.bind(this, mainContainer);
        this.activity = activity;
        this.navigationItemListener = navigationItemListener;
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(activity.getApplicationContext());
    }

    @Override
    public void start(Bundle savedInstanceState) {
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(activity, R.menu.menu_bottom_bar);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, null);
        final int accentColor;
        final int backgroundColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            accentColor = activity.getResources().getColor(R.color.primary, null);
            backgroundColor = activity.getResources().getColor(R.color.bottom_nav_background, null);
        } else {
            accentColor = activity.getResources().getColor(R.color.primary);
            backgroundColor = activity.getResources().getColor(R.color.bottom_nav_background);
        }
        bottomNavigation.setColored(false);
        bottomNavigation.setDefaultBackgroundColor(backgroundColor);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setAccentColor(accentColor);
        bottomNavigation.setOnTabSelectedListener(new BottomNavigationItemListener());
    }

    @Override
    public void show() {
        //do nothing, bottom navigation is always visible.
    }

    @Override
    public void openItem(int position) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private class BottomNavigationItemListener implements AHBottomNavigation.OnTabSelectedListener {

        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            if (wasSelected) {
                reselectTab(position);
            } else {
                selectTab(position);
            }
            return true;
        }

        private void selectTab(int position) {
            final Fragment fragment;
            switch (position) {
                case 0:
                    fragment = MyAgendaFragment.newInstance();
                    break;
                case 1:
                    fragment = VenuesFragment.newInstance();
                    break;
                case 2:
                    fragment = EventFragment.newInstance();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid navigation item position: " + position);
            }
            navigationItemListener.onItemClick(position, fragment);
            AnalyticsReporter.logNavigationEvent(firebaseAnalytics, String.valueOf(position), getItemTitle(position));
        }

        private String getItemTitle(int position) {
            final String title;
            switch (position) {
                case 0:
                    title = "MyAgenda";
                    break;
                case 1:
                    title = "Tracks";
                    break;
                case 2:
                    title = "Event";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid navigation item position: " + position);
            }
            return title;
        }

        private void reselectTab(int position) {
            navigationItemListener.onItemReselected(position);
        }
    }
}
