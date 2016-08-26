package com.github.lecho.mobilization.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.controller.VenueViewController;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SameSlotActivity extends AppCompatActivity {

    private static final String ARG_SLOT_KEY = "slot-key";

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_strip_layout)
    TabLayout tabStripLayout;

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

    private class SlotPagerAdapter extends PagerAdapter {

        private List<TalkViewModel> talks;
        private VenueViewController[] controllers;

        public SlotPagerAdapter(List<TalkViewModel> talks) {
            this.talks = talks;
            this.controllers = new VenueViewController[talks.size()];
        }

        @Override
        public int getCount() {
            return talks.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            TalkViewModel talkViewModel = talks.get(position);
            return talkViewModel.venue.getVenueText(SameSlotActivity.this);
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
            controllers[position] = null;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler_view, collection, false);
//            VenueViewController controller = new VenueViewController(SameSlotActivity.this, getLoaderManager(), new
//                    LinearLayoutManager(getApplicationContext()), talks.get(position), view, position);
//            controller.bindView();
//            collection.addView(view);
//            controllers[position] = controller;
            return view;
        }

        public Optional<VenueViewController> getVenueViewController(int position) {
            return Optional.of(controllers[position]);
        }
    }
}
