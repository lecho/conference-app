package com.github.lecho.mobilization.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.fragment.SpeakersFragment;
import com.github.lecho.mobilization.ui.fragment.SponsorsFragment;

public class SponsorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);

        if (savedInstanceState == null) {
            final Fragment fragment = SponsorsFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
        }
    }

}
