package com.github.lecho.mobilization.ui.adapter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import butterknife.BindView;

public class MyAgendaAdapter extends AgendaAdapter {

    public MyAgendaAdapter(AppCompatActivity activity, AgendaItemClickListener starTalkListener) {
        super(activity, starTalkListener);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(activity, view);
        } else if (ITEM_TYPE_TALK == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_my_agenda_talk, parent, false);
            viewHolder = new MyAgendaTalkViewHolder(activity, view);
        } else if (ITEM_TYPE_EMPTY_SLOT == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_empty_slot, parent, false);
            viewHolder = new EmptySlotViewHolder(activity, view);
        }
        return viewHolder;
    }

    protected class MyAgendaTalkViewHolder extends TalkViewHolder {

        @BindView(R.id.text_venue)
        TextView venueView;

        public MyAgendaTalkViewHolder(Activity activity, View itemView) {
            super(activity, itemView);
        }

        public void bindView(AgendaItemViewModel agendaItem) {
            super.bindView(agendaItem);
            TalkViewModel talkViewModel = agendaItem.talk;
            venueView.setText(talkViewModel.venue.getVenueText(activity));
        }
    }
}
