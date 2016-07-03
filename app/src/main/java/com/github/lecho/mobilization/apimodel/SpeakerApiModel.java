package com.github.lecho.mobilization.apimodel;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SpeakerApiModel {

    public String firstname;
    public String lastname;
    public String bioHtml;
    public String www;
    public String twitter;
    public String photoUrl;

    @Override
    public String toString() {
        return "SpeakerRealm{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", biography='" + bioHtml + '\'' +
                ", www='" + www + '\'' +
                ", twitterProfile='" + twitter + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public static class SpeakerApiParser extends BaseApiParser<SpeakerApiModel> {

        @Override
        public Map<String, SpeakerApiModel> fromJson(String json) {
            Type type = new TypeToken<Map<String, SpeakerApiModel>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
