package com.github.lecho.conference.async;

import android.app.IntentService;
import android.content.Intent;

import com.github.lecho.conference.apimodel.ApiData;
import com.github.lecho.conference.apimodel.ApiFacade;
import com.github.lecho.conference.realmmodel.RealmFacade;
import com.github.lecho.conference.util.Utils;

/**
 * Created by Leszek on 2015-09-01.
 */
public class ContentUpdateService extends IntentService {

    private static final String TAG = ContentUpdateService.class.getSimpleName();

    public ContentUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ApiFacade apiFacade = new ApiFacade();
        ApiData apiData = apiFacade.parseJsonFilesFromAssets(getApplicationContext());
        RealmFacade realmFacade = new RealmFacade(getApplicationContext());
        realmFacade.saveData(apiData);
    }
}
