package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SpeakersLoader extends BaseRealmLoader<List<SpeakerViewModel>> {

    private static final String TAG = SpeakersLoader.class.getSimpleName();

    public static SpeakersLoader getLoader(Context context) {
        return new SpeakersLoader(context);
    }

    private SpeakersLoader(Context context) {
        super(context);
    }

    @Override
    public List<SpeakerViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading speakers data");
        }
        List<SpeakerViewModel> newData = realmFacade.loadAllSpeakers();
        return newData;
    }
}