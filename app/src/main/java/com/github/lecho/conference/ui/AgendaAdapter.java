package com.github.lecho.conference.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.BreakViewDto;
import com.github.lecho.conference.viewmodel.SlotViewDto;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder> {

    public static final int ITEM_TYPE_BREAK = 0;
    public static final int ITEM_TYPE_TALK = 1;
    protected List<AgendaItemViewDto> data = new ArrayList<>();
    protected Context context;

    public AgendaAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AgendaItemViewDto> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public AgendaItemViewDto getItem(int position) {
        return data.get(position);
    }

    public void removeItemFromAdapter(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AgendaViewHolder viewHolder;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(context, view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_talk, parent, false);
            viewHolder = new AgendaTalkViewHolder(context, view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (AgendaItemViewDto.AgendaItemType.BREAK == data.get(position).type) {
            return ITEM_TYPE_BREAK;
        } else if (AgendaItemViewDto.AgendaItemType.TALK == data.get(position).type) {
            return ITEM_TYPE_TALK;
        } else {
            throw new IllegalArgumentException("Invalid item type " + data.get(position).type);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected abstract class AgendaViewHolder extends RecyclerView.ViewHolder {

        protected final Context context;

        public AgendaViewHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        public abstract void bindView(AgendaItemViewDto agendaItem);

        @NonNull
        protected String getTimeSlotText(SlotViewDto slotViewDto) {
            return new StringBuilder(slotViewDto.from).append(" - ").append(slotViewDto.to).toString();
        }

        @NonNull
        protected String getSpeakersText(TalkViewDto talkViewDto) {
            StringBuilder speakersText = new StringBuilder();
            for (SpeakerViewDto speakerViewDto : talkViewDto.speakers) {
                if (!TextUtils.isEmpty(speakersText)) {
                    speakersText.append("\n");
                }
                speakersText.append(speakerViewDto.firstName).append(" ").append(speakerViewDto.lastName);
            }
            return speakersText.toString();
        }
    }

    protected class BreakViewHolder extends AgendaViewHolder {

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Bind(R.id.text_title)
        TextView titleView;

        public BreakViewHolder(Context context, View itemView) {
            super(context, itemView);
        }

        @Override
        public void bindView(AgendaItemViewDto agendaItem) {
            BreakViewDto breakViewDto = agendaItem.agendaBreak;
            timeSlotView.setText(getTimeSlotText(breakViewDto.slot));
            titleView.setText(breakViewDto.title);
        }
    }

    protected class AgendaTalkViewHolder extends AgendaViewHolder {

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Bind(R.id.text_venue)
        TextView venueView;

        @Bind(R.id.text_title)
        TextView titleView;

        @Bind(R.id.text_language)
        TextView languageView;

        @Bind(R.id.text_speakers)
        TextView speakersView;

        public AgendaTalkViewHolder(Context context, View itemView) {
            super(context, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            TalkViewDto talkViewDto = agendaItem.talk;
            itemView.setOnClickListener(new TalkItemClickListener(context, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            venueView.setText(talkViewDto.venue.title);
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
            timeSlotView.setText(getTimeSlotText(talkViewDto.slot));
        }
    }

    protected class TalkItemClickListener implements View.OnClickListener {

        private final String talkKey;
        private final Context context;

        public TalkItemClickListener(Context context, String talkKey) {
            this.talkKey = talkKey;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            TalkActivity.startActivity(context, talkKey);
        }
    }
}
