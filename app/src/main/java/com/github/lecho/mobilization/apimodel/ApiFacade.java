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
    public static final String BREAKS_JSON_FILE = "breaks.json";
    public static final String SLOTS_JSON_FILE = "slots.json";
    public static final String SPEAKERS_JSON_FILE = "speakers.json";
    public static final String SPONSORS_JSON_FILE = "sponsors.json";
    public static final String TALKS_JSON_FILE = "talks.json";
    public static final String VENUES_JSON_FILE = "venues.json";

    public static ApiData parseJsonFilesFromAssets(Context context, String folderName) {
        String agendaJson = readFileFromAsstes(context, folderName, SCHEDULE_JSON_FILE);
        String slotJson = readFileFromAsstes(context, folderName, SLOTS_JSON_FILE);
        String breaksJson = readFileFromAsstes(context, folderName, BREAKS_JSON_FILE);
        String venuesJson = readFileFromAsstes(context, folderName, VENUES_JSON_FILE);
        String talkJson = readFileFromAsstes(context, folderName, TALKS_JSON_FILE);
        String speakersJson = readFileFromAsstes(context, folderName, SPEAKERS_JSON_FILE);

        ApiData apiData = new ApiData();
        apiData.agendaMap = AgendaItemApiDto.fromJson(agendaJson, AgendaItemApiDto.class);
        apiData.slotsMap = SlotApiDto.fromJson(slotJson, SlotApiDto.class);
        apiData.breaksMap = BreakApiDto.fromJson(breaksJson, BreakApiDto.class);
        apiData.venuesMap = VenueApiDto.fromJson(venuesJson, VenueApiDto.class);
        apiData.talksMap = TalkApiDto.fromJson(talkJson, TalkApiDto.class);
        apiData.speakersMap = SpeakerApiDto.fromJson(speakersJson, SpeakerApiDto.class);
        return apiData;
    }

    private static String readFileFromAsstes(Context context, String folderName, String fileName) {
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