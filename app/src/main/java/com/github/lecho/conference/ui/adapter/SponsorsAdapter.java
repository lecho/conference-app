package com.github.lecho.conference.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.conference.R;
import com.github.lecho.conference.util.Utils;
import com.github.lecho.conference.viewmodel.SponsorViewDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.SponsorViewHolder> {

    private final Context context;
    private List<SponsorViewDto> data = new ArrayList<>();

    public SponsorsAdapter(Context context) {
        this.context = context;
    }

    public void setData(@NonNull List<SponsorViewDto> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public SponsorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sponsor, parent, false);
        SponsorViewHolder viewHolder = new SponsorViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SponsorViewHolder holder, int position) {
        holder.bindView(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SponsorViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        @Bind(R.id.text_type)
        TextView typeView;

        @Bind(R.id.text_name)
        TextView nameView;

        @Bind(R.id.logo)
        ImageView logoView;

        public SponsorViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void bindView(SponsorViewDto sponsorViewDto) {
            nameView.setText(sponsorViewDto.name);
            Utils.loadSponsorImage(context.getApplicationContext(), sponsorViewDto.logo, logoView);
        }
    }

}
