package com.github.lecho.conference.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Displays speaker's name and avatar. Use this class only from code.
 */
public class SpeakerLayout extends LinearLayout {

    private static final String ASSETS_SPEAKERS_IMAGES = "file:///android_asset/speakers-images/";
    private final String speakerKey;

    @Bind(R.id.speaker_avatar)
    CircleImageView avatarView;

    @Bind(R.id.text_speaker_name)
    TextView speakerNameView;

    @OnClick
    public void onClick() {
        SpeakerActivity.startActivity(getContext(), speakerKey);
    }

    public SpeakerLayout(Context context, String speakerKey) {
        super(context);
        this.speakerKey = speakerKey;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.include_speaker, this, true);
        ButterKnife.bind(this, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
    }

    public void loadAvatar(String photoFileName) {
        Picasso.with(getContext()).load(ASSETS_SPEAKERS_IMAGES + photoFileName).placeholder(R.drawable.dummy_avatar)
                .into(avatarView);
    }

    public void setSpeakerName(String speakerName) {
        speakerNameView.setText(speakerName);
    }
}
