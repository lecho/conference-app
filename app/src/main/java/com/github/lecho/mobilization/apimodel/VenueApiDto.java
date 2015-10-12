package com.github.lecho.mobilization.apimodel;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Leszek on 2015-07-24.
 */
public class VenueApiDto {

    public String key;
    public String title;

    @Override
    public String toString() {
        return "VenueRealm{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public static class VenueApiParser extends BaseApiParser<VenueApiDto> {

        @Override
        public Map<String, VenueApiDto> fromJson(String json) {
            Type type = new TypeToken<Map<String, VenueApiDto>>() {
            }.getType();
            return parseJson(json, type);
        }
    }
}
