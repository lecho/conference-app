package com.github.lecho.mobilization;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VenueAgendaAdapter extends RecyclerView.Adapter<VenueAgendaAdapter.AgendaViewHolder> {

    private String[] dataset;

    public VenueAgendaAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public VenueAgendaAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venue_agenda, parent, false);
        AgendaViewHolder viewHolder = new AgendaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        holder.bindView(dataset[position]);
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }

    public static class AgendaViewHolder extends RecyclerView.ViewHolder {

        //TODO pass listeners in constructor
        View.OnClickListener agendaItemClickListener;
        View.OnClickListener addToMyAgendaClickListener;

        @Bind(R.id.button_add_to_my_agenda)
        FloatingActionButton addToMyAgenda;

        @Bind(R.id.button_add_to_my_agenda_layout)
        FrameLayout addToMyAgendaLayout;

        @Bind(R.id.text_time_slot)
        TextView timeSlot;

        @Bind(R.id.text_title)
        TextView title;

        @Bind(R.id.text_language)
        TextView language;

        @Bind(R.id.text_speakers)
        TextView speakers;

        public AgendaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(agendaItemClickListener);
            addToMyAgendaLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToMyAgenda.performClick();
                }
            });
            addToMyAgenda.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v instanceof FloatingActionButton) {
                        FloatingActionButton bt = (FloatingActionButton) v;
                        bt.setLineMorphingState((bt.getLineMorphingState() + 1) % 2, true);
                    }
                }
            });
        }

        public void bindView(String text) {
        }
    }

}
