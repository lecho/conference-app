package com.github.lecho.mobilization.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.lecho.mobilization.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SameSlotActivity extends AppCompatActivity {

    private static final String ARG_SLOT_KEY = "slot-key";

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    public static void startActivity(@NonNull Activity activity, @NonNull String slotKey) {
        Intent intent = new Intent(activity, SameSlotActivity.class);
        intent.putExtra(ARG_SLOT_KEY, slotKey);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_slot);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
