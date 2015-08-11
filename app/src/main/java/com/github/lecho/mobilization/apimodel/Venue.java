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
public class Venue {

    public String key;
    public String title;

    public static Map<String, Venue> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Venue>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Venue> venuesMap = gson.fromJson(json, genericType);
        return venuesMap;
    }

    @Override
    public String toString() {
        return "VenueRealm{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
