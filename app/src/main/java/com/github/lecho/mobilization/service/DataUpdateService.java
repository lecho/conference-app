package com.github.lecho.mobilization.service;

import android.app.IntentService;
import android.content.Intent;

import com.github.lecho.mobilization.apimodel.ApiData;
import com.github.lecho.mobilization.apimodel.ApiFacade;
import com.github.lecho.mobilization.realmmodel.RealmFacade;

/**
 * Created by Leszek on 2015-09-01.
 */
public class DataUpdateService extends IntentService {

    private static final String TAG = DataUpdateService.class.getSimpleName();

    public DataUpdateService(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //TODO DOWNLOAD JSON DATA
        ApiFacade apiFacade = new ApiFacade();
        ApiData apiData = apiFacade.parseJsonFilesFromAssets(getApplicationContext(), "test-data");
        RealmFacade realmFacade = new RealmFacade(getApplicationContext());
        realmFacade.saveData(apiData);
    }
}
