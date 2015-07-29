package com.github.lecho.mobilization.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.AgendaViewHolder> {

    private String[] dataset;

    public SponsorsAdapter(String[] dataset) {
        this.dataset = dataset;
    }

    @Override
    public SponsorsAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sponsor, parent, false);
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

        @Bind(R.id.text_color)
        TextView color;

        @Bind(R.id.text_name)
        TextView name;

        @Bind(R.id.logo)
        ImageView logo;

        public AgendaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(String text) {
        }
    }

}
