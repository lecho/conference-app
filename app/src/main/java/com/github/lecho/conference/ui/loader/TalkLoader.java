package com.github.lecho.conference.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.util.Optional;
import com.github.lecho.conference.viewmodel.TalkViewDto;

/**
 * Created by Leszek on 2015-09-03.
 */
public class TalkLoader extends BaseRealmLoader<Optional<TalkViewDto>> {

    private static final String TAG = TalkLoader.class.getSimpleName();
    private String talkKey;

    public static TalkLoader getLoader(Context context, String talkKey) {
        return new TalkLoader(context, talkKey);
    }

    private TalkLoader(Context context, String talkKey) {
        super(context, false);
        if (TextUtils.isEmpty(talkKey)) {
            throw new IllegalArgumentException("Talk key cannot be empty");
        }
        this.talkKey = talkKey;
    }

    @Override
    public Optional<TalkViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading talk data");
        }
        Optional<TalkViewDto> newData = realmFacade.loadTalkByKey(talkKey);
        return newData;
    }
}