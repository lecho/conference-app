package com.github.lecho.mobilization.ui.navigation;

import android.os.Bundle;
import android.view.KeyEvent;

/**
 * Created by Leszek on 28.07.2016.
 */
public interface NavigationController {

    public void start(Bundle savedInstanceState);

    public void show();

    public void openItem(int position);

    public void onSaveInstanceState(Bundle outState);

}
