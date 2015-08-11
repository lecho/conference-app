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
public class Speaker {

    public String firstname;
    public String lastname;
    public String bioHtml;
    public String www;
    public String twitter;
    public String photoUrl;

    public static Map<String, Speaker> fromJson(String json) {
        Type genericType = new TypeToken<Map<String, Speaker>>() {
        }.getType();
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Map<String, Speaker> speakersMap = gson.fromJson(json, genericType);
        return speakersMap;
    }

    @Override
    public String toString() {
        return "SpeakerRealm{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bioHtml='" + bioHtml + '\'' +
                ", www='" + www + '\'' +
                ", twitter='" + twitter + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
