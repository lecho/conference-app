package com.github.lecho.conference.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class MyAgendaAdapter extends RecyclerView.Adapter<MyAgendaAdapter.MyAgendaViewHolder> {

    public static final int ITEM_TYPE_BREAK = 0;
    public static final int ITEM_TYPE_TALK = 1;
    private List<AgendaItemViewDto> data = new ArrayList<>();
    private Context context;

    public MyAgendaAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AgendaItemViewDto> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyAgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (ITEM_TYPE_BREAK == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_break, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_agenda, parent, false);
        }
        MyAgendaViewHolder viewHolder = new MyAgendaViewHolder(context, view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAgendaViewHolder holder, int position) {
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

    private static class TalkItemClickListener implements View.OnClickListener {

        private final String talkKey;
        private final Context context;

        public TalkItemClickListener(Context context, String talkKey){
            this.talkKey = talkKey;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            TalkActivity.startActivity(context, talkKey);
        }
    }

    public static class MyAgendaViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private final int viewType;

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

        public MyAgendaViewHolder(Context context, View itemView, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.viewType = viewType;
            this.context = context;
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
                itemView.setOnClickListener(new TalkItemClickListener(context, talkViewDto.key));
                slotViewDto = talkViewDto.slot;
                titleText = talkViewDto.title;
                venueView.setText(talkViewDto.venue.title);
                languageView.setText(talkViewDto.language);
                speakersView.setText(getSpeakersText(talkViewDto));
            }

            String timeSlotText = new StringBuilder(slotViewDto.from).append(" - ").append(slotViewDto.to).toString();
            timeSlotView.setText(timeSlotText);
            titleView.setText(titleText);
        }

        @NonNull
        private String getSpeakersText(TalkViewDto talkViewDto) {
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
}
