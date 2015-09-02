package com.github.lecho.conference.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-12.
 */
public class BaseApiParser<T> {

    public Map<String, T> fromJson(String json) {
        Type type = new TypeToken<Map<String, T>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, T> objectsMap = gson.fromJson(json, type);
        return objectsMap;
    }
}
