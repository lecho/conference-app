package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class VenuesViewDataLoader extends BaseRealmLoader<List<VenueViewModel>> {

    private static final String TAG = VenuesViewDataLoader.class.getSimpleName();

    public static VenuesViewDataLoader getLoader(Context context) {
        return new VenuesViewDataLoader(context);
    }

    private VenuesViewDataLoader(Context context) {
        super(context);
    }

    @Override
    public List<VenueViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading venues data");
        }
        List<VenueViewModel> newData = realmFacade.loadAllVenues();
        return newData;
    }
}