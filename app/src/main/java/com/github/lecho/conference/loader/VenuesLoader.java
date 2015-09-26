package com.github.lecho.conference.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.conference.BuildConfig;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class VenuesLoader extends BaseRealmLoader<List<VenueViewDto>> {

    private static final String TAG = VenuesLoader.class.getSimpleName();

    public static VenuesLoader getVenuesLoader(Context context) {
        return new VenuesLoader(context);
    }

    private VenuesLoader(Context context) {
        super(context, true);
    }

    @Override
    public List<VenueViewDto> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading venues data");
        }
        List<VenueViewDto> newData = realmFacade.loadAllVenues();
        return newData;
    }
}