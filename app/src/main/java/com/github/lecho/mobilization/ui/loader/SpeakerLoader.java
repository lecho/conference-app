package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.SpeakerViewDto;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SpeakerLoader extends BaseRealmLoader<Optional<SpeakerViewDto>> {

    private static final String TAG = SpeakerLoader.class.getSimpleName();
    private String speakerKey;

    public static SpeakerLoader getLoader(Context context, String speakerKey) {
        return new SpeakerLoader(context, speakerKey);
    }

    private SpeakerLoader(Context context, String talkKey) {
        super(context);
        this.speakerKey = talkKey;
    }

    @Override
    public Optional<SpeakerViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading speaker data");
        }
        Optional<SpeakerViewDto> newData = realmFacade.loadSpeakerByKey(speakerKey);
        return newData;
    }
}