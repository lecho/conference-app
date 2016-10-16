package com.github.lecho.mobilization;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Leszek on 2015-09-26.
 */
public class ConferenceApplication extends Application {

    /**
     * Always increment schema version.
     */
    private static final int SCHEMA_VERSION = 10;
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
    }
}
