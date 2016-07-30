package com.github.lecho.mobilization.ui;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.MyAgendaFragment;
import com.github.lecho.mobilization.ui.fragment.Scrollable;
import com.github.lecho.mobilization.ui.navigation.BottomNavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationController;
import com.github.lecho.mobilization.ui.navigation.NavigationItemListener;
import com.github.lecho.mobilization.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MyAgendaFragment.OpenDrawerCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private NavigationController navigationController;

    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_container)
    View mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //TODO home button depends on navigation implementation(maybe remove it)
        setUpHomeButton();

        if (savedInstanceState == null) {
            replaceFragment(MyAgendaFragment.newInstance());
            Utils.upgradeSchema(getApplicationContext());
        }

        navigationController = new BottomNavigationController(this, mainContainer, new MainActivityNavItemListener());
        navigationController.start(savedInstanceState);
    }

    private void setUpHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        navigationController.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Workaround:/ https://code.google.com/p/android/issues/detail?id=183334
        boolean isEventHandled = navigationController.onKeyDown(keyCode, event);
        if (isEventHandled) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                navigationController.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
        showAppBarShadow();
    }

    public void showAppBarShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.drawable
                    .selector_appbar_shadow);
            appBar.setStateListAnimator(animator);
        }
    }

    public void hideAppBarShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = AnimatorInflater.loadStateListAnimator(getApplicationContext(), R.drawable
                    .selector_appbar_no_shadow);
            appBar.setStateListAnimator(animator);
        }
    }

    //TODO Get rid of this method
    @Override
    public void onOpenDrawer() {
        navigationController.show();
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
                ((Scrollable)fragment).scrollToTop();
            }
        }
    }
}
