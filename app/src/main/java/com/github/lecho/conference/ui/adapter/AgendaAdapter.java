package com.github.lecho.conference.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.ui.TalkActivity;
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
    protected AppCompatActivity activity;

    public AgendaAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setData(@NonNull List<AgendaItemViewDto> data) {
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
            viewHolder = new BreakViewHolder(activity, view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_talk, parent, false);
            viewHolder = new AgendaTalkViewHolder(activity, view);
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

        protected final AppCompatActivity activity;

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Bind(R.id.current_item_indicator)
        ImageView currentItemIndicatorView;

        public AgendaViewHolder(AppCompatActivity activity, View itemView) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

        public abstract void bindView(AgendaItemViewDto agendaItem);

        protected void bindSlot(SlotViewDto slotViewDto) {
            SlotViewDto.SlotInTimeZone slotInTimeZone = SlotViewDto.SlotInTimeZone.getSlotInTimezone(slotViewDto);
            timeSlotView.setText(slotInTimeZone.getTimeSlotText());
            if (slotInTimeZone.isInCurrentSlot()) {
                currentItemIndicatorView.setVisibility(View.VISIBLE);
            } else {
                currentItemIndicatorView.setVisibility(View.GONE);
            }
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

        @Bind(R.id.text_title)
        TextView titleView;

        public BreakViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewDto agendaItem) {
            BreakViewDto breakViewDto = agendaItem.agendaBreak;
            bindSlot(breakViewDto.slot);
            titleView.setText(breakViewDto.title);
        }
    }

    protected class AgendaTalkViewHolder extends AgendaViewHolder {

        @Bind(R.id.text_venue)
        TextView venueView;

        @Bind(R.id.text_title)
        TextView titleView;

        @Bind(R.id.text_language)
        TextView languageView;

        @Bind(R.id.text_speakers)
        TextView speakersView;

        public AgendaTalkViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            TalkViewDto talkViewDto = agendaItem.talk;
            bindSlot(talkViewDto.slot);
            itemView.setOnClickListener(new TalkItemClickListener(activity, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            venueView.setText(talkViewDto.venue.getVenueText(activity));
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
        }
    }

    protected class TalkItemClickListener implements View.OnClickListener {

        private final String talkKey;
        private final AppCompatActivity activity;

        public TalkItemClickListener(AppCompatActivity activity, String talkKey) {
            this.talkKey = talkKey;
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            TalkActivity.startActivity(activity, talkKey);
        }
    }
}
