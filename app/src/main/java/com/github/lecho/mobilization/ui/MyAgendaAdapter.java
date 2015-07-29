package com.github.lecho.mobilization.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lecho.mobilization.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyAgendaAdapter extends RecyclerView.Adapter<MyAgendaAdapter.AgendaViewHolder> {

    private List<String> dataset;

    public MyAgendaAdapter(List<String> dataset) {
        this.dataset = dataset;
    }

    @Override
    public MyAgendaAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_agenda, parent, false);
        AgendaViewHolder viewHolder = new AgendaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder holder, int position) {
        holder.bindView(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class AgendaViewHolder extends RecyclerView.ViewHolder {

        View.OnClickListener agendaItemClickListener;

        @Bind(R.id.text_time_slot)
        TextView timeSlot;

        @Bind(R.id.text_venue)
        TextView venue;

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
        }

        public void bindView(String text) {
        }
    }

}
