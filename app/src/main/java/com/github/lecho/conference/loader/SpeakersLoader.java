package com.github.lecho.conference.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SpeakersLoader extends BaseRealmLoader<List<SpeakerViewDto>> {

    private static final String TAG = SpeakersLoader.class.getSimpleName();

    public static SpeakersLoader getSpeakersLoader(Context context) {
        return new SpeakersLoader(context);
    }

    private SpeakersLoader(Context context) {
        super(context);
    }

    @Override
    public List<SpeakerViewDto> loadInBackground() {
        List<SpeakerViewDto> newData = realmFacade.loadAllSpeakers();
        return newData;
    }
}