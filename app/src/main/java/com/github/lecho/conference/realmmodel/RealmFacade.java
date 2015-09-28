package com.github.lecho.conference.realmmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.lecho.conference.apimodel.AgendaItemApiDto;
import com.github.lecho.conference.apimodel.ApiData;
import com.github.lecho.conference.apimodel.EventApiDto;
import com.github.lecho.conference.apimodel.TalkApiDto;
import com.github.lecho.conference.loader.ContentChangeObserver;
import com.github.lecho.conference.viewmodel.AgendaItemViewDto;
import com.github.lecho.conference.viewmodel.AgendaViewDto;
import com.github.lecho.conference.viewmodel.EventViewDto;
import com.github.lecho.conference.viewmodel.SpeakerViewDto;
import com.github.lecho.conference.viewmodel.TalkViewDto;
import com.github.lecho.conference.viewmodel.VenueViewDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Leszek on 2015-08-04.
 */
public class RealmFacade {

    public static final String TAG = RealmFacade.class.getSimpleName();
    private final Context context;
    private Realm realm;
    private Map<String, EventRealm> eventRealmsMap;
    private Map<String, SlotRealm> slotRealmsMap;
    private Map<String, BreakRealm> breakRealmsMap;
    private Map<String, TalkRealm> talkRealmsMap;
    private Map<String, VenueRealm> venueRealmsMap;
    private Map<String, SpeakerRealm> speakerRealmsMap;

    public RealmFacade(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveData(final ApiData apiData) {
        convertApiDataToRealm(apiData);
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(eventRealmsMap.values());
            realm.copyToRealmOrUpdate(slotRealmsMap.values());
            realm.copyToRealmOrUpdate(breakRealmsMap.values());
            realm.copyToRealmOrUpdate(venueRealmsMap.values());
            realm.copyToRealmOrUpdate(talkRealmsMap.values());
            realm.copyToRealmOrUpdate(speakerRealmsMap.values());
            realm.commitTransaction();
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not save api data to realm", e);
        } finally {
            closeRealm();
            ContentChangeObserver.emitBroadcast(context.getApplicationContext());
        }
    }

    private void convertApiDataToRealm(final ApiData apiData) {
        eventRealmsMap = convertApiDtoToRealm(apiData.eventsMap, new EventRealm.EventApiConverter());
        slotRealmsMap = convertApiDtoToRealm(apiData.slotsMap, new SlotRealm.SlotApiConverter());
        breakRealmsMap = convertApiDtoToRealm(apiData.breaksMap, new BreakRealm.BreakApiConverter());
        talkRealmsMap = convertApiDtoToRealm(apiData.talksMap, new TalkRealm.TalkApiConverter());
        venueRealmsMap = convertApiDtoToRealm(apiData.venuesMap, new VenueRealm.VenueApiConverter());
        speakerRealmsMap = convertApiDtoToRealm(apiData.speakersMap, new SpeakerRealm.SpeakerApiConverter());
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
            TalkApiDto talkApiDto = apiData.talksMap.get(entry.getKey());
            for (String speakerKey : talkApiDto.speakersKeys) {
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
        for (Map.Entry<String, AgendaItemApiDto> itemEntry : apiData.agendaMap.entrySet()) {
            String slotKey = itemEntry.getKey();
            AgendaItemApiDto agendaItemApiDto = itemEntry.getValue();
            SlotRealm slotRealm = slotRealmsMap.get(slotKey);
            if (TextUtils.isEmpty(agendaItemApiDto.breakKey)) {
                for (Map.Entry<String, AgendaItemApiDto.TalkItemApiDto> talkEntry : agendaItemApiDto.talks.entrySet()) {
                    String venueKey = talkEntry.getKey();
                    VenueRealm venueRealm = venueRealmsMap.get(venueKey);
                    AgendaItemApiDto.TalkItemApiDto agendaTalkItemApiDto = talkEntry.getValue();
                    TalkRealm talkRealm = talkRealmsMap.get(agendaTalkItemApiDto.talkKey);
                    talkRealm.setSlot(slotRealm);
                    talkRealm.setFromInMilliseconds(slotRealm.getFromInMilliseconds());
                    talkRealm.setVenue(venueRealm);
                }
            } else {
                BreakRealm breakRealm = breakRealmsMap.get(agendaItemApiDto.breakKey);
                breakRealm.setSlot(slotRealm);
                breakRealm.setFromInMilliseconds(slotRealm.getFromInMilliseconds());
            }
        }
    }

    public AgendaViewDto loadWholeAgenda() {
        try {
            realm = Realm.getDefaultInstance();
            //TODO: Wait for sorting by linked realm https://github.com/realm/realm-java/issues/672
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms);
        } finally {
            closeRealm();
        }
    }

