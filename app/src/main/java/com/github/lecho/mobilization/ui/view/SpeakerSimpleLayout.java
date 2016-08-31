package com.github.lecho.mobilization.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
public class SpeakerSimpleLayout extends FrameLayout {

    private final SpeakerViewModel speakerViewModel;

    @BindView(R.id.image_avatar)
    ImageView avatarView;

    @BindView(R.id.text_speaker_name)
    TextView speakerNameView;

    @OnClick
    public void onClick() {
        SpeakerActivity.startActivity(getActivity(), speakerViewModel.key);
    }

    public SpeakerSimpleLayout(Context context, SpeakerViewModel speakerViewModel) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.include_speaker, this, true);
        ButterKnife.bind(this, this);
        this.speakerViewModel = speakerViewModel;
    }

    public void bind() {
        Utils.loadSpeakerImageSmall(getContext().getApplicationContext(), speakerViewModel.photo, avatarView);
        speakerNameView.setText(speakerViewModel.getSpeakerNameText());
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
