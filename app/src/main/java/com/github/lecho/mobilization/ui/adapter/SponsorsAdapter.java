package com.github.lecho.mobilization.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SponsorsAdapter extends RecyclerView.Adapter<SponsorsAdapter.SponsorViewHolder> {

    private final Activity context;
    private List<SponsorViewModel> data = new ArrayList<>();

    public SponsorsAdapter(Activity context) {
        this.context = context;
    }

    public void setData(@NonNull List<SponsorViewModel> data) {
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

    public class SponsorViewHolder extends RecyclerView.ViewHolder {

        private final Context context;

        @BindView(R.id.sponsor_color)
        View sponsorColorView;

        @BindView(R.id.text_type)
        TextView typeView;

        @BindView(R.id.text_name)
        TextView nameView;

        @BindView(R.id.sponsor_logo)
        ImageView logoView;

        @BindView(R.id.text_web_page)
        TextView webPageView;

        public SponsorViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void bindView(SponsorViewModel sponsorViewModel) {
            sponsorColorView.setBackgroundResource(sponsorViewModel.type.getDrawable());
            typeView.setText(sponsorViewModel.type.getTextRes());
            nameView.setText(sponsorViewModel.name);
            webPageView.setText(sponsorViewModel.wwwPage);
            Utils.loadSponsorImage(context.getApplicationContext(), sponsorViewModel.logo, logoView);
            if (!TextUtils.isEmpty(sponsorViewModel.wwwPage)) {
                itemView.setOnClickListener(new SponsorItemClickListener(sponsorViewModel.wwwPage));
            }
        }
    }

    private class SponsorItemClickListener implements View.OnClickListener {

        private final String webPage;

        public SponsorItemClickListener(String webPage) {
            this.webPage = webPage;
        }

        @Override
        public void onClick(View v) {
            Utils.openWebBrowser(context.getApplicationContext(), webPage);
        }
    }

}
