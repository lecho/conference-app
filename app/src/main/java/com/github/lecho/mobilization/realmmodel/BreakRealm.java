package com.github.lecho.mobilization.realmmodel;

import io.realm.RealmObject;

/**
 * Created by Leszek on 2015-07-24.
 */
public class BreakRealm extends RealmObject {

    private String key;
    private String title;
    private String description;
    private SlotRealm slot;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SlotRealm getSlot() {
        return slot;
    }

    public void setSlot(SlotRealm slot) {
        this.slot = slot;
    }
}
