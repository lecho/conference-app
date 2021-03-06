package com.github.lecho.mobilization.realmmodel;

import com.github.lecho.mobilization.apimodel.TalkApiModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

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
    private boolean isInMyAgenda;
    private SlotRealm slot;
    private VenueRealm venue;
    private RealmList<SpeakerRealm> speakers = new RealmList<>();
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

    public boolean isInMyAgenda() {
        return isInMyAgenda;
    }

    public void setIsInMyAgenda(boolean isInMyAgenda) {
        this.isInMyAgenda = isInMyAgenda;
    }

    public long getFromInMilliseconds() {
        return fromInMilliseconds;
    }

    public void setFromInMilliseconds(long fromInMilliseconds) {
        this.fromInMilliseconds = fromInMilliseconds;
    }

    public static class TalkApiConverter extends RealmFacade.ApiToRealmConverter<TalkRealm, TalkApiModel> {

        @Override
        public TalkRealm convert(String key, TalkApiModel apiDto) {
            TalkRealm talkRealm = new TalkRealm();
            talkRealm.setKey(key);
            talkRealm.setDescription(apiDto.descriptionHtml);
            talkRealm.setLanguage(apiDto.language);
            talkRealm.setTitle(apiDto.title);
            return talkRealm;
        }
    }

    public static class TalkViewConverter extends RealmFacade.RealmToViewConverter<TalkRealm, TalkViewModel> {

        private SlotRealm.SlotViewConverter slotViewConverter = new SlotRealm.SlotViewConverter();
        private VenueRealm.VenueViewConverter venueViewConverter = new VenueRealm.VenueViewConverter();
        private SpeakerRealm.SpeakerViewConverter speakerViewConverter = new SpeakerRealm.SpeakerViewConverter();

        @Override
        public TalkViewModel convert(TalkRealm realmObject) {
            TalkViewModel talkViewModel = new TalkViewModel();
            talkViewModel.key = realmObject.getKey();
            talkViewModel.description = realmObject.getDescription();
            talkViewModel.language = realmObject.getLanguage();
            talkViewModel.title = realmObject.getTitle();
            talkViewModel.isInMyAgenda = realmObject.isInMyAgenda();
            talkViewModel.slot = slotViewConverter.convert(realmObject.getSlot());
            talkViewModel.venue = venueViewConverter.convert(realmObject.getVenue());
            for (SpeakerRealm speakerRealm : realmObject.getSpeakers()) {
                talkViewModel.speakers.add(speakerViewConverter.convert(speakerRealm));
            }
            return talkViewModel;
        }
    }
}
