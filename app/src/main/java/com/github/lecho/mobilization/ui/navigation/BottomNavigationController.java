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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leszek on 29.07.2016.
 */
public class BottomNavigationController implements NavigationController {

    private final FragmentActivity activity;
    private final NavigationItemListener navigationItemListener;
    private int checkedNavItemId;

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;

    public BottomNavigationController(FragmentActivity activity, View mainContainer, NavigationItemListener navigationItemListener) {
        ButterKnife.bind(this, mainContainer);
        this.activity = activity;
        this.navigationItemListener = navigationItemListener;
    }

    @Override
    public void start(Bundle savedInstanceState) {
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(activity, R.menu.menu_bottom_bar);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, null);
        final int accentColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            accentColor = activity.getResources().getColor(R.color.primary, null);
        } else {
            //noinspection deprecation
            accentColor = activity.getResources().getColor(R.color.primary);
        }
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
        }

        private void reselectTab(int position) {
            navigationItemListener.onItemReselected(position);
        }
    }
}
