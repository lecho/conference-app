package com.github.lecho.mobilization.ui.loader;

import android.content.Context;
import android.util.Log;

import com.github.lecho.mobilization.BuildConfig;
import com.github.lecho.mobilization.viewmodel.AgendaViewDto;

/**
 * Created by Leszek on 2015-07-29.
 */
public class AgendaLoader extends BaseRealmLoader<AgendaViewDto> {

    private static final String TAG = AgendaLoader.class.getSimpleName();
    private final AgendaLoaderType type;
    private final String venueKey;

    public static AgendaLoader getWholeAgendaLoader(Context context) {
        return new AgendaLoader(context, AgendaLoaderType.WHOLE_AGENDA, null);
    }

    public static AgendaLoader getMyAgendaLoader(Context context) {
        return new AgendaLoader(context, AgendaLoaderType.MY_AGENDA, null);
    }

    public static AgendaLoader getVenueAgendaLoader(Context context, String venueKey) {
        return new AgendaLoader(context, AgendaLoaderType.VENUE_AGENDA, venueKey);
    }

    private AgendaLoader(Context context, AgendaLoaderType type, String venueKey) {
        super(context);
        this.type = type;
        this.venueKey = venueKey;
    }

    @Override
    public AgendaViewDto loadInBackground() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Loading agenda data for loader type: " + type.name());
        }
        AgendaViewDto agendaData = null;
        switch (type) {
            case WHOLE_AGENDA:
                agendaData = realmFacade.loadWholeAgenda();
                break;
            case VENUE_AGENDA:
                agendaData = realmFacade.loadAgendaForVenue(venueKey);
                break;
            case MY_AGENDA:
                agendaData = realmFacade.loadMyAgenda();
                break;
        }
        return agendaData;
    }

    public enum AgendaLoaderType {
        MY_AGENDA, VENUE_AGENDA, WHOLE_AGENDA
    }
}