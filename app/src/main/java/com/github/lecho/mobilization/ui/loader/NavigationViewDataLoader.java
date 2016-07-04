package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.EventViewModel;
import com.github.lecho.mobilization.viewmodel.NavigationViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class NavigationViewDataLoader extends BaseRealmLoader<NavigationViewModel> {

    private static final String TAG = NavigationViewDataLoader.class.getSimpleName();

    public static NavigationViewDataLoader getLoader(Context context) {
        return new NavigationViewDataLoader(context);
    }

    private NavigationViewDataLoader(Context context) {
        super(context);
    }

    @Override
    public NavigationViewModel loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading navigation view data");
        }
        List<VenueViewModel> venueViewModels = realmFacade.loadAllVenues();
        Optional<EventViewModel> eventViewDto = realmFacade.loadEvent();
        NavigationViewModel newData = new NavigationViewModel();
        newData.venueViewModels = venueViewModels;
        newData.eventViewDto = eventViewDto;
        return newData;
    }
}