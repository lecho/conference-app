package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class SponsorsLoader extends BaseRealmLoader<List<SponsorViewModel>> {

    private static final String TAG = SponsorsLoader.class.getSimpleName();

    public static SponsorsLoader getLoader(Context context) {
        return new SponsorsLoader(context);
    }

    private SponsorsLoader(Context context) {
        super(context);
    }

    @Override
    public List<SponsorViewModel> loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading speakers data");
        }
        List<SponsorViewModel> newData = realmFacade.loadAllSponsors();
        return newData;
    }
}