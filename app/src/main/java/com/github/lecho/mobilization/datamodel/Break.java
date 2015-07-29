package com.github.lecho.mobilization.datamodel;

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
public class Break {

    public String key;
    public String title;
    public String descriptionHtml;

    public static List<Break> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Break>>() {
        }.getType();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Break> breaksMap = gson.fromJson(json, genericType);
        for (Map.Entry<String, Break> entry : breaksMap.entrySet()) {
            entry.getValue().key = entry.getKey();
        }

        List<Break> breaks = new ArrayList<>(breaksMap.size());
        breaks.addAll(breaksMap.values());
        return breaks;
    }

    @Override
    public String toString() {
        return "Break{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                '}';
    }
}