    public AgendaViewDto loadAgendaForVenue(String venueKey) {
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).equalTo("venue.key", venueKey)
                    .findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms);
        } finally {
            closeRealm();
        }
    }

    public AgendaViewDto loadMyAgenda() {
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<TalkRealm> talksRealms = realm.where(TalkRealm.class).equalTo("isInMyAgenda", true)
                    .findAllSorted("fromInMilliseconds");
            RealmResults<BreakRealm> breaksRealms = realm.where(BreakRealm.class).findAllSorted("fromInMilliseconds");
            return loadAgenda(talksRealms, breaksRealms);
        } finally {
            closeRealm();
        }
    }

    private AgendaViewDto loadAgenda(RealmResults<TalkRealm> talksRealms, RealmResults<BreakRealm> breaksRealms) {
        TalkRealm.TalkViewConverter talkViewConverter = new TalkRealm.TalkViewConverter();
        BreakRealm.BreakViewConverter breakViewConverter = new BreakRealm.BreakViewConverter();
        AgendaViewDto agendaViewDto = new AgendaViewDto();
        for (TalkRealm talkRealm : talksRealms) {
            AgendaItemViewDto agendaItemViewDto = convertTalkRealmToAgendaItem(talkRealm, talkViewConverter);
            agendaViewDto.agendaItems.add(agendaItemViewDto);
        }
        for (BreakRealm breakRealm : breaksRealms) {
            AgendaItemViewDto agendaItemViewDto = convertBreakRealmToAgendaItem(breakRealm, breakViewConverter);
            agendaViewDto.agendaItems.add(agendaItemViewDto);
        }
        Collections.sort(agendaViewDto.agendaItems, new AgendaItemComparator());
        return agendaViewDto;
    }

    private AgendaItemViewDto convertTalkRealmToAgendaItem(TalkRealm talkRealm, TalkRealm.TalkViewConverter
            talkViewConverter) {
        AgendaItemViewDto agendaItemViewDto = new AgendaItemViewDto();
        agendaItemViewDto.type = AgendaItemViewDto.AgendaItemType.TALK;
        agendaItemViewDto.talk = talkViewConverter.convert(talkRealm);
        return agendaItemViewDto;
    }

    private AgendaItemViewDto convertBreakRealmToAgendaItem(BreakRealm breakRealm, BreakRealm.BreakViewConverter
            breakViewConverter) {
        AgendaItemViewDto agendaItemViewDto = new AgendaItemViewDto();
        agendaItemViewDto.type = AgendaItemViewDto.AgendaItemType.BREAK;
        agendaItemViewDto.agendaBreak = breakViewConverter.convert(breakRealm);
        return agendaItemViewDto;
    }

    public TalkViewDto loadTalkByKey(String talkKey) {
        try {
            realm = Realm.getDefaultInstance();
            TalkRealm talkRealm = loadTalkRealmByKey(talkKey);
            if (talkRealm != null) {
                return new TalkRealm.TalkViewConverter().convert(talkRealm);
            } else {
                return null;
            }
        } finally {
            closeRealm();
        }
    }

    private TalkRealm loadTalkRealmByKey(String talkKey) {
        return realm.where(TalkRealm.class).equalTo("key", talkKey).findFirst();
    }

    public SpeakerViewDto loadSpeakerByKey(String speakerKey) {
        try {
            realm = Realm.getDefaultInstance();
            SpeakerRealm speakerRealm = realm.where(SpeakerRealm.class).equalTo("key", speakerKey).findFirst();
            return new SpeakerRealm.SpeakerViewConverter().convert(speakerRealm);
        } finally {
            closeRealm();
        }
    }

    public List<SpeakerViewDto> loadAllSpeakers() {
        try {
            SpeakerRealm.SpeakerViewConverter speakerViewConverter = new SpeakerRealm.SpeakerViewConverter();
            realm = Realm.getDefaultInstance();
            RealmResults<SpeakerRealm> speakersRealms = realm.where(SpeakerRealm.class).findAllSorted("firstName");
            List<SpeakerViewDto> venueViewDtos = new ArrayList<>(speakersRealms.size());
            for (SpeakerRealm speakerRealm : speakersRealms) {
                venueViewDtos.add(speakerViewConverter.convert(speakerRealm));
            }
            return venueViewDtos;
        } finally {
            closeRealm();
        }
    }

    public List<VenueViewDto> loadAllVenues() {
        try {
            VenueRealm.VenueViewConverter venueViewConverter = new VenueRealm.VenueViewConverter();
            realm = Realm.getDefaultInstance();
            RealmResults<VenueRealm> venuesRealms = realm.where(VenueRealm.class).findAll();
            List<VenueViewDto> venueViewDtos = new ArrayList<>(venuesRealms.size());
            for (VenueRealm venueRealm : venuesRealms) {
                venueViewDtos.add(venueViewConverter.convert(venueRealm));
            }
            return venueViewDtos;
        } finally {
            closeRealm();
        }
    }

    public void addTalkToMyAgenda(String talkKey, boolean shouldEmitBroadcast) {
        changeTalkFavoriteState(talkKey, true, shouldEmitBroadcast);
    }

    public void removeTalkFromMyAgenda(String talkKey, boolean shouldEmitBroadcast) {
        changeTalkFavoriteState(talkKey, false, shouldEmitBroadcast);
    }

    public void changeTalkFavoriteState(String talkKey, boolean isInMyAgenda, boolean shouldEmitBroadcast) {
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            TalkRealm talkRealm = loadTalkRealmByKey(talkKey);
            talkRealm.setIsInMyAgenda(isInMyAgenda);
            realm.commitTransaction();
            if (shouldEmitBroadcast) {
                ContentChangeObserver.emitBroadcast(context.getApplicationContext());
            }
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
            Log.e(TAG, "Could not add talk to my agenda", e);
        } finally {
            closeRealm();
        }
    }

    public EventViewDto loadEvent() {
        try {
            EventRealm.EventViewConverter eventViewConverter = new EventRealm.EventViewConverter();
            realm = Realm.getDefaultInstance();
            EventRealm eventRealm = realm.where(EventRealm.class).findFirst();
            EventViewDto eventViewDto = eventViewConverter.convert(eventRealm);
            return eventViewDto;
        } finally {
            closeRealm();
        }
    }

    private void closeRealm() {
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

    private static class AgendaItemComparator implements Comparator<AgendaItemViewDto> {

        @Override
        public int compare(AgendaItemViewDto lhs, AgendaItemViewDto rhs) {
            long lhsValue;
            long rhsValue;
            if (AgendaItemViewDto.AgendaItemType.BREAK == lhs.type) {
                lhsValue = lhs.agendaBreak.slot.fromInMilliseconds;
            } else {
                lhsValue = lhs.talk.slot.fromInMilliseconds;
            }
            if (AgendaItemViewDto.AgendaItemType.BREAK == rhs.type) {
                rhsValue = rhs.agendaBreak.slot.fromInMilliseconds;
            } else {
                rhsValue = rhs.talk.slot.fromInMilliseconds;
            }
            return (int) (lhsValue - rhsValue);
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }
}
