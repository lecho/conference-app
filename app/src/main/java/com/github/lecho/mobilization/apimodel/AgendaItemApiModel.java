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
public class AgendaItemApiModel {

    public static final String BREAK_KEY = "break_key";
    public String breakKey;
    public Map<String, TalkItemApiDto> talks = new HashMap<>();

    @Override
    public String toString() {
        return "AgendaItemApiModel{" +
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

    public static class AgendaItemApiParser extends BaseApiParser<AgendaItemApiModel> {

        @Override
        public Map<String, AgendaItemApiModel> fromJson(String json) {
            Type type = new TypeToken<Map<String, AgendaItemApiModel>>() {
            }.getType();
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(AgendaItemApiModel.class, new AgendaItemDeserializer()).create();
            Map<String, AgendaItemApiModel> objectsMap = gson.fromJson(json, type);
            return objectsMap;
        }
    }

    private static class AgendaItemDeserializer implements JsonDeserializer<AgendaItemApiModel> {

        @Override
        public AgendaItemApiModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            if (!(json instanceof JsonObject)) {
                throw new IllegalStateException("Could not deserialize AgendaItemApiModel, JsonElement is not instance " +
                        "of JsonObject");
            }
            AgendaItemApiModel agendaItem;
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            JsonObject jsonObject = (JsonObject) json;
            if (jsonObject.has(AgendaItemApiModel.BREAK_KEY)) {
                //BreakRealm item
                agendaItem = gson.fromJson(json, AgendaItemApiModel.class);
            } else {
                //TalkRealm items
                Type genericType = new TypeToken<Map<String, TalkItemApiDto>>() {
                }.getType();
                Map<String, TalkItemApiDto> talksMap = gson.fromJson(json, genericType);
                agendaItem = new AgendaItemApiModel();
                agendaItem.talks = talksMap;
            }
            return agendaItem;
        }
    }
}
