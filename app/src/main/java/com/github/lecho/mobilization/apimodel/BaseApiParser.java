package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-08-12.
 */
public abstract class BaseApiParser<T> {

    public abstract Map<String, T> fromJson(String json);

    /**
     * @param json
     * @param type Type type = new TypeToken<Map<String, T>>() {}.getType();
     * @return
     */
    protected Map<String, T> parseJson(String json, Type type) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, T> objectsMap = gson.fromJson(json, type);
        return objectsMap;
    }
}
