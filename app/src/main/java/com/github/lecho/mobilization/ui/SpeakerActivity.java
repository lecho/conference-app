package com.github.lecho.mobilization.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
                finish();
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

        private static final String SPEAKER_HEADER_IMAGE = "speaker_header.jpg";

        @BindView(R.id.image_avatar)
        ImageView avatarView;

        @BindView(R.id.text_speaker_name)
        TextView speakerNameView;

//        @BindView(R.id.www_button)
//        ImageButton wwwButton;
//
//        @BindView(R.id.twitter_button)
//        ImageButton twitterButton;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final SpeakerViewModel speakerViewModel) {
            speakerNameView.setText(speakerViewModel.getSpeakerNameText());
            Utils.loadSpeakerImageBig(getApplicationContext(), speakerViewModel.photo, avatarView);
            //setUpWwwButton(speakerViewModel);
            //setUpTwitterButton(speakerViewModel);
        }

//        private void setUpWwwButton(final SpeakerViewModel speakerViewModel) {
//            if (TextUtils.isEmpty(speakerViewModel.wwwPage)) {
//                wwwButton.setEnabled(false);
//            } else {
//                wwwButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Utils.openWebBrowser(getApplicationContext(), speakerViewModel.wwwPage);
//                    }
//                });
//            }
//        }
//
//        private void setUpTwitterButton(final SpeakerViewModel speakerViewModel) {
//            if (TextUtils.isEmpty(speakerViewModel.twitterProfile)) {
//                twitterButton.setEnabled(false);
//            } else {
//                twitterButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Utils.openTwitter(getApplicationContext(), speakerViewModel.twitterProfile);
//                    }
//                });
//            }
//        }
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
