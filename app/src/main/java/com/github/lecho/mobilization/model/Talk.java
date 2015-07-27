package com.github.lecho.mobilization.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Talk {

    public String key;
    public String title;
    public String descriptionHtml;
    public String[] speakersKeys;
    public String language;

    public static List<Talk> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Talk>>() {
        }.getType();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Talk> talksMap = gson.fromJson(json, genericType);
        for (Map.Entry<String, Talk> entry : talksMap.entrySet()) {
            entry.getValue().key = entry.getKey();
        }

        List<Talk> talks = new ArrayList<>(talksMap.size());
        talks.addAll(talksMap.values());
        return talks;
    }

    @Override
    public String toString() {
        return "Talk{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                ", speakersKeys=" + Arrays.toString(speakersKeys) +
                ", language='" + language + '\'' +
                '}';
    }
}
