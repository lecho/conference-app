package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.TalkApiDto;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class TalkRealm extends RealmObject {

    @PrimaryKey
    private String key;
    private String title;
    private String description;
    private String language;
    private SlotRealm slot;
    private VenueRealm venue;
    private RealmList<SpeakerRealm> speakers = new RealmList<>();

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SlotRealm getSlot() {
        return slot;
    }

    public void setSlot(SlotRealm slot) {
        this.slot = slot;
    }

    public VenueRealm getVenue() {
        return venue;
    }

    public void setVenue(VenueRealm venue) {
        this.venue = venue;
    }

    public RealmList<SpeakerRealm> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(RealmList<SpeakerRealm> speakers) {
        this.speakers = speakers;
    }

    public static class TalkApiConverter extends RealmFacade.ApiToRealmConverter<TalkRealm, TalkApiDto> {

        @Override
        public TalkRealm convert(String key, TalkApiDto apiDto) {
            TalkRealm talkRealm = new TalkRealm();
            talkRealm.setKey(key);
            talkRealm.setDescription(apiDto.descriptionHtml);
            talkRealm.setLanguage(apiDto.language);
            talkRealm.setTitle(apiDto.title);
            return talkRealm;
        }
    }
}
