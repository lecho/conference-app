package com.github.lecho.mobilization.apimodel;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Slot {

    public String from;
    public String to;

    public static Map<String, Slot> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Slot>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Slot> slotsMap = gson.fromJson(json, genericType);
        return slotsMap;
    }

    @Override
    public String toString() {
        return "SlotRealm{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
