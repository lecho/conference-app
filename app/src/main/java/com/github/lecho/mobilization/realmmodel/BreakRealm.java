package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.BreakApiModel;
import com.github.lecho.mobilization.viewmodel.BreakViewDto;

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
    /**
     * Realm doesn't support sorting by relation for now so keep this value here
     */
    private long fromInMilliseconds;

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

    public long getFromInMilliseconds() {
        return fromInMilliseconds;
    }

    public void setFromInMilliseconds(long fromInMilliseconds) {
        this.fromInMilliseconds = fromInMilliseconds;
    }

    public static class BreakApiConverter extends RealmFacade.ApiToRealmConverter<BreakRealm, BreakApiModel> {
        @Override
        public BreakRealm convert(String key, BreakApiModel apiDto) {
            BreakRealm breakRealm = new BreakRealm();
            breakRealm.setKey(key);
            breakRealm.setDescription(apiDto.descriptionHtml);
            breakRealm.setTitle(apiDto.title);
            return breakRealm;
        }
    }

    public static class BreakViewConverter extends RealmFacade.RealmToViewConverter<BreakRealm, BreakViewDto> {

        private SlotRealm.SlotViewConverter slotViewConverter = new SlotRealm.SlotViewConverter();

        @Override
        public BreakViewDto convert(BreakRealm realmObject) {
            BreakViewDto breakViewDto = new BreakViewDto();
            breakViewDto.key = realmObject.getKey();
            breakViewDto.title = realmObject.getTitle();
            breakViewDto.description = realmObject.getDescription();
            breakViewDto.slot = slotViewConverter.convert(realmObject.getSlot());
            return breakViewDto;
        }
    }
}
