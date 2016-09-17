package com.github.lecho.mobilization.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.ui.SameSlotActivity;
import com.github.lecho.mobilization.ui.TalkActivity;
import com.github.lecho.mobilization.util.AnalyticsReporter;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.SlotViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO Consider extracting slot and talk listeners outside this adapter
public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.BaseViewHolder> {

    public static final int ITEM_TYPE_BREAK = 0;
    public static final int ITEM_TYPE_TALK = 1;
    public static final int ITEM_TYPE_EMPTY_SLOT = 2;
    protected List<AgendaItemViewModel> data = new ArrayList<>();
    protected final Activity activity;
    protected final AgendaItemClickListener starTalkListener;
    protected final FirebaseAnalytics firebaseAnalytics;

    public AgendaAdapter(Activity activity, AgendaItemClickListener starTalkListener) {
        this.activity = activity;
        this.starTalkListener = starTalkListener;
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(activity.getApplicationContext());
    }

    public void setData(@NonNull List<AgendaItemViewModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public AgendaItemViewModel getItem(int position) {
        return data.get(position);
    }

    /**
     * Visually remove talk item and replace it with empty slot item. This method doesn't modify data in database.
     *
     * @param position talk position in adapter
     */
    public void removeTalk(int position) {
        AgendaItemViewModel item = data.get(position);
        item.type = AgendaItemViewModel.AgendaItemType.SLOT;
        item.talk = null;
        notifyItemChanged(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        if (ITEM_TYPE_BREAK == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_break, parent, false);
            viewHolder = new BreakViewHolder(activity, view);
        } else if (ITEM_TYPE_TALK == viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_agenda_talk, parent, false);
            viewHolder = new TalkViewHolder(activity, view);
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
        if (AgendaItemViewModel.AgendaItemType.BREAK == data.get(position).type) {
            return ITEM_TYPE_BREAK;
        } else if (AgendaItemViewModel.AgendaItemType.TALK == data.get(position).type) {
            return ITEM_TYPE_TALK;
        } else if (AgendaItemViewModel.AgendaItemType.SLOT == data.get(position).type) {
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

        protected final Activity activity;

        @BindView(R.id.text_time_slot)
        TextView timeSlotView;

        @BindView(R.id.current_item_indicator)
        ImageView currentItemIndicatorView;

        @BindView(R.id.text_title)
        TextView titleView;

        public BaseViewHolder(Activity activity, View itemView) {
            super(itemView);
            this.activity = activity;
            ButterKnife.bind(this, itemView);
        }

        public void bindView(AgendaItemViewModel agendaItem) {
            bindSlot(agendaItem.slot);
        }

        private void bindSlot(SlotViewModel slotViewModel) {
            SlotViewModel.SlotInTimeZone slotInTimeZone = SlotViewModel.SlotInTimeZone.getSlotInTimezone(slotViewModel);
            timeSlotView.setText(slotInTimeZone.getTimeSlotText());
            if (slotInTimeZone.isInCurrentSlot()) {
                currentItemIndicatorView.setVisibility(View.VISIBLE);
            } else {
                currentItemIndicatorView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * View holder for empty slot item
     */
    protected class EmptySlotViewHolder extends BaseViewHolder {

        public EmptySlotViewHolder(Activity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewModel agendaItem) {
            super.bindView(agendaItem);
            itemView.setOnClickListener(new EmptySlotClickListener(activity, agendaItem.slot.key, firebaseAnalytics));
        }
    }

    /**
     * View holder for coffee break item
     */
    protected class BreakViewHolder extends BaseViewHolder {

        public BreakViewHolder(Activity activity, View itemView) {
            super(activity, itemView);
        }

        @Override
        public void bindView(AgendaItemViewModel agendaItem) {
            super.bindView(agendaItem);
            titleView.setText(agendaItem.agendaBreak.title);
        }
    }

    /**
     * Specific venue/track talk view holder
     */
    protected class TalkViewHolder extends BaseViewHolder {

        @BindView(R.id.fab)
        ImageButton addToMyAgendaButton;

        @BindView(R.id.text_title)
        TextView titleView;

        @BindView(R.id.text_language)
        TextView languageView;

        @BindView(R.id.text_speaker)
        TextView speakersView;

        public TalkViewHolder(Activity activity, View itemView) {
            super(activity, itemView);
        }

        public void bindView(AgendaItemViewModel agendaItem) {
            super.bindView(agendaItem);
            TalkViewModel talkViewModel = agendaItem.talk;
            itemView.setOnClickListener(new TalkItemClickListener(activity, talkViewModel.key, firebaseAnalytics));
            titleView.setText(talkViewModel.title);
            languageView.setText(talkViewModel.getLanguageInBrackets());
            speakersView.setText(talkViewModel.getSpeakersText(activity));
            if (talkViewModel.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_32);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_32);
            }
            addToMyAgendaButton.setOnClickListener(new StarTalkClickListener(getLayoutPosition(), agendaItem));
        }
    }

    /**
     * Listener for talk item click
     */
    protected static class TalkItemClickListener implements View.OnClickListener {

        private final String talkKey;
        private final Activity activity;
        private final FirebaseAnalytics firebaseAnalytics;

        public TalkItemClickListener(Activity activity, String talkKey, FirebaseAnalytics firebaseAnalytics) {
            this.talkKey = talkKey;
            this.activity = activity;
            this.firebaseAnalytics = firebaseAnalytics;
        }

        @Override
        public void onClick(View v) {
            TalkActivity.startActivity(activity, talkKey);
            AnalyticsReporter.logTalkSelected(firebaseAnalytics, talkKey);
        }
    }

    /**
     * Listener for star/unstar talk button
     */
    protected class StarTalkClickListener implements View.OnClickListener {

        private final AgendaItemViewModel agendaItem;
        private final int position;

        public StarTalkClickListener(int position, AgendaItemViewModel agendaItem) {
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

    /**
     * Empty slot click listener
     */
    public static class EmptySlotClickListener implements View.OnClickListener {

        private final Activity activity;
        private final String slotKey;
        private final FirebaseAnalytics firebaseAnalytics;

        public EmptySlotClickListener(Activity activity, String slotKey, FirebaseAnalytics firebaseAnalytics) {
            this.slotKey = slotKey;
            this.activity = activity;
            this.firebaseAnalytics = firebaseAnalytics;
        }

        @Override
        public void onClick(View v) {
            SameSlotActivity.startActivity(activity, slotKey);
            AnalyticsReporter.logEmptySlotSelected(firebaseAnalytics, slotKey);
        }
    }

    public interface AgendaItemClickListener {
        void onItemClick(int position, AgendaItemViewModel agendaItem, View view);
    }
}
