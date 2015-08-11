package com.github.lecho.mobilization.apimodel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class Talk {

    public String title;
    public String descriptionHtml;
    public String[] speakersKeys;
    public String language;

    public static Map<String, Talk> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Talk>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Talk> talksMap = gson.fromJson(json, genericType);
        return talksMap;
    }

    @Override
    public String toString() {
        return "TalkRealm{" +
                "title='" + title + '\'' +
                ", descriptionHtml='" + descriptionHtml + '\'' +
                ", speakersKeys=" + Arrays.toString(speakersKeys) +
                ", language='" + language + '\'' +
                '}';
    }
}
