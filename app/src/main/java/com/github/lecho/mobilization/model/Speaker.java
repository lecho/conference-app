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
public class Speaker {

    public String key;
    public String firstname;
    public String lastname;
    public String bioHtml;
    public String www;
    public String twitter;
    public String photoUrl;

    public static List<Speaker> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Speaker>>() {
        }.getType();

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Speaker> speakersMap = gson.fromJson(json, genericType);
        for (Map.Entry<String, Speaker> entry : speakersMap.entrySet()) {
            entry.getValue().key = entry.getKey();
        }

        List<Speaker> speakers = new ArrayList<>(speakersMap.size());
        speakers.addAll(speakersMap.values());
        return speakers;
    }

    @Override
    public String toString() {
        return "SpeakerData{" +
                "key='" + key + '\'' +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", bioHtml='" + bioHtml + '\'' +
                ", www='" + www + '\'' +
                ", twitter='" + twitter + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
