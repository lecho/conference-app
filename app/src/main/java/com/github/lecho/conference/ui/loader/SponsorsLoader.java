package com.github.lecho.conference.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.github.lecho.conference.viewmodel.SponsorViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SponsorsLoader extends BaseRealmLoader<List<SponsorViewDto>> {

    private static final String TAG = SponsorsLoader.class.getSimpleName();

    public static SponsorsLoader getLoader(Context context) {
        return new SponsorsLoader(context);
    }

    private SponsorsLoader(Context context) {
        super(context);
    }

    @Override
    public List<SponsorViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading speakers data");
        }
        List<SponsorViewDto> newData = realmFacade.loadAllSponsors();
        return newData;
    }
}