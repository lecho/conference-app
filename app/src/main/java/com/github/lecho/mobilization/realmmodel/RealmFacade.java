package com.github.lecho.mobilization.realmmodel;

import android.text.TextUtils;
import android.util.Log;

import com.github.lecho.mobilization.apimodel.AgendaItemApiModel;
import com.github.lecho.mobilization.apimodel.ApiData;
import com.github.lecho.mobilization.apimodel.TalkApiModel;
import com.github.lecho.mobilization.rx.DatabaseUpdatedEvent;
import com.github.lecho.mobilization.rx.MyAgendaUpdatedEvent;
import com.github.lecho.mobilization.rx.RxBus;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.viewmodel.AgendaItemViewModel;
import com.github.lecho.mobilization.viewmodel.AgendaViewModel;
import com.github.lecho.mobilization.viewmodel.EventViewModel;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;
import com.github.lecho.mobilization.viewmodel.VenueViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Leszek on 2015-08-04.
 */
public class RealmFacade {

    public static final String TAG = RealmFacade.class.getSimpleName();
    private Map<String, EventRealm> eventRealmsMap;
    private Map<String, SlotRealm> slotRealmsMap;
    private Map<String, BreakRealm> breakRealmsMap;
    private Map<String, TalkRealm> talkRealmsMap;
    private Map<String, VenueRealm> venueRealmsMap;
    private Map<String, SpeakerRealm> speakerRealmsMap;
    private Map<String, SponsorRealm> sponsorRealmMap;

    public RealmFacade() {
    }

