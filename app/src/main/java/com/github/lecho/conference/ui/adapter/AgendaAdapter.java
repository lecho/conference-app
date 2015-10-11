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

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.BaseViewHolder> {

    public static final int ITEM_TYPE_BREAK = 0;
    public static final int ITEM_TYPE_TALK = 1;
    public static final int ITEM_TYPE_EMPTY_SLOT = 2;
    protected List<AgendaItemViewDto> data = new ArrayList<>();
    protected AppCompatActivity activity;
    protected View.OnClickListener emptySlotListener;

    public AgendaAdapter(AppCompatActivity activity, View.OnClickListener emptySlotListener) {
        this.activity = activity;
        this.emptySlotListener = emptySlotListener;
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
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(activity, view);
        } else if (ITEM_TYPE_TALK == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_talk, parent, false);
            viewHolder = new AgendaTalkViewHolder(activity, view);
        } else if (ITEM_TYPE_EMPTY_SLOT == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_empty_slot, parent, false);
            viewHolder = new EmptySlotViewHolder(activity, view);
        } else {
            viewHolder = new BaseViewHolder(new View(activity));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (AgendaItemViewDto.AgendaItemType.BREAK == data.get(position).type) {
            return ITEM_TYPE_BREAK;
        } else if (AgendaItemViewDto.AgendaItemType.TALK == data.get(position).type) {
            return ITEM_TYPE_TALK;
        } else if (AgendaItemViewDto.AgendaItemType.SLOT == data.get(position).type) {
            return ITEM_TYPE_EMPTY_SLOT;
        } else {
            throw new IllegalArgumentException("Invalid item type " + data.get(position).type);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
        }
    }

    protected abstract class AgendaViewHolder extends BaseViewHolder {

        protected final AppCompatActivity activity;

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Bind(R.id.current_item_indicator)
        ImageView currentItemIndicatorView;

        @Override
        public abstract void bindView(AgendaItemViewDto agendaItem);

        public AgendaViewHolder(AppCompatActivity activity, View itemView) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

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

    protected class EmptySlotViewHolder extends AgendaViewHolder {

        public EmptySlotViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewDto agendaItem) {
            bindSlot(agendaItem.slot);
            itemView.setOnClickListener(emptySlotListener);
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
            bindSlot(agendaItem.slot);
            titleView.setText(agendaItem.agendaBreak.title);
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
            bindSlot(agendaItem.slot);
            TalkViewDto talkViewDto = agendaItem.talk;
            itemView.setOnClickListener(new TalkItemClickListener(activity, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            venueView.setText(talkViewDto.venue.getVenueText(activity));
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
        }
    }

    protected static class TalkItemClickListener implements View.OnClickListener {

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
