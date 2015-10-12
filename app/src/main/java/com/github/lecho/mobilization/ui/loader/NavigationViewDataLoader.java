package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.EventViewDto;
import com.github.lecho.mobilization.viewmodel.NavigationViewDto;
import com.github.lecho.mobilization.viewmodel.VenueViewDto;

import java.util.List;

/**
 * Created by Leszek on 2015-09-03.
 */
public class NavigationViewDataLoader extends BaseRealmLoader<NavigationViewDto> {

    private static final String TAG = NavigationViewDataLoader.class.getSimpleName();

    public static NavigationViewDataLoader getLoader(Context context) {
        return new NavigationViewDataLoader(context);
    }

    private NavigationViewDataLoader(Context context) {
        super(context);
    }

    @Override
    public NavigationViewDto loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading navigation view data");
        }
        List<VenueViewDto> venueViewDtos = realmFacade.loadAllVenues();
        Optional<EventViewDto> eventViewDto = realmFacade.loadEvent();
        NavigationViewDto newData = new NavigationViewDto();
        newData.venueViewDtos = venueViewDtos;
        newData.eventViewDto = eventViewDto;
        return newData;
    }
}