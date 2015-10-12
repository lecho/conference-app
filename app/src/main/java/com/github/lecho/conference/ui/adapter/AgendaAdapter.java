package com.github.lecho.conference.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.ui.TalkActivity;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
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
    private List<AgendaItemViewDto> data = new ArrayList<>();
    private final AppCompatActivity activity;
    private final AgendaItemClickListener starTalkListener;
    private final AgendaItemClickListener emptySlotListener;

    public AgendaAdapter(AppCompatActivity activity, AgendaItemClickListener starTalkListener,
                         AgendaItemClickListener emptySlotListener) {
        this.activity = activity;
        this.starTalkListener = starTalkListener;
        this.emptySlotListener = emptySlotListener;
    }

    public void setData(@NonNull List<AgendaItemViewDto> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public AgendaItemViewDto getItem(int position) {
        return data.get(position);
    }

    /**
     * Visually remove talk item and replace it with empty slot item. This method doesn't modify data in database.
     *
     * @param position
     */
    public void removeTalk(int position) {
        notifyItemRemoved(position);
        AgendaItemViewDto item = data.get(position);
        item.type = AgendaItemViewDto.AgendaItemType.SLOT;
        notifyItemInserted(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(activity, view);
        } else if (ITEM_TYPE_TALK == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_talk, parent, false);
            viewHolder = new MyAgendaTalkViewHolder(activity, view);
        } else if (ITEM_TYPE_EMPTY_SLOT == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_empty_slot, parent, false);
            viewHolder = new EmptySlotViewHolder(activity, view);
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

    /**
     * Base agenda item view holder
     */
    protected abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        protected final AppCompatActivity activity;

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Bind(R.id.current_item_indicator)
        ImageView currentItemIndicatorView;

        @Bind(R.id.text_title)
        TextView titleView;

        public BaseViewHolder(AppCompatActivity activity, View itemView) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            bindSlot(agendaItem.slot);
        }

        private void bindSlot(SlotViewDto slotViewDto) {
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

    /**
     * View holder for empty slot item
     */
    protected class EmptySlotViewHolder extends BaseViewHolder {

        public EmptySlotViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewDto agendaItem) {
            super.bindView(agendaItem);
            itemView.setOnClickListener(new EmptySlotClickListener(getLayoutPosition(), agendaItem));
        }
    }

    /**
     * View holder for coffee break item
     */
    protected class BreakViewHolder extends BaseViewHolder {

        public BreakViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewDto agendaItem) {
            super.bindView(agendaItem);
            titleView.setText(agendaItem.agendaBreak.title);
        }
    }

    /**
     * Specific venue/track talk view holder
     */
    protected class VenueAgendaTalkViewHolder extends BaseViewHolder {

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
            super.bindView(agendaItem);
            TalkViewDto talkViewDto = agendaItem.talk;
            itemView.setOnClickListener(new TalkItemClickListener(activity, talkViewDto.key));
            titleView.setText(talkViewDto.title);
            languageView.setText(talkViewDto.language);
            speakersView.setText(getSpeakersText(talkViewDto));
            if (talkViewDto.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_accent);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_accent);
            }
            addToMyAgendaButton.setOnClickListener(new StartTalkClickListener(getLayoutPosition(), agendaItem));
            addToMyAgendaButtonLayout.setOnClickListener(new StarLayoutListener(addToMyAgendaButton));
        }
    }

    protected class MyAgendaTalkViewHolder extends VenueAgendaTalkViewHolder {

        @Bind(R.id.text_venue)
        TextView venueView;

        public MyAgendaTalkViewHolder(AppCompatActivity activity, View itemView) {
            super(activity, itemView);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            super.bindView(agendaItem);
            TalkViewDto talkViewDto = agendaItem.talk;
            venueView.setText(talkViewDto.venue.getVenueText(activity));
        }
    }

    /**
     * Listener for talk item click
     */
    private static class TalkItemClickListener implements View.OnClickListener {

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

    /**
     * Listener for star/unstar talk button
     */
    private class StartTalkClickListener implements View.OnClickListener {

        private final AgendaItemViewDto agendaItem;
        private final int position;

        public StartTalkClickListener(int position, AgendaItemViewDto agendaItem) {
            this.position = position;
            this.agendaItem = agendaItem;
        }

        @Override
        public void onClick(View v) {
            if (starTalkListener != null) {
                starTalkListener.onItemClick(position, agendaItem, v);
            }
        }
    }

    private class StarLayoutListener implements View.OnClickListener {

        private View view;

        public StarLayoutListener(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            view.performClick();
        }
    }

    /**
     * Empty slot click listener
     */
    private class EmptySlotClickListener implements View.OnClickListener {

        private final AgendaItemViewDto agendaItem;
        private final int position;

        public EmptySlotClickListener(int position, AgendaItemViewDto agendaItem) {
            this.position = position;
            this.agendaItem = agendaItem;
        }

        @Override
        public void onClick(View v) {
            if (emptySlotListener != null) {
                emptySlotListener.onItemClick(position, agendaItem, v);
            }
        }
    }


    public interface AgendaItemClickListener {
        void onItemClick(int position, AgendaItemViewDto agendaItem, View view);
    }
}
