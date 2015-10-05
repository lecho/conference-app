package com.github.lecho.conference.ui.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.async.TalkAsyncHelper;
import com.github.lecho.conference.util.Utils;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;

import butterknife.Bind;

public class VenueAgendaAdapter extends AgendaAdapter {

    private static final String TAG = AddToMyAgendaClickListener.class.getSimpleName();

    public VenueAgendaAdapter(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AgendaViewHolder viewHolder;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(activity, view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venue_agenda_talk, parent,
                    false);
            viewHolder = new VenueAgendaTalkViewHolder(activity, view);
        }
        return viewHolder;
    }

    protected class VenueAgendaTalkViewHolder extends AgendaViewHolder {

        @Bind(R.id.button_add_to_my_agenda_layout)
        View addToMyAgendaButtonLayout;

        @Bind(R.id.button_add_to_my_agenda)
        ImageButton addToMyAgendaButton;

        @Bind(R.id.text_title)
        TextView titleView;

        @Bind(R.id.text_language)
        TextView languageView;

        @Bind(R.id.text_speakers)
        TextView speakersView;

        public VenueAgendaTalkViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            TalkViewDto talkViewDto = agendaItem.talk;
            bindSlot(talkViewDto.slot);
            itemView.setOnClickListener(new TalkItemClickListener(activity, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
            if (talkViewDto.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_accent);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_accent);
            }
            addToMyAgendaButton.setOnClickListener(new AddToMyAgendaClickListener(activity, talkViewDto));
            addToMyAgendaButtonLayout.setOnClickListener(new AddToMyAgendaLayoutListener(addToMyAgendaButton));
        }
    }

    protected class AddToMyAgendaClickListener implements View.OnClickListener {

        private TalkViewDto talkViewDto;
        private AppCompatActivity activity;


        public AddToMyAgendaClickListener(AppCompatActivity activity, TalkViewDto talkViewDto) {
            this.activity = activity;
            this.talkViewDto = talkViewDto;
        }

        @Override
        public void onClick(View v) {
            if (talkViewDto.isInMyAgenda) {
                talkViewDto.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(activity.getApplicationContext(), talkViewDto.key);
            } else {
                if (Utils.checkSlotConflict(activity, talkViewDto.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewDto.key);
                    return;
                }
                talkViewDto.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(activity.getApplicationContext(), talkViewDto.key);
            }
            notifyDataSetChanged();
        }
    }

    protected class AddToMyAgendaLayoutListener implements View.OnClickListener {

        private View view;

        public AddToMyAgendaLayoutListener(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            view.performClick();
        }
    }
}
