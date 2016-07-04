package com.github.lecho.mobilization.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.SpeakerActivity;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays speaker's name and avatar. Use this class only from code.
 */
public class SpeakerForTalkLayout extends LinearLayout {

    private final SpeakerViewModel speakerViewModel;

    @BindView(R.id.speaker_avatar)
    ImageView avatarView;

    @BindView(R.id.text_speaker_name)
    TextView speakerNameView;

    @OnClick
    public void onClick() {
        SpeakerActivity.startActivity(getContext(), speakerViewModel.key);
    }

    public SpeakerForTalkLayout(Context context, SpeakerViewModel speakerViewModel) {
        super(context);
        this.speakerViewModel = speakerViewModel;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.speaker_for_talk_layout, this, true);
        ButterKnife.bind(this, this);
    }

    public void bind() {
        Utils.loadSpeakerImageSmall(getContext().getApplicationContext(), speakerViewModel.photo, avatarView);
        speakerNameView.setText(speakerViewModel.getSpeakerNameText());
    }
}