    public void deleteAll() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            RxBus.post(new DatabaseUpdatedEvent());
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not delete all data from realm", e);
        } finally {
            closeRealm(realm);
        }
    }

    public void saveData(final ApiData apiData) {
        convertApiDataToRealm(apiData);
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(eventRealmsMap.values());
            realm.copyToRealmOrUpdate(slotRealmsMap.values());
            realm.copyToRealmOrUpdate(breakRealmsMap.values());
            realm.copyToRealmOrUpdate(venueRealmsMap.values());
            realm.copyToRealmOrUpdate(talkRealmsMap.values());
            realm.copyToRealmOrUpdate(speakerRealmsMap.values());
            realm.copyToRealmOrUpdate(sponsorRealmMap.values());
            realm.commitTransaction();
            RxBus.post(new DatabaseUpdatedEvent());
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not save api data to realm", e);
        } finally {
            closeRealm(realm);
        }
    }

    private void convertApiDataToRealm(final ApiData apiData) {
        eventRealmsMap = convertApiDtoToRealm(apiData.eventsMap, new EventRealm.EventApiConverter());
        slotRealmsMap = convertApiDtoToRealm(apiData.slotsMap, new SlotRealm.SlotApiConverter());
        breakRealmsMap = convertApiDtoToRealm(apiData.breaksMap, new BreakRealm.BreakApiConverter());
        talkRealmsMap = convertApiDtoToRealm(apiData.talksMap, new TalkRealm.TalkApiConverter());
        venueRealmsMap = convertApiDtoToRealm(apiData.venuesMap, new VenueRealm.VenueApiConverter());
        speakerRealmsMap = convertApiDtoToRealm(apiData.speakersMap, new SpeakerRealm.SpeakerApiConverter());
        sponsorRealmMap = convertApiDtoToRealm(apiData.sponsorsMap, new SponsorRealm.SponsorApiConverter());
        createTalkSpeakerRelation(apiData);
        createAgendaRelations(apiData);
    }

    /**
     * TALK -> SPEAKERS(based on talks.json)
     * SPEAKER -> TALKS(based on talks.json)
     */
    private void createTalkSpeakerRelation(ApiData apiData) {
        for (Map.Entry<String, TalkRealm> entry : talkRealmsMap.entrySet()) {
            TalkRealm talkRealm = entry.getValue();
            TalkApiModel talkApiModel = apiData.talksMap.get(entry.getKey());
            for (String speakerKey : talkApiModel.speakersKeys) {
                SpeakerRealm speakerRealm = speakerRealmsMap.get(speakerKey);
                talkRealm.getSpeakers().add(speakerRealm);
                //TODO set speakers talks for both direction relation
            }
        }
    }

    /**
     * TALK -> VENUE(based on schedule.json)
     * TALK -> SLOT(based on schedule.json)
     * BREAK -> SLOT(based on schedule.json)
     */
    private void createAgendaRelations(ApiData apiData) {
        for (Map.Entry<String, AgendaItemApiModel> itemEntry : apiData.agendaMap.entrySet()) {
            String slotKey = itemEntry.getKey();
            AgendaItemApiModel agendaItemApiModel = itemEntry.getValue();
            SlotRealm slotRealm = slotRealmsMap.get(slotKey);
            if (TextUtils.isEmpty(agendaItemApiModel.breakKey)) {
                for (Map.Entry<String, AgendaItemApiModel.TalkItemApiDto> talkEntry : agendaItemApiModel.talks.entrySet()) {
                    String venueKey = talkEntry.getKey();
                    VenueRealm venueRealm = venueRealmsMap.get(venueKey);
                    AgendaItemApiModel.TalkItemApiDto agendaTalkItemApiDto = talkEntry.getValue();
                    TalkRealm talkRealm = talkRealmsMap.get(agendaTalkItemApiDto.talkKey);
                    talkRealm.setSlot(slotRealm);
                    talkRealm.setFromInMilliseconds(slotRealm.getFromInMilliseconds());
                    talkRealm.setVenue(venueRealm);
                }
            } else {
                BreakRealm breakRealm = breakRealmsMap.get(agendaItemApiModel.breakKey);
                slotRealm.setIsInMyAgenda(true);//Break slot is always in my agenda
                breakRealm.setSlot(slotRealm);
                breakRealm.setFromInMilliseconds(slotRealm.getFromInMilliseconds());
            }
        }
    }

    public AgendaViewModel loadWholeAgenda() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            //TODO: Wait for sorting by linked realm https://github.com/realm/realm-java/issues/672
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms, null);
        } finally {
            closeRealm(realm);
        }
    }

    public AgendaViewModel loadAgendaForVenue(String venueKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).equalTo("venue.key", venueKey)
                    .findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms, null);
        } finally {
            closeRealm(realm);
        }
    }

    public AgendaViewModel loadMyAgenda() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).equalTo("isInMyAgenda", true)
                    .findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            RealmResults<SlotRealm> slotsRealms = realm.where(SlotRealm.class).equalTo("isInMyAgenda", false)
                    .findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms, slotsRealms);
        } finally {
            closeRealm(realm);
        }
    }

    private AgendaViewModel loadAgenda(RealmResults<TalkRealm> talksRealms, RealmResults<BreakRealm> breaksRealms,
                                       RealmResults<SlotRealm> slotsRealms) {
        TalkRealm.TalkViewConverter talkViewConverter = new TalkRealm.TalkViewConverter();
        BreakRealm.BreakViewConverter breakViewConverter = new BreakRealm.BreakViewConverter();
        AgendaViewModel agendaViewModel = new AgendaViewModel();
        for (TalkRealm talkRealm : talksRealms) {
            AgendaItemViewModel agendaItemViewModel = convertTalkRealmToAgendaItem(talkRealm, talkViewConverter);
            agendaViewModel.agendaItems.add(agendaItemViewModel);
        }
        for (BreakRealm breakRealm : breaksRealms) {
            AgendaItemViewModel agendaItemViewModel = convertBreakRealmToAgendaItem(breakRealm, breakViewConverter);
            agendaViewModel.agendaItems.add(agendaItemViewModel);
        }
        if (slotsRealms != null) {
            SlotRealm.SlotViewConverter slotViewConverter = new SlotRealm.SlotViewConverter();
            for (SlotRealm slotRealm : slotsRealms) {
                AgendaItemViewModel agendaItemViewModel = convertSlotRealmToAgendaItem(slotRealm, slotViewConverter);
                agendaViewModel.agendaItems.add(agendaItemViewModel);
            }
        }
        Collections.sort(agendaViewModel.agendaItems, new AgendaItemComparator());
        return agendaViewModel;
    }

    private AgendaItemViewModel convertTalkRealmToAgendaItem(TalkRealm talkRealm,
                                                             TalkRealm.TalkViewConverter converter) {
        AgendaItemViewModel agendaItemViewModel = new AgendaItemViewModel();
        agendaItemViewModel.type = AgendaItemViewModel.AgendaItemType.TALK;
        agendaItemViewModel.talk = converter.convert(talkRealm);
        agendaItemViewModel.slot = agendaItemViewModel.talk.slot;
        return agendaItemViewModel;
    }

    private AgendaItemViewModel convertBreakRealmToAgendaItem(BreakRealm breakRealm,
                                                              BreakRealm.BreakViewConverter converter) {
        AgendaItemViewModel agendaItemViewModel = new AgendaItemViewModel();
        agendaItemViewModel.type = AgendaItemViewModel.AgendaItemType.BREAK;
        agendaItemViewModel.agendaBreak = converter.convert(breakRealm);
        agendaItemViewModel.slot = agendaItemViewModel.agendaBreak.slot;
        return agendaItemViewModel;
    }

    private AgendaItemViewModel convertSlotRealmToAgendaItem(SlotRealm slotRealm,
                                                             SlotRealm.SlotViewConverter converter) {
        AgendaItemViewModel agendaItemViewModel = new AgendaItemViewModel();
        agendaItemViewModel.type = AgendaItemViewModel.AgendaItemType.SLOT;
        agendaItemViewModel.slot = converter.convert(slotRealm);
        return agendaItemViewModel;
    }

    public Optional<TalkViewModel> loadTalkByKey(String talkKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            TalkRealm talkRealm = loadTalkRealmByKey(realm, talkKey);
            if (talkRealm == null) {
                return Optional.empty();
            }
            return Optional.of(new TalkRealm.TalkViewConverter().convert(talkRealm));
        } finally {
            closeRealm(realm);
        }
    }

    private TalkRealm loadTalkRealmByKey(Realm realm, String talkKey) {
        return realm.where(TalkRealm.class).equalTo("key", talkKey).findFirst();
    }

    private TalkRealm loadFavoriteTalkRealmBySlot(Realm realm, String slotKey) {
        return realm.where(TalkRealm.class).equalTo("isInMyAgenda", true).equalTo("slot.key", slotKey).findFirst();
    }

    public List<TalkViewModel> loadTalksBySlotSortedByVenue(String slotKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<VenueRealm> venuesRealms = realm.where(VenueRealm.class).findAllSorted("title");
            TalkRealm.TalkViewConverter talkViewConverter = new TalkRealm.TalkViewConverter();
            List<TalkViewModel> talkViewModels = new ArrayList<>();
            for (VenueRealm venueRealm : venuesRealms) {
                TalkRealm talkRealm = realm.where(TalkRealm.class).equalTo("venue.key", venueRealm.getKey()).equalTo
                        ("slot.key", slotKey).findFirst();
                if (talkRealm != null) {
                    talkViewModels.add(talkViewConverter.convert(talkRealm));
                }
            }
            return talkViewModels;
        } finally {
            closeRealm(realm);
        }
    }

    public Optional<SpeakerViewModel> loadSpeakerByKey(String speakerKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            SpeakerRealm speakerRealm = realm.where(SpeakerRealm.class).equalTo("key", speakerKey).findFirst();
            if (speakerRealm == null) {
                return Optional.empty();
            }
            return Optional.of(new SpeakerRealm.SpeakerViewConverter().convert(speakerRealm));
        } finally {
            closeRealm(realm);
        }
    }

    public List<SpeakerViewModel> loadAllSpeakers() {
        Realm realm = null;
        try {
            SpeakerRealm.SpeakerViewConverter speakerViewConverter = new SpeakerRealm.SpeakerViewConverter();
            realm = Realm.getDefaultInstance();
            RealmResults<SpeakerRealm> speakersRealms = realm.where(SpeakerRealm.class).findAllSorted("firstName");
            List<SpeakerViewModel> speakerViewModels = new ArrayList<>(speakersRealms.size());
            for (SpeakerRealm speakerRealm : speakersRealms) {
                speakerViewModels.add(speakerViewConverter.convert(speakerRealm));
            }
            return speakerViewModels;
        } finally {
            closeRealm(realm);
        }
    }

    public List<VenueViewModel> loadAllVenues() {
        Realm realm = null;
        try {
            VenueRealm.VenueViewConverter venueViewConverter = new VenueRealm.VenueViewConverter();
            realm = Realm.getDefaultInstance();
            RealmResults<VenueRealm> venuesRealms = realm.where(VenueRealm.class).findAllSorted("title");
            List<VenueViewModel> venueViewModels = new ArrayList<>(venuesRealms.size());
            for (VenueRealm venueRealm : venuesRealms) {
                venueViewModels.add(venueViewConverter.convert(venueRealm));
            }
            return venueViewModels;
        } finally {
            closeRealm(realm);
        }
    }

    public List<SponsorViewModel> loadAllSponsors() {
        Realm realm = null;
        try {
            SponsorRealm.SponsorViewConverter sponsorViewConverter = new SponsorRealm.SponsorViewConverter();
            realm = Realm.getDefaultInstance();
            RealmResults<SponsorRealm> sponsorRealms = realm.where(SponsorRealm.class).findAllSorted(
                    new String[]{"priority", "name"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING});
            List<SponsorViewModel> sponsorViewModels = new ArrayList<>(sponsorRealms.size());
            for (SponsorRealm sponsorRealm : sponsorRealms) {
                sponsorViewModels.add(sponsorViewConverter.convert(sponsorRealm));
            }
            return sponsorViewModels;
        } finally {
            closeRealm(realm);
        }
    }

    public void addTalkToMyAgenda(String talkKey) {
        changeTalkFavoriteState(talkKey, true, true);
    }

    public void removeTalkFromMyAgenda(String talkKey) {
        changeTalkFavoriteState(talkKey, false, true);
    }

    public void removeTalkFromMyAgendaSilent(String talkKey) {
        changeTalkFavoriteState(talkKey, false, false);
    }

    private void changeTalkFavoriteState(String talkKey, boolean isInMyAgenda, boolean postUpdateEvent) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            TalkRealm talkRealm = loadTalkRealmByKey(realm, talkKey);
            realm.beginTransaction();
            talkRealm.setIsInMyAgenda(isInMyAgenda);
            talkRealm.getSlot().setIsInMyAgenda(isInMyAgenda);
            realm.commitTransaction();
            if (postUpdateEvent) {
                RxBus.post(new MyAgendaUpdatedEvent());
            }
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not add talk to my agenda", e);
        } finally {
            closeRealm(realm);
        }
    }

    public Optional<TalkViewModel> getConflictedTalk(String talkKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            TalkRealm talkRealm = loadTalkRealmByKey(realm, talkKey);
            TalkRealm conflictedTalk = loadFavoriteTalkRealmBySlot(realm, talkRealm.getSlot().getKey());
            if (conflictedTalk == null) {
                return Optional.empty();
            }
            return Optional.of(new TalkRealm.TalkViewConverter().convert(conflictedTalk));
        } finally {
            closeRealm(realm);
        }
    }

    public void replaceTalk(String oldTalkKey, String newTalkKey) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            TalkRealm oldTalkRealm = loadTalkRealmByKey(realm, oldTalkKey);
            TalkRealm newTalkRealm = loadTalkRealmByKey(realm, newTalkKey);
            realm.beginTransaction();
            oldTalkRealm.setIsInMyAgenda(false);
            oldTalkRealm.getSlot().setIsInMyAgenda(false);
            newTalkRealm.setIsInMyAgenda(true);
            newTalkRealm.getSlot().setIsInMyAgenda(true);
            realm.commitTransaction();
            RxBus.post(new MyAgendaUpdatedEvent());
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not replace talks, old talk: " + oldTalkKey + ", new talk: " + newTalkKey, e);
        } finally {
            closeRealm(realm);
        }
    }

    public Optional<EventViewModel> loadEvent() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            EventRealm eventRealm = realm.where(EventRealm.class).findFirst();
            if (eventRealm == null) {
                return Optional.empty();
            }
            return Optional.of(new EventRealm.EventViewConverter().convert(eventRealm));
        } finally {
            closeRealm(realm);
        }
    }

    private void closeRealm(Realm realm) {
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    private <R extends RealmObject, A> Map<String, R> convertApiDtoToRealm(Map<String, A>
                                                                                   apiDtoMap,
                                                                           ApiToRealmConverter<R, A>
                                                                                   converter) {
        Map<String, R> resultMap = new HashMap<>(apiDtoMap.size());
        for (Map.Entry<String, A> entry : apiDtoMap.entrySet()) {
            final String key = entry.getKey();
            resultMap.put(key, converter.convert(key, entry.getValue()));
        }
        return resultMap;
    }

    public static abstract class ApiToRealmConverter<R extends RealmObject, A> {
        public abstract R convert(String key, A apiDto);
    }

    public static abstract class RealmToViewConverter<R extends RealmObject, V> {
        public abstract V convert(R realmObject);
    }

    private static class AgendaItemComparator implements Comparator<AgendaItemViewModel> {

        @Override
        public int compare(AgendaItemViewModel lhs, AgendaItemViewModel rhs) {
            return (int) (lhs.slot.fromInMilliseconds - rhs.slot.fromInMilliseconds);
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }
}
