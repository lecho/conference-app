package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class AgendaItemApiDto extends BaseApiDto{

    public static final String BREAK_KEY = "break_key";
    public String breakKey;
    public Map<String, TalkItemApiDto> talks = new HashMap<>();

    public static Map<String, AgendaItemApiDto> fromJson(String json, Class clazz) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(AgendaItemApiDto.class, new AgendaItemDeserializer()).create();
        Map<String, AgendaItemApiDto> objectsMap = gson.fromJson(json, GENERIC_TYPES.get(clazz.getName()));
        return objectsMap;
    }

    private static class AgendaItemDeserializer implements JsonDeserializer<AgendaItemApiDto> {

        @Override
        public AgendaItemApiDto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            if (!(json instanceof JsonObject)) {
                throw new IllegalStateException("Could not deserialize AgendaItemApiDto, JsonElement is not instance of " +
                        "JsonObject");
            }
            AgendaItemApiDto agendaItem;
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            JsonObject jsonObject = (JsonObject) json;
            if (jsonObject.has(AgendaItemApiDto.BREAK_KEY)) {
                //BreakRealm item
                agendaItem = gson.fromJson(json, AgendaItemApiDto.class);
            } else {
                //TalkRealm items
                Type genericType = new TypeToken<Map<String, TalkItemApiDto>>() {
                }.getType();
                Map<String, TalkItemApiDto> talksMap = gson.fromJson(json, genericType);
                agendaItem = new AgendaItemApiDto();
                agendaItem.talks = talksMap;
            }
            return agendaItem;
        }
    }

    @Override
    public String toString() {
        return "AgendaItemApiDto{" +
                "breakKey='" + breakKey + '\'' +
                ", talksMap=" + talks +
                '}';
    }

    public static class TalkItemApiDto {
        public String talkKey;

        @Override
        public String toString() {
            return "TalkItemApiDto{" +
                    "talkKey='" + talkKey + '\'' +
                    '}';
        }
    }
}
