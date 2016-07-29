package com.github.lecho.mobilization.ui.navigation;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.github.lecho.mobilization.R;

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

    public BottomNavigationController(FragmentActivity activity, View mainContainer, NavigationItemListener navItemListener) {
        ButterKnife.bind(this, mainContainer);
        this.activity = activity;
        this.navigationItemListener = navItemListener;
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
        bottomNavigation.setAccentColor(accentColor);
    }

    @Override
    public void open() {
        //do nothing, bottom navigation is always opened.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //TODO return to home view
        return false;
    }
}
