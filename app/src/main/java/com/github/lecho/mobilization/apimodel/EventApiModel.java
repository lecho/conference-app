package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class EventApiModel {

    public String title;
    public String data;
    public String time;
    public LocationApiDto location;

    @Override
    public String toString() {
        return "EventApiModel{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", time='" + time + '\'' +
                ", location=" + location +
                '}';
    }

    public static class EventApiParser extends BaseApiParser<EventApiModel> {

        @Override
        public Map<String, EventApiModel> fromJson(String json) {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            EventApiModel eventApiModel = gson.fromJson(json, EventApiModel.class);
            Map<String, EventApiModel> objectsMap = new HashMap<>();
            objectsMap.put("key", eventApiModel);
            return objectsMap;
        }
    }
}
