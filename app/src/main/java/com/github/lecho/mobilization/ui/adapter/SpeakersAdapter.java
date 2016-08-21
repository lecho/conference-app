package com.github.lecho.mobilization.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.SpeakerActivity;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakersAdapter extends RecyclerView.Adapter<SpeakersAdapter.SpeakerViewHolder> {

    private final Activity activity;
    private List<SpeakerViewModel> data = new ArrayList<>();

    public SpeakersAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(@NonNull List<SpeakerViewModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public SpeakerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SpeakerViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker, parent, false);
        viewHolder = new SpeakerViewHolder(activity, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SpeakerViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class SpeakerViewHolder extends RecyclerView.ViewHolder {

        private final Activity activity;

        @BindView(R.id.image_avatar)
        ImageView avatarView;

        @BindView(R.id.text_speaker_name)
        TextView speakerNameView;

        public SpeakerViewHolder(Activity activity, View itemView) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

        public void bindView(SpeakerViewModel speakerViewModel) {
            speakerNameView.setText(speakerViewModel.getSpeakerNameText());
            itemView.setOnClickListener(new SpeakerItemClickListener(activity, speakerViewModel.key, avatarView));
            Utils.loadSpeakerImageMedium(activity.getApplicationContext(), speakerViewModel.photo, avatarView);
        }
    }

    protected class SpeakerItemClickListener implements View.OnClickListener {

        private final Activity activity;
        private final String speakerKey;
        private final View avatarView;

        public SpeakerItemClickListener(Activity activity, String speakerKey, View avatarView) {
            this.activity = activity;
            this.speakerKey = speakerKey;
            this.avatarView = avatarView;
        }

        @Override
        public void onClick(View v) {
            SpeakerActivity.startActivityWithTransition(activity, speakerKey, avatarView);
        }
    }
}
