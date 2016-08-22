package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.rx.MyAgendaUpdatedEvent;
import com.github.lecho.mobilization.rx.RxBus;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import rx.Subscription;

/**
 * Created by Leszek on 2015-09-03.
 */
public class TalkLoader extends BaseRealmLoader<Optional<TalkViewModel>> {

    private static final String TAG = TalkLoader.class.getSimpleName();
    private String talkKey;
    private Subscription agendaUpdateSubscription;

    public static TalkLoader getLoader(Context context, String talkKey) {
        return new TalkLoader(context, talkKey);
    }

    private TalkLoader(Context context, String talkKey) {
        super(context);
        if (TextUtils.isEmpty(talkKey)) {
            throw new IllegalArgumentException("Talk key cannot be empty");
        }
        this.talkKey = talkKey;
    }

    @Override
    public Optional<TalkViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading talk data");
        }
        Optional<TalkViewModel> newData = realmFacade.loadTalkByKey(talkKey);
        return newData;
    }

    protected void registerObserver() {
        super.registerObserver();
        if (agendaUpdateSubscription == null) {
            agendaUpdateSubscription = RxBus.subscribe(MyAgendaUpdatedEvent.class, databaseUpdatedEvent ->
                    TalkLoader.this.onContentChanged());
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