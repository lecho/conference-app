package com.github.lecho.mobilization.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.loader.TalkLoader;
import com.github.lecho.mobilization.ui.view.SpeakerSmallLayout;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SlotViewModel;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalkActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Optional<TalkViewModel>> {

    private static final String TAG = TalkActivity.class.getSimpleName();
    private static final String ARG_TALK_KEY = "talk-key";
    private static final int LOADER_ID = 0;
    private static final String SHARE_INTENT_TYPE = "text/plain";
    private String talkKey;
    private FABController fabController;
    private HeaderController headerController;
    private DescriptionController descriptionController;
    private SpeakersController speakersController;
    private TalkViewModel talkViewModel;
    private FirebaseAnalytics firebaseAnalytics;

    @BindView(R.id.main_container)
    View mainContainerView;

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    public static void startActivity(@NonNull Activity activity, @NonNull String talkKey) {
        Intent intent = new Intent(activity, TalkActivity.class);
        intent.putExtra(ARG_TALK_KEY, talkKey);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        fabController = new FABController(mainContainerView);
        headerController = new HeaderController(mainContainerView);
        descriptionController = new DescriptionController(mainContainerView);
        speakersController = new SpeakersController(mainContainerView);

        setSupportActionBar(toolbarView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        talkKey = getIntent().getStringExtra(ARG_TALK_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_talk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                shareTalk();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTalk() {
        if (talkViewModel != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, talkViewModel.title);
            intent.setType(SHARE_INTENT_TYPE);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }
    }

    @Override
    public Loader<Optional<TalkViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            Log.w(TAG, "Create talk loader for key: " + talkKey);
            return TalkLoader.getLoader(this, talkKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<TalkViewModel>> loader, Optional<TalkViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            Log.w(TAG, "Loaded talk data: " + talkKey);
            if (!data.isPresent()) {
                Log.w(TAG, "Talk data is null for talk-key: " + talkKey);
                return;
            }
            talkViewModel = data.get();
            fabController.bind(talkViewModel);
            headerController.bind(talkViewModel);
            descriptionController.bind(talkViewModel);
            speakersController.bind(talkViewModel);
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<TalkViewModel>> loader) {
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
        }
    }

    protected class HeaderController {

        @BindView(R.id.text_time_slot)
        TextView timeSlotView;

        @BindView(R.id.text_title)
        TextView talkTitleView;

        @BindView(R.id.text_venue)
        TextView talkVenueView;

        @BindView(R.id.text_language)
        TextView talkLanguageView;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewModel talkViewModel) {
            SlotViewModel.SlotInTimeZone slotInTimeZone = SlotViewModel.SlotInTimeZone.getSlotInTimezone(talkViewModel
                    .slot);
            timeSlotView.setText(slotInTimeZone.getTimeSlotText());
            talkTitleView.setText(talkViewModel.title);
            talkVenueView.setText(talkViewModel.venue.getVenueText(getApplicationContext()));
            talkLanguageView.setText(talkViewModel.getLanguageInBrackets());
        }
    }

    protected class DescriptionController {

        @BindView(R.id.text_description)
        TextView descriptionView;

        public DescriptionController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewModel talkViewModel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                descriptionView.setText(Html.fromHtml(talkViewModel.description, Html.FROM_HTML_MODE_COMPACT));
            } else {
                descriptionView.setText(Html.fromHtml(talkViewModel.description));
            }
        }
    }

    protected class SpeakersController {

        @BindView(R.id.speakers_layout)
        LinearLayout speakersLayout;

        public SpeakersController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewModel talkViewModel) {
            speakersLayout.removeAllViews();
            for (SpeakerViewModel speakerViewModel : talkViewModel.speakers) {
                SpeakerSmallLayout speakerSimpleLayout = new SpeakerSmallLayout(TalkActivity.this,
                        speakerViewModel);
                speakerSimpleLayout.bind();
                speakersLayout.addView(speakerSimpleLayout);
                speakerSimpleLayout.setOnClickListener(view -> {
                    SpeakerActivity.startActivity(TalkActivity.this, speakerViewModel.key);
                    AnalyticsReporter.logSpeakerSelected(firebaseAnalytics, speakerViewModel.key);
                });
            }
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
                AnalyticsReporter.logTalkRemoved(firebaseAnalytics, talkViewModel.key);
            } else {
                //TODO use optimistic result and move checking slot conflict off main thread, then use RxBus to trigger
                //T dialog if necessary.
                if (Utils.checkSlotConflict(TalkActivity.this, talkViewModel.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                floatingActionButton.setImageResource(R.drawable.ic_star_24);
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(talkViewModel.key);
                AnalyticsReporter.logTalkAdded(firebaseAnalytics, talkViewModel.key);
            }
        }
    }
}
