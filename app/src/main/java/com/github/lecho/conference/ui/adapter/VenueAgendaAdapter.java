package com.github.lecho.conference.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.async.TalkFavoriteTask;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;

import butterknife.Bind;

public class VenueAgendaAdapter extends AgendaAdapter {

    public VenueAgendaAdapter(Context context) {
        super(context);
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AgendaViewHolder viewHolder;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(context, view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venue_agenda_talk, parent,
                    false);
            viewHolder = new VenueAgendaTalkViewHolder(context, view);
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

        public VenueAgendaTalkViewHolder(Context context, View itemView) {
            super(context, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            TalkViewDto talkViewDto = agendaItem.talk;
            bindSlot(talkViewDto.slot);
            itemView.setOnClickListener(new TalkItemClickListener(context, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
            if (talkViewDto.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_accent);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_accent);
            }
            addToMyAgendaButton.setOnClickListener(new AddToMyAgendaClickListener(context, talkViewDto));
            addToMyAgendaButtonLayout.setOnClickListener(new AddToMyAgendaLayoutListener(addToMyAgendaButton));
        }
    }

    protected class AddToMyAgendaClickListener implements View.OnClickListener {

        private TalkViewDto talkViewDto;
        private Context context;


        public AddToMyAgendaClickListener(Context context, TalkViewDto talkViewDto) {
            this.context = context;
            this.talkViewDto = talkViewDto;
        }

        @Override
        public void onClick(View v) {
            if (talkViewDto.isInMyAgenda) {
                talkViewDto.isInMyAgenda = false;
                TalkFavoriteTask.removeFromMyAgenda(context.getApplicationContext(), talkViewDto.key, false);
            } else {
                talkViewDto.isInMyAgenda = true;
                TalkFavoriteTask.addToMyAgenda(context.getApplicationContext(), talkViewDto.key, false);
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