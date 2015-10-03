package com.github.lecho.conference.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SponsorsLoader extends BaseRealmLoader<List<SpeakerViewDto>> {

    private static final String TAG = SponsorsLoader.class.getSimpleName();

    public static SponsorsLoader getSpeakersLoader(Context context) {
        return new SponsorsLoader(context);
    }

    private SponsorsLoader(Context context) {
        super(context, true);
    }

    @Override
    public List<SpeakerViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading speakers data");
        }
        List<SpeakerViewDto> newData = realmFacade.loadAllSpeakers();
        return newData;
    }
}