package com.github.lecho.mobilization.apimodel;

import android.content.Context;

import com.github.lecho.mobilization.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-12.
 */
public class ApiFacade {

    private static final String TAG = ApiFacade.class.getSimpleName();
    private static final String SCHEDULE_JSON_FILE = "schedule.json";
    private static final String EVENT_JSON_FILE = "event.json";
    private static final String BREAKS_JSON_FILE = "breaks.json";
    private static final String SLOTS_JSON_FILE = "slots.json";
    private static final String SPEAKERS_JSON_FILE = "speakers.json";
    private static final String SPONSORS_JSON_FILE = "sponsors.json";
    private static final String TALKS_JSON_FILE = "talks.json";
    private static final String VENUES_JSON_FILE = "venues.json";

    public enum ApiDtoType {
        AGENDA, EVENT, BREAKS, SLOTS, SPEAKERS, SPONSORS, TALKS, VENUES
    }

    public ApiData parseJsonFilesFromAssets(Context context) {
        final String jsonFolder = Utils.getJsonFolder();
        String eventJson = Utils.readFileFromAssets(context, jsonFolder, EVENT_JSON_FILE);
        String agendaJson = Utils.readFileFromAssets(context, jsonFolder, SCHEDULE_JSON_FILE);
        String slotJson = Utils.readFileFromAssets(context, jsonFolder, SLOTS_JSON_FILE);
        String breaksJson = Utils.readFileFromAssets(context, jsonFolder, BREAKS_JSON_FILE);
        String venuesJson = Utils.readFileFromAssets(context, jsonFolder, VENUES_JSON_FILE);
        String talkJson = Utils.readFileFromAssets(context, jsonFolder, TALKS_JSON_FILE);
        String speakersJson = Utils.readFileFromAssets(context, jsonFolder, SPEAKERS_JSON_FILE);
        String sponsorJson = Utils.readFileFromAssets(context, jsonFolder, SPONSORS_JSON_FILE);

        Map<ApiDtoType, String> jsonsMap = new HashMap<>();
        jsonsMap.put(ApiDtoType.EVENT, eventJson);
        jsonsMap.put(ApiDtoType.AGENDA, agendaJson);
        jsonsMap.put(ApiDtoType.SLOTS, slotJson);
        jsonsMap.put(ApiDtoType.BREAKS, breaksJson);
        jsonsMap.put(ApiDtoType.VENUES, venuesJson);
        jsonsMap.put(ApiDtoType.TALKS, talkJson);
        jsonsMap.put(ApiDtoType.SPEAKERS, speakersJson);
        jsonsMap.put(ApiDtoType.SPONSORS, sponsorJson);

        return parseJsons(jsonsMap);
    }

    public ApiData parseJsons(Map<ApiDtoType, String> jsonsMap) {
        ApiData apiData = new ApiData();
        for (Map.Entry<ApiDtoType, String> entry : jsonsMap.entrySet()) {
            apiData = assignParsedData(apiData, entry.getKey(), entry.getValue());
        }
        return apiData;
    }

    private ApiData assignParsedData(ApiData apiData, ApiDtoType className, String json) {
        switch (className) {
            case BREAKS:
                apiData.breaksMap = new BreakApiModel.BreakApiParser().fromJson(json);
                break;
            case EVENT:
                apiData.eventsMap = new EventApiModel.EventApiParser().fromJson(json);
                break;
            case AGENDA:
                apiData.agendaMap = new AgendaItemApiModel.AgendaItemApiParser().fromJson(json);
                break;
            case SLOTS:
                apiData.slotsMap = new SlotApiModel.SlotApiParser().fromJson(json);
                break;
            case SPEAKERS:
                apiData.speakersMap = new SpeakerApiModel.SpeakerApiParser().fromJson(json);
                break;
            case SPONSORS:
                apiData.sponsorsMap = new SponsorApiModel.SponsorApiParser().fromJson(json);
                break;
            case TALKS:
                apiData.talksMap = new TalkApiModel.TalkApiParser().fromJson(json);
                break;
            case VENUES:
                apiData.venuesMap = new VenueApiDto.VenueApiParser().fromJson(json);
                break;
        }
        return apiData;
    }
}