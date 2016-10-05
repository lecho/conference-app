package com.github.lecho.mobilization.async;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.github.lecho.mobilization.apimodel.ApiData;
import com.github.lecho.mobilization.apimodel.ApiFacade;
import com.github.lecho.mobilization.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-09-01.
 */
public class DatabaseUpdateService extends IntentService {

    private static final String TAG = DatabaseUpdateService.class.getSimpleName();
    private static final String UPDATE_SOURCE = "update-source";

    public DatabaseUpdateService() {
        super(TAG);
    }

    public static void updateFromAssets(Context context) {
        Intent serviceIntent = new Intent(context, DatabaseUpdateService.class);
        serviceIntent.putExtra(UPDATE_SOURCE, JsonSource.ASSETS);
        context.startService(serviceIntent);
    }

    public static void updateFromInternalMemory(Context context) {
        Intent serviceIntent = new Intent(context, DatabaseUpdateService.class);
        serviceIntent.putExtra(UPDATE_SOURCE, JsonSource.INTERNAL_MEMORY);
        context.startService(serviceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        JsonSource source = (JsonSource) intent.getSerializableExtra(UPDATE_SOURCE);
        if (source == null) {
            throw new IllegalArgumentException("Json source cannot be null");
        }
        ApiFacade apiFacade = new ApiFacade();
        ApiData apiData;
        switch (source) {
            case ASSETS:
                apiData = apiFacade.parseJsonFilesFromAssets(getApplicationContext());
                break;
            case INTERNAL_MEMORY:
                apiData = apiFacade.parseJsonFromInternalMemory(getApplicationContext());
                break;
            default:
                throw new IllegalArgumentException("Unknown json source " + source.name());
        }
        RealmFacade realmFacade = new RealmFacade();
        realmFacade.deleteAll();
        realmFacade.saveData(apiData);
    }

    private enum JsonSource {
        ASSETS, INTERNAL_MEMORY
    }
}
