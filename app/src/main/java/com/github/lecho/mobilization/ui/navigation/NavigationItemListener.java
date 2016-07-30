package com.github.lecho.mobilization.ui.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

/**
 * Created by Leszek on 21.07.2016.
 */
public interface NavigationItemListener {

    void onItemClick(int itemId, @NonNull Fragment fragment);

    void onItemReselected(int itemId);
}
