package com.github.lecho.mobilization.apimodel;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public ApiData parseJsonFilesFromAssets(Context context, String folderName) {
        String agendaJson = readFileFromAsstes(context, folderName, SCHEDULE_JSON_FILE);
        String slotJson = readFileFromAsstes(context, folderName, SLOTS_JSON_FILE);
        String breaksJson = readFileFromAsstes(context, folderName, BREAKS_JSON_FILE);
        String venuesJson = readFileFromAsstes(context, folderName, VENUES_JSON_FILE);
        String talkJson = readFileFromAsstes(context, folderName, TALKS_JSON_FILE);
        String speakersJson = readFileFromAsstes(context, folderName, SPEAKERS_JSON_FILE);

        Map<ApiDtoType, String> jsonsMap = new HashMap<>();
        jsonsMap.put(ApiDtoType.AGENDA, agendaJson);
        jsonsMap.put(ApiDtoType.SLOTS, slotJson);
        jsonsMap.put(ApiDtoType.BREAKS, breaksJson);
        jsonsMap.put(ApiDtoType.VENUES, venuesJson);
        jsonsMap.put(ApiDtoType.TALKS, talkJson);
        jsonsMap.put(ApiDtoType.SPEAKERS, speakersJson);

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
                apiData.breaksMap = new BaseApiParser<BreakApiDto>().fromJson(json);
                break;
            case EVENT:
                break;
            case AGENDA:
                apiData.agendaMap = new AgendaItemApiDto.AgendaItemApiParser().fromJson(json);
                break;
            case SLOTS:
                apiData.slotsMap = new BaseApiParser<SlotApiDto>().fromJson(json);
                break;
            case SPEAKERS:
                apiData.speakersMap = new BaseApiParser<SpeakerApiDto>().fromJson(json);
                break;
            case SPONSORS:
                break;
            case TALKS:
                apiData.talksMap = new BaseApiParser<TalkApiDto>().fromJson(json);
                break;
            case VENUES:
                apiData.venuesMap = new BaseApiParser<VenueApiDto>().fromJson(json);
                break;
        }
        return apiData;
    }

    private String readFileFromAsstes(Context context, String folderName, String fileName) {
        String jsonString = "";
        BufferedInputStream bufferedInputStream = null;
        try {
            File jsonFile = new File(folderName, fileName);
            InputStream inputStream = context.getAssets().open(jsonFile.getPath(), Context.MODE_PRIVATE);
            bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            jsonString = new String(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Could not read file from assets", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close input stream", e);
                }
            }
        }
        return jsonString;
    }
}