package com.github.lecho.conference.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.ui.SpeakerActivity;
import com.github.lecho.conference.util.Utils;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Displays speaker's name and avatar. Use this class only from code.
 */
public class SpeakerForTalkLayout extends LinearLayout {

    private final SpeakerViewDto speakerViewDto;

    @Bind(R.id.speaker_avatar)
    CircleImageView avatarView;

    @Bind(R.id.text_speaker_name)
    TextView speakerNameView;

    @OnClick
    public void onClick() {
        SpeakerActivity.startActivity(getContext(), speakerViewDto.key);
    }

    public SpeakerForTalkLayout(Context context, SpeakerViewDto speakerViewDto) {
        super(context);
        this.speakerViewDto = speakerViewDto;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.include_speaker, this, true);
        ButterKnife.bind(this, this);
    }

    public void bind() {
        Utils.loadSpeakerImage(getContext().getApplicationContext(), speakerViewDto.photo, avatarView);
        speakerNameView.setText(getSpeakerNameText(speakerViewDto));
    }

    @NonNull
    private String getSpeakerNameText(SpeakerViewDto speakerViewDto) {
        return new StringBuilder(speakerViewDto.firstName).append(" ").append(speakerViewDto.lastName).toString();
    }
}
