package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.BreakApiDto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class BreakRealm extends RealmObject {

    @PrimaryKey
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

    public static class BreakConverter extends RealmFacade.RealmConverter<BreakRealm, BreakApiDto> {
        @Override
        public BreakRealm convert(String key, BreakApiDto apiDto) {
            BreakRealm breakRealm = new BreakRealm();
            breakRealm.setKey(key);
            breakRealm.setDescription(apiDto.descriptionHtml);
            breakRealm.setTitle(apiDto.title);
            return breakRealm;
        }
    }
}
