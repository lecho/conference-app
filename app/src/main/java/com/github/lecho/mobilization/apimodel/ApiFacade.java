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
        final String jsonFolder = Utils.getJsonAssetsFolder();
        return parseJsonFiles(context, new AssetsJsonFileReader(), jsonFolder);
    }

    public ApiData parseJsonFromInternalMemory(Context context) {
        final String jsonFolder = Utils.getJsonInternalMemoryFolder(context);
        return parseJsonFiles(context, new InternalMemoryJsonFileReader(), jsonFolder);
    }

    private ApiData parseJsonFiles(Context context, JsonFileReader jsonFileReader, String folderName) {
        String eventJson = jsonFileReader.readJsonFile(context, folderName, EVENT_JSON_FILE);
        String agendaJson = jsonFileReader.readJsonFile(context, folderName, SCHEDULE_JSON_FILE);
        String slotJson = jsonFileReader.readJsonFile(context, folderName, SLOTS_JSON_FILE);
        String breaksJson = jsonFileReader.readJsonFile(context, folderName, BREAKS_JSON_FILE);
        String venuesJson = jsonFileReader.readJsonFile(context, folderName, VENUES_JSON_FILE);
        String talkJson = jsonFileReader.readJsonFile(context, folderName, TALKS_JSON_FILE);
        String speakersJson = jsonFileReader.readJsonFile(context, folderName, SPEAKERS_JSON_FILE);
        String sponsorJson = jsonFileReader.readJsonFile(context, folderName, SPONSORS_JSON_FILE);

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

    private ApiData parseJsons(Map<ApiDtoType, String> jsonsMap) {
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
                apiData.venuesMap = new VenueApiModel.VenueApiParser().fromJson(json);
                break;
        }
        return apiData;
    }

    private interface JsonFileReader {
        public String readJsonFile(Context context, String folderName, String fileName);
    }

    private class AssetsJsonFileReader implements JsonFileReader {

        @Override
        public String readJsonFile(Context context, String folderName, String fileName) {
            return Utils.readFileFromAssets(context, folderName, fileName);
        }
    }

    private class InternalMemoryJsonFileReader implements JsonFileReader {

        @Override
        public String readJsonFile(Context context, String folderName, String fileName) {
            return Utils.readFileFromInternalStorage(context, folderName, fileName);
        }
    }
}