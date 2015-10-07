package com.github.lecho.conference.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.async.TalkAsyncHelper;
import com.github.lecho.conference.ui.loader.TalkLoader;
import com.github.lecho.conference.ui.snackbar.SnackbarForTalkHelper;
import com.github.lecho.conference.ui.view.SpeakerForTalkLayout;
import com.github.lecho.conference.util.Optional;
import com.github.lecho.conference.util.Utils;
import com.github.lecho.conference.viewmodel.SlotViewDto;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Optional<TalkViewDto>> {

    private static final String TAG = TalkActivity.class.getSimpleName();
    private static final String ARG_TALK_KEY = "talk-key";
    private static final int LOADER_ID = 0;
    private String talkKey;
    private FABController fabController;
    private HeaderController headerController;
    private InfoCardController infoCardController;
    private SpeakersCardController speakersCardController;
    private SnackbarForTalkHelper snackbarForTalkHelper;

    @Bind(R.id.main_container)
    View mainContainerView;

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

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

        fabController = new FABController(mainContainerView);
        headerController = new HeaderController(mainContainerView);
        infoCardController = new InfoCardController(mainContainerView);
        speakersCardController = new SpeakersCardController(mainContainerView);
        snackbarForTalkHelper = new SnackbarForTalkHelper(getApplicationContext(), toolbarView);

        setSupportActionBar(toolbarView);
        collapsingToolbarLayout.setTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        talkKey = getIntent().getStringExtra(ARG_TALK_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        snackbarForTalkHelper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snackbarForTalkHelper.onResume();
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
    public Loader<Optional<TalkViewDto>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            Log.w(TAG, "Create talk loader for key: " + talkKey);
            return TalkLoader.getLoader(this, talkKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<TalkViewDto>> loader, Optional<TalkViewDto> data) {
        if (loader.getId() == LOADER_ID) {
            Log.w(TAG, "Loaded talk data: " + talkKey);
            if (!data.isPresent()) {
                Log.w(TAG, "Talk data is null for talk-key: " + talkKey);
                return;
            }
            TalkViewDto talkViewDto = data.get();
            fabController.bind(talkViewDto);
            headerController.bind(talkViewDto);
            infoCardController.bind(talkViewDto);
            speakersCardController.bind(talkViewDto);

        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<TalkViewDto>> loader) {
    }

    protected class FABController {

        @Bind(R.id.button_add_to_my_agenda)
        FloatingActionButton addToMyAgendaButton;

        public FABController(View mainContainer) {
            ButterKnife.bind(this, mainContainer);
        }

        public void bind(TalkViewDto talkViewDto) {
            if (talkViewDto.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_accent_big);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_accent_big);
            }
            addToMyAgendaButton.setOnClickListener(new AddToMyAgendaClickListener(getApplicationContext(),
                    talkViewDto));
            addToMyAgendaButton.show();
        }
    }

    protected class HeaderController {

        private static final String TALK_HEADER_IMAGE = "header_image1.jpg";

        @Bind(R.id.header_image)
        ImageView headerImageView;

        @Bind(R.id.text_talk_title)
        TextView talkTitleView;

        @Bind(R.id.text_venue)
        TextView talkVenueView;

        @Bind(R.id.text_language)
        TextView talkLanguageView;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewDto talkViewDto) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                SlotViewDto.SlotInTimeZone slotInTimeZone = SlotViewDto.SlotInTimeZone.getSlotInTimezone(talkViewDto
                        .slot);
                actionBar.setTitle(slotInTimeZone.getTimeSlotText());
            }
            talkTitleView.setText(talkViewDto.title);
            Utils.loadHeaderImage(getApplicationContext(), TALK_HEADER_IMAGE, headerImageView);
            talkVenueView.setText(talkViewDto.venue.getVenueText(getApplicationContext()));
            talkLanguageView.setText(talkViewDto.language);
        }
    }

    protected class InfoCardController {

        @Bind(R.id.text_info)
        TextView talkInfoView;

        public InfoCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewDto talkViewDto) {
            talkInfoView.setText(talkViewDto.description);
        }
    }

    protected class SpeakersCardController {

        @Bind(R.id.speakers_layout)
        LinearLayout speakersLayout;

        public SpeakersCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewDto talkViewDto) {
            speakersLayout.removeAllViews();
            for (SpeakerViewDto speakerViewDto : talkViewDto.speakers) {
                SpeakerForTalkLayout speakerForTalkLayout = new SpeakerForTalkLayout(TalkActivity.this, speakerViewDto);
                speakerForTalkLayout.bind();
                speakersLayout.addView(speakerForTalkLayout);
            }
        }
    }

    private class AddToMyAgendaClickListener implements View.OnClickListener {

        private TalkViewDto talkViewDto;
        private Context context;

        public AddToMyAgendaClickListener(Context context, TalkViewDto talkViewDto) {
            this.context = context;
            this.talkViewDto = talkViewDto;
        }

        @Override
        public void onClick(View v) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) v;
            if (talkViewDto.isInMyAgenda) {
                floatingActionButton.setImageResource(R.drawable.ic_star_border_accent_big);
                talkViewDto.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(context.getApplicationContext(), talkViewDto.key);
            } else {
                if (Utils.checkSlotConflict(TalkActivity.this, talkViewDto.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewDto.key);
                    return;
                }
                floatingActionButton.setImageResource(R.drawable.ic_star_accent_big);
                talkViewDto.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(context.getApplicationContext(), talkViewDto.key);
            }
        }
    }
}
