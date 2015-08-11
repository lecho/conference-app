package com.github.lecho.mobilization.realmmodel;

import io.realm.RealmObject;

/**
 * Created by Leszek on 2015-07-24.
 */
public class VenueRealm extends RealmObject {

    private String key;
    private String title;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
