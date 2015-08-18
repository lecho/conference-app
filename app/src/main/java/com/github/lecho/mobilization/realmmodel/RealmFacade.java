package com.github.lecho.mobilization.realmmodel;

import android.content.Context;
import android.text.TextUtils;

import com.github.lecho.mobilization.apimodel.AgendaItemApiDto;
import com.github.lecho.mobilization.apimodel.ApiData;
import com.github.lecho.mobilization.apimodel.BaseApiDto;
import com.github.lecho.mobilization.apimodel.BreakApiDto;
import com.github.lecho.mobilization.apimodel.SlotApiDto;
import com.github.lecho.mobilization.apimodel.SpeakerApiDto;
import com.github.lecho.mobilization.apimodel.TalkApiDto;
import com.github.lecho.mobilization.apimodel.VenueApiDto;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Leszek on 2015-08-04.
 */
public class RealmFacade {

    private Context context;
    private Realm realm;

    public RealmFacade(Context context) {
        this.context = context;
    }

    public void saveApiData(final ApiData apiData) {
        realm = Realm.getInstance(context);
        saveAgenda(apiData);
        realm.commitTransaction();
        realm.close();
        //TODO: Finally
    }

    public <R extends RealmObject, A extends BaseApiDto> Map<String, R> convertApiDtoToRealm(Map<String, A>
                                                                                                     apiDtoMap,
                                                                                             RealmConverter<R, A>
                                                                                                     converter) {
        Map<String, R> resultMap = new HashMap<>(apiDtoMap.size());
        for (Map.Entry<String, A> entry : apiDtoMap.entrySet()) {
            final String key = entry.getKey();
            resultMap.put(key, converter.convert(key, entry.getValue()));
        }
        return resultMap;
    }

    public static abstract class RealmConverter<R extends RealmObject, A extends BaseApiDto> {
        public abstract R convert(String key, A apiDto);
    }

    private void saveAgenda(final ApiData apiData) {
        Map<String, AgendaItemApiDto> agendaItemMap = apiData.agendaItemApiDtoMap;
        Map<String, TalkApiDto> talkApiDtoMap = apiData.talkApiDtoMap;
        Map<String, SlotApiDto> slotApiDtoMap = apiData.slotApiDtoMap;
        Map<String, VenueApiDto> venueApiDtoMap = apiData.venueApiDtoMap;
        Map<String, SpeakerApiDto> speakerApiDtoMap = apiData.speakerApiDtoMap;
        Map<String, BreakApiDto> breakApiDtoMap = apiData.breakApiDtoMap;

        Map<String, SlotRealm> slotRealmMap = convertApiDtoToRealm(slotApiDtoMap, new SlotRealm.SlotConverter());
        Map<String, BreakRealm> breakRealmMap = convertApiDtoToRealm(breakApiDtoMap, new BreakRealm.BreakConverter());
        Map<String, TalkRealm> talkRealmsMap = convertApiDtoToRealm(talkApiDtoMap, new TalkRealm.TalkConverter());
        Map<String, VenueRealm> venueRealmMap = convertApiDtoToRealm(venueApiDtoMap, new VenueRealm.VenueConverter());
        Map<String, SpeakerRealm> speakerRealmMap = convertApiDtoToRealm(speakerApiDtoMap, new SpeakerRealm
                .SpeakerConverter());

        //TODO use relation resolvers
        // TALK -> SPEAKERS(based on talks.json)
        // SPEAKER -> TALKS(based on talks.json)
        // TALK -> VENUE(based on schedule.json)
        // TALK -> SLOT(based on schedule.json)
        // BREAK -> SLOT(based on schedule.json)

//        for (Map.Entry<String, AgendaItemApiDto> itemEntry : agendaItemMap.entrySet()) {
//            String key = itemEntry.getKey();
//            AgendaItemApiDto item = itemEntry.getValue();
//
//            if (TextUtils.isEmpty(item.breakKey)) {
//                for (Map.Entry<String, AgendaItemApiDto.AgendaTalkItem> talkEntry : item.talks.entrySet()) {
//                    final String talkKey = talkEntry.getKey();
//                    final AgendaItemApiDto.AgendaTalkItem agendaTalkItem = talkEntry.getValue();
//                    TalkApiDto talk = talkApiDtoMap.get(talkKey);
//                    TalkRealm talkRealm = new TalkRealm();
//                    talkRealm.setKey(talkKey);
//                    talkRealm.setSlot(slotRealm);
//                    talkRealm.setDescription(talk.descriptionHtml);
//                    talkRealm.setLanguage(talk.language);
//                    talkRealm.setTitle(talk.title);
//                    for (String speaker : talk.speakersKeys) {
//                        //TODO speakerApiDtoMap
//                    }
//
//                    //TODO venue
//                }
//                //TODO: Save talkApiDtoMap or add to list
//            } else {
//                BreakApiDto breakk = breakApiDtoMap.get(itemEntry.getValue().breakKey);
//                BreakRealm breakRealm = new BreakRealm();
//                breakRealm.setKey(itemEntry.getValue().breakKey);
//                breakRealm.setDescription(breakk.descriptionHtml);
//                breakRealm.setSlot(slotRealm);
//                //TODO: Save break or add to list
//            }
//        }
    }
}
