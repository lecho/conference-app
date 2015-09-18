package com.github.lecho.conference.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.BreakViewDto;
import com.github.lecho.conference.viewmodel.SlotViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyAgendaAdapter extends RecyclerView.Adapter<MyAgendaAdapter.AgendaViewHolder> {

    public static final int ITEM_TYPE_BREAK = 0;
    public static final int ITEM_TYPE_TALK = 1;
    private List<AgendaItemViewDto> data;

    public MyAgendaAdapter(List<AgendaItemViewDto> data) {
        this.data = data;
    }

    @Override
    public MyAgendaAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (ITEM_TYPE_BREAK == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_break, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_agenda, parent, false);
        }
        AgendaViewHolder viewHolder = new AgendaViewHolder(view, viewType);
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
        } else if (AgendaItemViewDto.AgendaItemType.BREAK == data.get(position).type) {
            return ITEM_TYPE_TALK;
        } else {
            throw new IllegalArgumentException("Invalid item type " + data.get(position).type);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AgendaViewHolder extends RecyclerView.ViewHolder {

        private View.OnClickListener agendaItemClickListener;
        private int viewType;

        @Bind(R.id.text_time_slot)
        TextView timeSlotView;

        @Nullable
        @Bind(R.id.text_venue)
        TextView venueView;

        @Bind(R.id.text_title)
        TextView titleView;

        @Nullable
        @Bind(R.id.text_language)
        TextView languageView;

        @Nullable
        @Bind(R.id.text_speakers)
        TextView speakersView;

        public AgendaViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(agendaItemClickListener);
        }

        public void bindView(AgendaItemViewDto agendaItem) {
            SlotViewDto slotViewDto;
            String titleText;
            if (ITEM_TYPE_BREAK == viewType) {
                BreakViewDto breakViewDto = agendaItem.agendaBreak;
                slotViewDto = breakViewDto.slot;
                titleText = breakViewDto.title;
            } else {
                TalkViewDto talkViewDto = agendaItem.talk;
                slotViewDto = talkViewDto.slot;
                titleText = talkViewDto.title;
            }

            String timeSlotText = new StringBuilder(slotViewDto.from).append(" ").append(slotViewDto.to).toString();
            timeSlotView.setText(timeSlotText);
            titleView.setText(titleText);
        }
    }

}
