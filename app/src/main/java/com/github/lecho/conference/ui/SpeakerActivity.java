package com.github.lecho.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.SpeakerLoader;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpeakerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<SpeakerViewDto> {

    private static final String TAG = SpeakerActivity.class.getSimpleName();
    private static final String ARG_SPEAKER_KEY = "speaker-key";
    private static final int LOADER_ID = 0;
    private String speakerKey;
    private HeaderController headerController;
    private InfoCardController infoCardController;

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.speaker_header)
    View headerView;

    @Bind(R.id.info_card)
    View infoCard;

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

        headerController = new HeaderController(headerView);
        infoCardController = new InfoCardController(infoCard);

        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        speakerKey = getIntent().getStringExtra(ARG_SPEAKER_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
    public Loader<SpeakerViewDto> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SpeakerLoader.getSpeakerLoader(this, speakerKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<SpeakerViewDto> loader, SpeakerViewDto speakerViewDto) {
        if (loader.getId() == LOADER_ID) {
            if (speakerViewDto == null) {
                Log.w(TAG, "Speaker data is null for speaker-key: " + speakerKey);
                return;
            }
            headerController.bind(speakerViewDto);
            infoCardController.bind(speakerViewDto);
        }
    }

    @Override
    public void onLoaderReset(Loader<SpeakerViewDto> loader) {
    }

    protected class HeaderController {

        private static final String TWITTER_WWW = "https://twitter.com/";
        private static final String ASSETS_SPEAKERS_IMAGES = "file:///android_asset/speakers-images/";

        @Bind(R.id.speaker_avatar)
        CircleImageView avatarView;

        @Bind(R.id.text_speaker_name)
        TextView speakerNameView;

        @Bind(R.id.www_button)
        ImageButton wwwButton;

        @Bind(R.id.twitter_button)
        ImageButton twitterButton;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(SpeakerViewDto speakerViewDto) {
            speakerNameView.setText(getSpeakersFullName(speakerViewDto));
            loadAvatar(speakerViewDto.photo);
        }

        private void loadAvatar(String photoFileName) {
            Picasso.with(getApplicationContext()).load(ASSETS_SPEAKERS_IMAGES + photoFileName)
                    .placeholder(R.drawable.dummy_avatar).into(avatarView);
        }

        @NonNull
        private String getSpeakersFullName(SpeakerViewDto speakerViewDto) {
            return new StringBuilder(speakerViewDto.firstName).append(" ").append(speakerViewDto.lastName).toString();
        }

        private void goToWebPage(@NonNull String url) {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (!url.startsWith("http://") || !url.startsWith("https://")) {
                urlBuilder.insert(0, "http://");
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urlBuilder.toString()));
            startActivity(i);
        }

        private void gotToTwitterProfile(@NonNull String twitterName) {
            goToWebPage(TWITTER_WWW + twitterName);
        }
    }

    protected class InfoCardController {

        @Bind(R.id.text_info)
        TextView speakerInfoView;

        public InfoCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(SpeakerViewDto speakerViewDto) {
            speakerInfoView.setText(speakerViewDto.biography);
        }
    }
}
