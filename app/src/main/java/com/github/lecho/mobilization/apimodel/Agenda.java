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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Agenda extends BaseApiModel {

    public static Map<String, AgendaItem> fromJson(String json) {
        //JsonParser parser = new JsonParser();
        Type genericType = new TypeToken<Map<String, AgendaItem>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(AgendaItem.class, new AgendaItemDeserializer()).create();
        Map<String, AgendaItem> objectsMap = gson.fromJson(json, genericType);
        return objectsMap;
    }

    private static class AgendaItemDeserializer implements JsonDeserializer<AgendaItem> {

        @Override
        public AgendaItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                JsonParseException {
            if (!(json instanceof JsonObject)) {
                throw new IllegalStateException("Could not deserialize AgendaItem, JsonElement is not instance of " +
                        "JsonObject");
            }
            AgendaItem agendaItem;
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            JsonObject jsonObject = (JsonObject) json;
            if (jsonObject.has(AgendaItem.BREAK_KEY)) {
                //Break item
                agendaItem = gson.fromJson(json, AgendaItem.class);
            } else {
                //Talk items
                Type genericType = new TypeToken<Map<String, AgendaTalkItem>>() {
                }.getType();
                Map<String, AgendaTalkItem> talksMap = gson.fromJson(json, genericType);
                agendaItem = new AgendaItem();
                agendaItem.talks = talksMap;
            }
            return agendaItem;
        }
    }

    public static class AgendaItem {
        public static final String BREAK_KEY = "break_key";
        public String slotKey;
        public String breakKey;
        public Map<String, AgendaTalkItem> talks = new HashMap<>();

        @Override
        public String toString() {
            return "AgendaItem{" +
                    "slotKey='" + slotKey + '\'' +
                    ", breakKey='" + breakKey + '\'' +
                    ", talks=" + talks +
                    '}';
        }
    }

    public static class AgendaTalkItem {
        public String talkKey;

        @Override
        public String toString() {
            return "AgendaTalkItem{" +
                    "talkKey='" + talkKey + '\'' +
                    '}';
        }
    }
}
