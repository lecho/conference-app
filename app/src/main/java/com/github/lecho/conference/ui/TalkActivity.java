package com.github.lecho.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.loader.TalkLoader;
import com.github.lecho.conference.realmmodel.RealmFacade;
import com.github.lecho.conference.viewmodel.SlotViewDto;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<TalkViewDto> {

    private static final String TAG = TalkActivity.class.getSimpleName();
    private static final String ARG_TALK_KEY = "talk-key";
    private static final int LOADER_ID = 0;
    private String talkKey;
    private FABController fabController;
    private HeaderController headerController;
    private InfoCardController infoCardController;
    private SpeakersCardController speakersCardController;

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.talk_header)
    View headerView;

    @Bind(R.id.info_card)
    View infoCard;

    @Bind(R.id.speakers_card)
    View speakersCard;

    @Bind(R.id.button_add_to_my_agenda)
    FloatingActionButton addToMyAgendaButton;

    public static void startActivity(@NonNull Context context, @NonNull String talkKey) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(ARG_TALK_KEY, talkKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);

        fabController = new FABController(addToMyAgendaButton);
        headerController = new HeaderController(headerView);
        infoCardController = new InfoCardController(infoCard);
        speakersCardController = new SpeakersCardController(speakersCard);

        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        talkKey = getIntent().getStringExtra(ARG_TALK_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<TalkViewDto> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return TalkLoader.getTalkLoader(this, talkKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<TalkViewDto> loader, TalkViewDto talkViewDto) {
        if (loader.getId() == LOADER_ID) {
            if (talkViewDto == null) {
                Log.w(TAG, "Talk data is null for talk-key: " + talkKey);
                return;
            }
            fabController.bind(talkViewDto);
            headerController.bind(talkViewDto);
            infoCardController.bind(talkViewDto);
            speakersCardController.bind(talkViewDto);

        }
    }

    @Override
    public void onLoaderReset(Loader<TalkViewDto> loader) {
    }

    protected class FABController {

        FloatingActionButton addToMyAgendaButton;

        public FABController(FloatingActionButton addToMyAgendaButton) {
            this.addToMyAgendaButton = addToMyAgendaButton;
        }

        public void bind(TalkViewDto talkViewDto) {
            if (talkViewDto.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_clear_big);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_add_big);
            }
            addToMyAgendaButton.setOnClickListener(new AddToMyAgendaClickListener(talkViewDto));
            addToMyAgendaButton.show();
        }
    }

    protected class HeaderController {

        @Bind(R.id.text_talk_title)
        TextView talkTitleView;

        @Bind(R.id.text_time_slot)
        TextView talkTimeSlotView;

        @Bind(R.id.text_venue)
        TextView talkVenueView;

        @Bind(R.id.text_language)
        TextView talkLanguageView;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewDto talkViewDto) {
            talkTitleView.setText(talkViewDto.title);
            talkTimeSlotView.setText(getTimeSlotText(talkViewDto.slot));
            talkVenueView.setText(talkViewDto.venue.title);
            talkLanguageView.setText(talkViewDto.language);
        }

        @NonNull
        private String getTimeSlotText(SlotViewDto slotViewDto) {
            return new StringBuilder(slotViewDto.from).append(" - ").append(slotViewDto.to).toString();
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
                SpeakerLayout speakerLayout = new SpeakerLayout(TalkActivity.this, speakerViewDto.key);
                speakerLayout.setSpeakerName(getSpeakerNameText(speakerViewDto));
                //TODO set speaker avatar
                speakersLayout.addView(speakerLayout);
            }
        }

        @NonNull
        private String getSpeakerNameText(SpeakerViewDto speakerViewDto) {
            return new StringBuilder(speakerViewDto.firstName).append(" ").append(speakerViewDto.lastName).toString();
        }
    }

    protected class AddToMyAgendaClickListener implements View.OnClickListener {

        private TalkViewDto talkViewDto;

        public AddToMyAgendaClickListener(TalkViewDto talkViewDto) {
            this.talkViewDto = talkViewDto;
        }

        @Override
        public void onClick(View v) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) v;
            RealmFacade realmFacade = new RealmFacade(getApplicationContext());
            if (talkViewDto.isInMyAgenda) {
                realmFacade.removeTalkFromMyAgenda(talkViewDto.key);
                floatingActionButton.setImageResource(R.drawable.ic_add_big);
                talkViewDto.isInMyAgenda = false;
            } else {
                realmFacade.addTalkToMyAgenda(talkViewDto.key);
                floatingActionButton.setImageResource(R.drawable.ic_clear_big);
                talkViewDto.isInMyAgenda = true;
            }
        }
    }
}
