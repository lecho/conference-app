package com.github.lecho.mobilization.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.SpeakerActivity;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewDto;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays speaker's name and avatar. Use this class only from code.
 */
public class SpeakerForTalkLayout extends LinearLayout {

    private final SpeakerViewDto speakerViewDto;

    @BindView(R.id.speaker_avatar)
    ImageView avatarView;

    @BindView(R.id.text_speaker_name)
    TextView speakerNameView;

    @OnClick
    public void onClick() {
        SpeakerActivity.startActivity(getContext(), speakerViewDto.key);
    }

    public SpeakerForTalkLayout(Context context, SpeakerViewDto speakerViewDto) {
        super(context);
        this.speakerViewDto = speakerViewDto;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.speaker_for_talk_layout, this, true);
        ButterKnife.bind(this, this);
    }

    public void bind() {
        Utils.loadSpeakerImageSmall(getContext().getApplicationContext(), speakerViewDto.photo, avatarView);
        speakerNameView.setText(speakerViewDto.getSpeakerNameText());
    }
}
