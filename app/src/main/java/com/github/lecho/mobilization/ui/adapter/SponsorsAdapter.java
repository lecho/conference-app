package com.github.lecho.mobilization.ui.adapter;

import android.content.Context;
import android.os.Build;
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
import com.github.lecho.mobilization.viewmodel.SponsorViewDto;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

        public void bindView(SponsorViewDto sponsorViewDto) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                sponsorColorView.setBackgroundColor(context.getResources().getColor(sponsorViewDto.type.getColorRes()));
            } else {
                sponsorColorView.setBackgroundColor(context.getColor(sponsorViewDto.type.getColorRes()));
            }
            typeView.setText(sponsorViewDto.type.getTextRes());
            nameView.setText(sponsorViewDto.name);
            webPageView.setText(sponsorViewDto.wwwPage);
            Utils.loadSponsorImage(context.getApplicationContext(), sponsorViewDto.logo, logoView);
            if (!TextUtils.isEmpty(sponsorViewDto.wwwPage)) {
                itemView.setOnClickListener(new SponsorItemClickListener(sponsorViewDto.wwwPage));
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
