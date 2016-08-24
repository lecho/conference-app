package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.rx.MyAgendaUpdatedEvent;
import com.github.lecho.mobilization.rx.RxBus;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import java.util.List;

import rx.Subscription;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SameSlotLoader extends BaseRealmLoader<List<TalkViewModel>> {

    private static final String TAG = SameSlotLoader.class.getSimpleName();
    private String slotKey;
    private Subscription agendaUpdateSubscription;

    public static SameSlotLoader getLoader(Context context, String talkKey) {
        return new SameSlotLoader(context, talkKey);
    }

    private SameSlotLoader(Context context, String slotKey) {
        super(context);
        if (TextUtils.isEmpty(slotKey)) {
            throw new IllegalArgumentException("Slot key cannot be empty");
        }
        this.slotKey = slotKey;
    }

    @Override
    public List<TalkViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading talks by slot");
        }
        List<TalkViewModel> newData = realmFacade.loadTalksBySlotSortedByVenue(slotKey);
        return newData;
    }

    protected void registerObserver() {
        super.registerObserver();
        if (agendaUpdateSubscription == null) {
            agendaUpdateSubscription = RxBus.subscribe(MyAgendaUpdatedEvent.class, databaseUpdatedEvent ->
                    SameSlotLoader.this.onContentChanged());
        }
    }

    protected void unregisterObserver() {
        super.unregisterObserver();
        if (agendaUpdateSubscription != null) {
            agendaUpdateSubscription.unsubscribe();
            agendaUpdateSubscription = null;
        }
    }
}