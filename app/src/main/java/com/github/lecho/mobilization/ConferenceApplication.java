package com.github.lecho.mobilization;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Leszek on 2015-09-26.
 */
public class ConferenceApplication extends Application {

    /**
     * Always increment schema version.
     */
    private static final int SCHEMA_VERSION = 5;

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .name("conference.realm")
                .schemaVersion(SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
