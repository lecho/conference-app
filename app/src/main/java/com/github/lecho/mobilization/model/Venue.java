package com.github.lecho.mobilization.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Venue {

    public String key;
    public String title;

    public static List<Venue> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Venue>>() {
        }.getType();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Venue> venuesMap = gson.fromJson(json, genericType);
        for (Map.Entry<String, Venue> entry : venuesMap.entrySet()) {
            entry.getValue().key = entry.getKey();
        }

        List<Venue> venues = new ArrayList<>(venuesMap.size());
        venues.addAll(venuesMap.values());
        return venues;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
