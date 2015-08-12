package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-12.
 */
public abstract class BaseApiDto {

    public static final Map<String, Type> GENERIC_TYPES = new HashMap<>();

    //Static initialization is needed due to static nature of fromJson method.
    static {
        GENERIC_TYPES.put(AgendaItem.class.getName(), new TypeToken<Map<String, AgendaItem>>() {
        }.getType());
        GENERIC_TYPES.put(BreakApiDto.class.getName(), new TypeToken<Map<String, BreakApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(EventApiDto.class.getName(), new TypeToken<Map<String, EventApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(SlotApiDto.class.getName(), new TypeToken<Map<String, SlotApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(SpeakerApiDto.class.getName(), new TypeToken<Map<String, SpeakerApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(SponsorApiDto.class.getName(), new TypeToken<Map<String, SponsorApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(TalkApiDto.class.getName(), new TypeToken<Map<String, TalkApiDto>>() {
        }.getType());
        GENERIC_TYPES.put(VenueApiDto.class.getName(), new TypeToken<Map<String, VenueApiDto>>() {
        }.getType());
    }

    public static <T> Map<String, T> fromJson(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, T> objectsMap = gson.fromJson(json, GENERIC_TYPES.get(clazz.getName()));
        return objectsMap;
    }
}
