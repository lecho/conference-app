package com.github.lecho.mobilization.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.loader.SpeakerLoader;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewDto;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakerActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Optional<SpeakerViewDto>> {

    private static final String TAG = SpeakerActivity.class.getSimpleName();
    private static final String ARG_SPEAKER_KEY = "speaker-key";
    private static final int LOADER_ID = 0;
    private String speakerKey;
    private HeaderController headerController;
    private InfoCardController infoCardController;

    @BindView(R.id.main_container)
    View mainContainerView;

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    public static void startActivity(Context context, String speakerKey) {
        Intent intent = new Intent(context, SpeakerActivity.class);
        intent.putExtra(ARG_SPEAKER_KEY, speakerKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        ButterKnife.bind(this);

        headerController = new HeaderController(mainContainerView);
        headerController.bindHeaderImage();
        infoCardController = new InfoCardController(mainContainerView);

        setSupportActionBar(toolbarView);
        collapsingToolbarLayout.setTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        speakerKey = getIntent().getStringExtra(ARG_SPEAKER_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Workaround:/ https://code.google.com/p/android/issues/detail?id=183334
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    public Loader<Optional<SpeakerViewDto>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SpeakerLoader.getLoader(this, speakerKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<SpeakerViewDto>> loader, Optional<SpeakerViewDto> data) {
        if (loader.getId() == LOADER_ID) {
            if (!data.isPresent()) {
                Log.w(TAG, "Speaker data is null for speaker-key: " + speakerKey);
                return;
            }
            SpeakerViewDto speakerViewDto = data.get();
            headerController.bind(speakerViewDto);
            infoCardController.bind(speakerViewDto);
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<SpeakerViewDto>> loader) {
    }

    protected class HeaderController {

        private static final String SPEAKER_HEADER_IMAGE = "speaker_header.jpg";
        @BindView(R.id.header_image)
        ImageView headerImageView;

        @BindView(R.id.speaker_avatar)
        ImageView avatarView;

        @BindView(R.id.text_speaker_name)
        TextView speakerNameView;

        @BindView(R.id.www_button)
        ImageButton wwwButton;

        @BindView(R.id.twitter_button)
        ImageButton twitterButton;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindHeaderImage() {
            Utils.loadHeaderImage(getApplicationContext(), SPEAKER_HEADER_IMAGE, headerImageView);
        }

        public void bind(final SpeakerViewDto speakerViewDto) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(speakerViewDto.firstName);
            }
            speakerNameView.setText(speakerViewDto.getSpeakerNameText());
            Utils.loadSpeakerImageBig(getApplicationContext(), speakerViewDto.photo, avatarView);
            setUpWwwButton(speakerViewDto);
            setUpTwitterButton(speakerViewDto);
        }

        private void setUpWwwButton(final SpeakerViewDto speakerViewDto) {
            if (TextUtils.isEmpty(speakerViewDto.wwwPage)) {
                wwwButton.setEnabled(false);
            } else {
                wwwButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.openWebBrowser(getApplicationContext(), speakerViewDto.wwwPage);
                    }
                });
            }
        }

        private void setUpTwitterButton(final SpeakerViewDto speakerViewDto) {
            if (TextUtils.isEmpty(speakerViewDto.twitterProfile)) {
                twitterButton.setEnabled(false);
            } else {
                twitterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.openTwitter(getApplicationContext(), speakerViewDto.twitterProfile);
                    }
                });
            }
        }
    }

    protected class InfoCardController {

        @BindView(R.id.text_info)
        TextView speakerInfoView;

        public InfoCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(SpeakerViewDto speakerViewDto) {
            speakerInfoView.setText(Html.fromHtml(speakerViewDto.biography));
        }
    }
}
