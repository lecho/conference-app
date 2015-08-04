package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-04.
 */
public class BaseApiModel {

    public static <T> Map<String, T> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, T>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, T> talksMap = gson.fromJson(json, genericType);
        return talksMap;
    }

}
