package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.VenueApiDto;
import com.github.lecho.mobilization.viewmodel.VenueViewDto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class VenueRealm extends RealmObject {

    @PrimaryKey
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

    public static class VenueApiConverter extends RealmFacade.ApiToRealmConverter<VenueRealm, VenueApiDto> {

        @Override
        public VenueRealm convert(String key, VenueApiDto apiDto) {
            VenueRealm venueRealm = new VenueRealm();
            venueRealm.setKey(key);
            venueRealm.setTitle(apiDto.title);
            return venueRealm;
        }
    }

    public static class VenueViewConverter extends RealmFacade.RealmToViewConverter<VenueRealm, VenueViewDto> {

        @Override
        public VenueViewDto convert(VenueRealm realmObject) {
            VenueViewDto venueViewDto = new VenueViewDto();
            venueViewDto.key = realmObject.getKey();
            venueViewDto.title = realmObject.getTitle();
            return venueViewDto;
        }
    }
}
