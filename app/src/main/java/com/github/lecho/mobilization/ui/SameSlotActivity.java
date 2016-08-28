package com.github.lecho.mobilization.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.controller.VenueViewController;
import com.github.lecho.mobilization.ui.loader.SameSlotLoader;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SameSlotActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<TalkViewModel>> {

    public static final String TAG = SameSlotActivity.class.getSimpleName();
    private static final String ARG_SLOT_KEY = "slot-key";
    private static final int LOADER_ID = 0;
    private SameSlotPagerAdapter pagerAdapter;
    private String slotKey;
    private FABController fabController;

    @BindView(R.id.main_container)
    View mainContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

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
        fabController = new FABController(mainContainer);

        slotKey = getIntent().getStringExtra(ARG_SLOT_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fabController.bind(pagerAdapter.getTalkViewModel(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    @Override
    public Loader<List<TalkViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SameSlotLoader.getLoader(getApplicationContext(), slotKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<TalkViewModel>> loader, List<TalkViewModel> talkViewModels) {
        if (loader.getId() == LOADER_ID) {
            //TODO handle situation when there is no talk in a slot(that should not happen)
            pagerAdapter = new SameSlotPagerAdapter(talkViewModels);
            viewPager.setAdapter(pagerAdapter);
            final int position = viewPager.getCurrentItem();
            fabController.bind(pagerAdapter.getTalkViewModel(position));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TalkViewModel>> loader) {
    }

    private class SameSlotPagerAdapter extends PagerAdapter {

        private List<TalkViewModel> talks;
        private VenueViewController[] controllers;

        public SameSlotPagerAdapter(List<TalkViewModel> talks) {
            this.talks = talks;
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
            return talkViewModel.venue.title;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = LayoutInflater.from(SameSlotActivity.this).inflate(R.layout.item_same_slot_talk, collection,
                    false);
//            VenueViewController controller = new VenueViewController(SameSlotActivity.this, getLoaderManager(), new
//                    LinearLayoutManager(getApplicationContext()), talks.get(position), view, position);
//            controller.bindView();
            CardView c = (CardView) view.findViewById(R.id.card_view);
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            collection.addView(view);
//            controllers[position] = controller;
            return view;
        }

        public TalkViewModel getTalkViewModel(int position) {
            return talks.get(position);
        }
    }

    protected class FABController {

        @BindView(R.id.fab)
        FloatingActionButton fab;

        public FABController(View mainContainer) {
            ButterKnife.bind(this, mainContainer);
        }

        public void bind(TalkViewModel talkViewModel) {
            if (talkViewModel.isInMyAgenda) {
                fab.setImageResource(R.drawable.ic_star_24);
            } else {
                fab.setImageResource(R.drawable.ic_star_border_24);
            }
            fab.setOnClickListener(new FabClickListener(talkViewModel));
            fab.show();
        }
    }

    private class FabClickListener implements View.OnClickListener {

        private TalkViewModel talkViewModel;

        public FabClickListener(TalkViewModel talkViewModel) {
            this.talkViewModel = talkViewModel;
        }

        @Override
        public void onClick(View v) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) v;
            if (talkViewModel.isInMyAgenda) {
                floatingActionButton.setImageResource(R.drawable.ic_star_border_24);
                talkViewModel.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(talkViewModel.key);
            } else {
                //TODO use optimistic result and move checking slot conflict off main thread, then use RxBus to trigger
                //T dialog if necessary.
                if (Utils.checkSlotConflict(SameSlotActivity.this, talkViewModel.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                floatingActionButton.setImageResource(R.drawable.ic_star_24);
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(talkViewModel.key);
            }
        }
    }
}
