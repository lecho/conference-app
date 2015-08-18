package com.github.lecho.mobilization.apimodel;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-12.
 */
public class ApiFacade {

    private static final String TAG = ApiFacade.class.getSimpleName();
    public static final String SCHEDULE_JSON_FILE = "schedule.json";
    public static final String EVENT_JSON_FILE = "event.json";
    public static final String BREAKS_JSON_FILE = "breakApiDtoMap.json";
    public static final String SLOTS_JSON_FILE = "slotApiDtoMap.json";
    public static final String SPEAKERS_JSON_FILE = "speakerApiDtoMap.json";
    public static final String SPONSORS_JSON_FILE = "sponsors.json";
    public static final String TALKS_JSON_FILE = "talkApiDtoMap.json";
    public static final String VENUES_JSON_FILE = "venueApiDtoMap.json";

    public static ApiData parseJsonsFromAssets(Context context, String folderName) {
        String agendaApi = readFileFromAsstes(context, folderName, SCHEDULE_JSON_FILE);
        String slotApi = readFileFromAsstes(context, folderName, SLOTS_JSON_FILE);
        String breaksApi = readFileFromAsstes(context, folderName, BREAKS_JSON_FILE);
        String talkApi = readFileFromAsstes(context, folderName, TALKS_JSON_FILE);
        Map<String, AgendaItemApiDto> agendaItemMap = AgendaItemApiDto.fromJson(agendaApi, AgendaItemApiDto.class);
        Map<String, SlotApiDto> slotMap = SlotApiDto.fromJson(slotApi, SlotApiDto.class);
        Map<String, BreakApiDto> breakMap = BreakApiDto.fromJson(breaksApi, BreakApiDto.class);
        Map<String, TalkApiDto> talkMap = TalkApiDto.fromJson(talkApi, TalkApiDto.class);

        ApiData apiData = new ApiData();
        apiData.agendaItemApiDtoMap = agendaItemMap;
        apiData.slotApiDtoMap = slotMap;
        apiData.breakApiDtoMap = breakMap;
        apiData.talkApiDtoMap = talkMap;
        return apiData;
    }

    private static String readFileFromAsstes(Context context, String folderName, String fileName) {
        String jsonString = "";
        BufferedInputStream bufferedInputStream = null;
        try {
            final File jsonFile = new File(folderName, fileName);
            final InputStream inputStream = context.getAssets().open(jsonFile.getPath(), Context.MODE_PRIVATE);
            bufferedInputStream = new BufferedInputStream(inputStream);
            final byte[] buffer = new byte[ bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            jsonString = new String(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Could not read file from assets", e);
        } finally {
            if (null != bufferedInputStream) {
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