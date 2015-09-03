package com.github.lecho.conference.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.viewmodel.TalkViewDto;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class TalkLoader extends BaseRealmLoader<TalkViewDto> {

    private static final String TAG = TalkLoader.class.getSimpleName();
    private String talkKey;

    public static TalkLoader getTalkLoader(Context context, String talkKey) {
        return new TalkLoader(context, talkKey);
    }

    private TalkLoader(Context context, String talkKey) {
        super(context);
        this.talkKey = talkKey;
    }

    @Override
    public TalkViewDto loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading venues data");
        }
        TalkViewDto newData = realmFacade.loadTalkByKey(talkKey);
        return newData;
    }
}