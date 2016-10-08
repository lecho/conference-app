package com.github.lecho.mobilization.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays speaker's name and avatar. Use this class only from code.
 */
public class SpeakerSmallLayout extends FrameLayout {

    private final SpeakerViewModel speakerViewModel;

    @BindView(R.id.image_avatar)
    ImageView avatarView;

    @BindView(R.id.text_speaker_name)
    TextView speakerNameView;

    public SpeakerSmallLayout(Context context, SpeakerViewModel speakerViewModel) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_speaker_small, this, true);
        ButterKnife.bind(this, this);
        this.speakerViewModel = speakerViewModel;
    }

    public void bind() {
        Utils.loadSpeakerImageSmall(getContext().getApplicationContext(), speakerViewModel.photo, avatarView);
        speakerNameView.setText(speakerViewModel.getSpeakerNameText());
    }
}
