package com.github.lecho.mobilization;

import android.app.Application;

import com.github.lecho.mobilization.async.JsonDataVersion;
import com.github.lecho.mobilization.util.Utils;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Leszek on 2015-09-26.
 */
public class ConferenceApplication extends Application {

    /**
     * Always increment schema version.
     */
    private static final int SCHEMA_VERSION = 8;
    private static final String REALM_NAME = "conference.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .name(REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase.setLogLevel(Logger.Level.DEBUG);
    }
}
