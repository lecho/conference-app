package com.github.lecho.mobilization.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.loader.SpeakerLoader;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakerActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Optional<SpeakerViewModel>> {

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

    public static void startActivity(Activity activity, String speakerKey) {
        Intent intent = new Intent(activity, SpeakerActivity.class);
        intent.putExtra(ARG_SPEAKER_KEY, speakerKey);
        activity.startActivity(intent);
    }

    public static void startActivityWithTransition(Activity activity, String speakerKey, View avatarView) {
        Intent intent = new Intent(activity, SpeakerActivity.class);
        intent.putExtra(ARG_SPEAKER_KEY, speakerKey);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, avatarView, activity.getString(R.string.speaker_avatar));
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        ButterKnife.bind(this);

        headerController = new HeaderController(mainContainerView);
        infoCardController = new InfoCardController(mainContainerView);

        setSupportActionBar(toolbarView);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        speakerKey = getIntent().getStringExtra(ARG_SPEAKER_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Optional<SpeakerViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return SpeakerLoader.getLoader(this, speakerKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<SpeakerViewModel>> loader, Optional<SpeakerViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            if (!data.isPresent()) {
                Log.w(TAG, "Speaker data is null for speaker-key: " + speakerKey);
                return;
            }
            SpeakerViewModel speakerViewModel = data.get();
            headerController.bind(speakerViewModel);
            infoCardController.bind(speakerViewModel);
        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<SpeakerViewModel>> loader) {
    }

    protected class HeaderController {

        @BindView(R.id.image_avatar)
        ImageView avatarView;

        @BindView(R.id.text_speaker_name)
        TextView speakerNameView;

        @BindView(R.id.button_twitter)
        Button twitterButton;

        @BindView(R.id.button_github)
        Button githubButton;

        @BindView(R.id.button_website)
        Button websiteButton;


        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final SpeakerViewModel speakerViewModel) {
            speakerNameView.setText(speakerViewModel.getSpeakerNameText());
            Utils.loadSpeakerImageBig(getApplicationContext(), speakerViewModel.photo, avatarView);
            setUpWebsiteButton(speakerViewModel);
            setUpTwitterButton(speakerViewModel);
            setUpGithubButton(speakerViewModel);
        }

        private void setUpTwitterButton(final SpeakerViewModel speakerViewModel) {
            final String twitterUrl = speakerViewModel.twitter;
            if (TextUtils.isEmpty(twitterUrl)) {
                twitterButton.setVisibility(View.GONE);
            } else {
                final String twitterUser = getUserLoginFromUrl(twitterUrl, getString(R.string.twitter));
                twitterButton.setText(twitterUser);
                twitterButton.setVisibility(View.VISIBLE);
                twitterButton.setOnClickListener(v -> Utils.openTwitter(getApplicationContext(), twitterUrl));
            }
        }

        private void setUpGithubButton(final SpeakerViewModel speakerViewModel) {
            final String githubUrl = speakerViewModel.github;
            if (TextUtils.isEmpty(githubUrl)) {
                githubButton.setVisibility(View.GONE);
            } else {
                final String githubUser = getUserLoginFromUrl(githubUrl, getString(R.string.github));
                twitterButton.setText(githubUser);
                githubButton.setVisibility(View.VISIBLE);
                githubButton.setOnClickListener(v -> Utils.openWebBrowser(getApplicationContext(), githubUrl));
            }
        }

        private void setUpWebsiteButton(final SpeakerViewModel speakerViewModel) {
            if (TextUtils.isEmpty(speakerViewModel.website)) {
                websiteButton.setVisibility(View.GONE);
            } else {
                websiteButton.setVisibility(View.VISIBLE);
                websiteButton.setOnClickListener(v -> Utils.openWebBrowser(getApplicationContext(), speakerViewModel
                        .website));
            }
        }

        @NonNull
        private String getUserLoginFromUrl(final String url, final String defaultValue) {
            Uri uri = Uri.parse(url);
            final String userLogin = uri.getLastPathSegment();
            if (TextUtils.isEmpty(userLogin)) {
                return defaultValue;
            }
            return userLogin;
        }
    }

    protected class InfoCardController {

        @BindView(R.id.text_description)
        TextView speakerDescriptionView;

        public InfoCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(SpeakerViewModel speakerViewModel) {
            final Spanned speakerDescription;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                speakerDescription = Html.fromHtml(speakerViewModel.biography, Html.FROM_HTML_MODE_COMPACT);
            } else {
                speakerDescription = Html.fromHtml(speakerViewModel.biography);
            }
            speakerDescriptionView.setText(speakerDescription);
        }
    }
}
